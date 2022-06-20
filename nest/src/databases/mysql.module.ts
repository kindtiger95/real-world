import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UsersEntity } from './entities/users.entity';
import { UsersRepo } from './repositories/users.repo';

@Module({
    imports: [TypeOrmModule.forFeature([UsersEntity])],
    providers: [UsersRepo],
    exports: [UsersRepo],
})
export class MysqlModule {}
