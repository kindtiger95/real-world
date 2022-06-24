import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { MysqlModule } from 'src/databases/mysql.module';
import { JwtAuthGuard, JwtStrategy } from './jwt.strategy';

@Module({
    imports: [
        MysqlModule,
        PassportModule,
        JwtModule.register({
            secret: 'test',
        }),
    ],
    providers: [JwtStrategy, JwtAuthGuard],
    exports: [JwtStrategy, JwtAuthGuard],
})
export class AuthModule {}
