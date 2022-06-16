import { Controller, Delete, Get, Param, Post } from '@nestjs/common';
import { ProfilesService } from '../services/profiles.service';

@Controller('/api/profiles')
export class ProfilesController {
    constructor(private readonly profilesService: ProfilesService) {}

    @Get(':username')
    async getProfile(@Param('username') username: string) {
        return await this.profilesService.getProfile(username);
    }

    @Post(':username/follow')
    async following(@Param('username') username: string) {
        return await this.profilesService.following(username);
    }

    @Delete(':username/follow')
    async unfollowing(@Param('username') username: string) {
        return await this.profilesService.unfollowing(username);
    }
}
