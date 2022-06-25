import { BadRequestException, Injectable, InternalServerErrorException, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { UsersRepo } from 'src/databases/repositories/users.repo';
import * as bcrypt from 'bcrypt';
import { ReqLoginDto, ReqSignUpDto, ReqUpdateDto } from 'src/commons/dto/users.dto';
import { AuthInfo, JwtPayload } from 'src/commons/common.type';

@Injectable()
export class UsersService {
    constructor(private readonly _jwtService: JwtService, private readonly _usersRepo: UsersRepo) {}

    async checkCurrentUser(auth_info: AuthInfo) {
        const searched_user = await this._usersRepo.findUsersByPk(auth_info.user_uid);
        return {
            user: {
                email: searched_user.email,
                token: auth_info.token,
                username: searched_user.username,
                bio: searched_user.bio,
                image: searched_user.image,
            },
        };
    }

    async signUp(user: ReqSignUpDto) {
        const { password, username } = user;

        const found = await this._usersRepo.findByUsername(username);
        if (found) throw new UnauthorizedException();

        const hashed = await bcrypt.hash(password, 10);
        const insert_ret = await this._usersRepo.createNewUser(user, hashed);
        if (!insert_ret) throw new InternalServerErrorException();
        const user_inserted = await this._usersRepo.findUsersByPk(insert_ret.identifiers[0].uid);
        const payload: JwtPayload = {
            user_uid: user_inserted.uid,
        };
        const token = this._jwtService.sign(payload);
        return {
            user: {
                email: user_inserted.email,
                token,
                username: user_inserted.username,
                bio: user_inserted.bio,
                image: user_inserted.image,
            },
        };
    }

    async modifiedInfo(user: ReqUpdateDto, auth_info: AuthInfo) {
        const found = await this._usersRepo.findUsersByPk(auth_info.user_uid);
        if (!found) throw new BadRequestException();

        user.password ? (user.password = await bcrypt.hash(user.password, 10)) : '';

        const updated_ret = await this._usersRepo.updateByUserId(auth_info.user_uid, user);
        if (!updated_ret) throw new InternalServerErrorException();

        const searched_user = await this._usersRepo.findUsersByPk(auth_info.user_uid);
        if (!searched_user) throw new InternalServerErrorException();

        return {
            user: {
                email: searched_user.email,
                token: auth_info.token,
                username: searched_user.username,
                bio: searched_user.bio,
                image: searched_user.image,
            },
        };
    }

    async login(user: ReqLoginDto) {
        const searched_user = await this._usersRepo.findUsersByEmail(user.email);
        if (!user) throw new UnauthorizedException();
        const payload: JwtPayload = {
            user_uid: searched_user.uid,
        };
        const token = this._jwtService.sign(payload);
        return {
            user: {
                email: searched_user.email,
                token,
                username: searched_user.username,
                bio: searched_user.bio,
                image: searched_user.image,
            },
        };
    }
}
