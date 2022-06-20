import { Injectable, InternalServerErrorException, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { UsersRepo } from 'src/databases/repositories/users.repo';
import { SignUpDto } from '../dto/users.dto';
import * as bcrypt from 'bcrypt';

@Injectable()
export class UsersService {
    constructor(private readonly _jwtService: JwtService, private readonly _usersRepo: UsersRepo) {}

    async checkCurrentUser() {
        return '';
    }

    async signUp(user: SignUpDto) {
        const { email, password, username } = user;

        const found = await this._usersRepo.findByUsername(username);
        if (!found) throw new UnauthorizedException();

        const hashed = await bcrypt.hash(password, 10);
        const userId = await this._usersRepo.createNewUser(user, hashed);

        if (!userId) throw new InternalServerErrorException();
        const token = this._jwtService.sign(userId);
        return {
            user: {
                email,
                token,
                username,
                bio: '',
                image: null,
            },
        };
    }

    async modifiedInfo(user: any) {
        return user;
    }

    async login(user: any) {
        return user;
    }
}
