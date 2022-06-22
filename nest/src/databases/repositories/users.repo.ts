import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { DataSource } from 'typeorm';
import { UsersEntity } from '../entities/users.entity';

@Injectable()
export class UsersRepo {
    private readonly _alias = 'users';

    constructor(@InjectDataSource() private readonly dataSource: DataSource) {}

    async findByUsername(username: string): Promise<UsersEntity | null> {
        try {
            return await this.dataSource
                .getRepository(UsersEntity)
                .createQueryBuilder(this._alias)
                .select()
                .where(`${this._alias}.username = :username`, {
                    username,
                })
                .getOne();
        } catch (error) {
            console.log({
                status: '에러',
            });
            return null;
        }
    }
}
