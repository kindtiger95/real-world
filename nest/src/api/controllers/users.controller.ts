import { Body, Controller, Get, Headers, Post, Put, UseGuards, UsePipes, ValidationPipe } from '@nestjs/common';
import { AuthInfo } from 'src/commons/common.type';
import { ReqLoginDto, ReqSignUpDto, ReqUpdateDto } from 'src/commons/dto/users.dto';
import { JwtAuthGuard } from 'src/commons/guard/auth/jwt.strategy';
import { UsersService } from '../services/users.service';

@Controller('/api')
@UsePipes(ValidationPipe)
export class UsersController {
    constructor(private readonly usersService: UsersService) {}

    @Get('user')
    @UseGuards(JwtAuthGuard)
    async checkCurrentUser(@Headers('Authorization') auth: string, @Body() body: any) {
        const auth_info: AuthInfo = {
            token: auth.split(' ')[1],
            user_uid: body.user_uid,
        };
        return this.usersService.checkCurrentUser(auth_info);
    }

    @Post('users')
    async signUp(@Body('user') user: ReqSignUpDto) {
        return this.usersService.signUp(user);
    }

    @Put('user')
    @UseGuards(JwtAuthGuard)
    async modifiedInfo(
        @Headers('Authorization') auth: string,
        @Body('user') user: ReqUpdateDto,
        @Body('user_uid') uid: number,
    ) {
        const auth_info: AuthInfo = {
            token: auth.split(' ')[1],
            user_uid: uid,
        };
        return this.usersService.modifiedInfo(user, auth_info);
    }

    @Post('users/login')
    async login(@Body('user') user: ReqLoginDto) {
        return this.usersService.login(user);
    }
}
