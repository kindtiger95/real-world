import { Body, Controller, Get, Post, Put, UseGuards, UsePipes, ValidationPipe } from '@nestjs/common';
import { ReqLoginDto, ReqSignUpDto } from 'src/commons/dto/users.dto';
import { JwtAuthGuard } from 'src/commons/guard/auth/jwt.strategy';
import { UsersService } from '../services/users.service';

@Controller('/api')
@UsePipes(ValidationPipe)
export class UsersController {
    constructor(private readonly usersService: UsersService) {}

    @Get('user')
    @UseGuards(JwtAuthGuard)
    async checkCurrentUser() {
        return this.usersService.checkCurrentUser();
    }

    @Post('users')
    async signUp(@Body('user') user: ReqSignUpDto) {
        return this.usersService.signUp(user);
    }

    @Put('user')
    async modifiedInfo(@Body('user') user: any) {
        return this.usersService.modifiedInfo(user);
    }

    @Post('users/login')
    async login(@Body('user') user: ReqLoginDto) {
        return this.usersService.login(user);
    }
}
