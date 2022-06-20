import { Body, Controller, Get, Post, Put, UsePipes, ValidationPipe } from '@nestjs/common';
import { SignUpDto } from '../dto/users.dto';
import { UsersService } from '../services/users.service';

@Controller('/api/users')
@UsePipes(ValidationPipe)
export class UsersController {
    constructor(private readonly usersService: UsersService) {}

    @Get()
    async checkCurrentUser() {
        return this.usersService.checkCurrentUser();
    }

    @Post()
    async signUp(@Body('user') user: SignUpDto) {
        return this.usersService.signUp(user);
    }

    @Put()
    async modifiedInfo(@Body('user') user: any) {
        return this.usersService.modifiedInfo(user);
    }

    @Post('login')
    async login(@Body('user') user: any) {
        return this.usersService.login(user);
    }
}
