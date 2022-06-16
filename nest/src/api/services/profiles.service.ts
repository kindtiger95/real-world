import { Injectable } from '@nestjs/common';

@Injectable()
export class ProfilesService {
    async getProfile(username: string) {
        return username;
    }

    async following(username: string) {
        return username;
    }

    async unfollowing(username: string) {
        return username;
    }
}
