import { Injectable } from '@nestjs/common';
import { SignUpDto } from '../dto/users.dto';

@Injectable()
export class UsersService {
    async checkCurrentUser() {
        return '';
    }

    async signUp(user: SignUpDto) {
        return user;
    }

    async modifiedInfo(user: any) {
        return user;
    }

    async login(user: any) {
        return user;
    }
}
