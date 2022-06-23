import { ExecutionContext, Injectable, UnauthorizedException } from '@nestjs/common';
import { AuthGuard, PassportStrategy } from '@nestjs/passport';
import { Request } from 'express';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { UsersRepo } from 'src/databases/repositories/users.repo';
import { JwtPayload } from '../common.type';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
    constructor(private readonly usersRepo: UsersRepo) {
        super({
            jwtFromRequest: ExtractJwt.fromHeader('Token'),
            ignoreExpiration: false,
            secretOrKey: 'test',
        });
    }

    async validate(jwtPayload: JwtPayload) {
        const user = await this.usersRepo.findUsersByPk(jwtPayload.user_uid);
        if (!user) throw new UnauthorizedException();
        return jwtPayload;
    }
}

@Injectable()
export class JwtAuthGuard extends AuthGuard('jwt') {
    handleRequest<TUser = any>(err: any, user: any, info: any, context: ExecutionContext): TUser {
        console.log('1');
        if (err || info || !user) throw err || new UnauthorizedException();
        const getBody = context.switchToHttp().getRequest<Request>().body;
        getBody.user_uid = user.user_uid;
        return user;
    }
}
