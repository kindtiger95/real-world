import { OmitType, PickType } from '@nestjs/swagger';
import { IsEmail, IsNotEmpty, IsOptional, IsString } from 'class-validator';
import { UsersScheme } from 'src/commons/common.type';

export class UsersDto implements UsersScheme {
    @IsNotEmpty()
    @IsEmail()
    email: string;

    @IsNotEmpty()
    @IsString()
    token: string;

    @IsNotEmpty()
    @IsString()
    username: string;

    @IsNotEmpty()
    @IsString()
    password: string;

    @IsNotEmpty()
    @IsString()
    bio: string;

    @IsNotEmpty()
    @IsString()
    image: string;
}

export class ReqSignUpDto extends PickType(UsersDto, ['email', 'username']) {
    @IsNotEmpty()
    @IsString()
    password: string;
}

export class ReqLoginDto extends PickType(UsersDto, ['email', 'password']) {}

export class ReqUpdateDto extends OmitType(UsersDto, ['token']) {
    @IsOptional()
    @IsEmail()
    email: string;

    @IsOptional()
    username: string;

    @IsOptional()
    password: string;

    @IsOptional()
    image: string;

    @IsOptional()
    bio: string;
}
