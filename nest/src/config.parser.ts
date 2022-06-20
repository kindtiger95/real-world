import { IsNumber, IsString } from 'class-validator';

export class EnvironmentVariables {
    @IsString()
    JWT_SECRET: string;

    @IsNumber()
    JWT_EXPIRES_SEC: number;

    @IsNumber()
    BCRYPT_SALT_ROUNDS: number;

    @IsNumber()
    HOST_PORT: number;

    @IsString()
    DB_HOST: string;

    @IsString()
    DB_DATABASE: string;

    @IsString()
    DB_USER: string;

    @IsString()
    DB_PASSWORD: string;

    @IsNumber()
    DB_PORT: number;
}
