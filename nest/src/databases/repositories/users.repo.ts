import { Injectable } from '@nestjs/common';
import { InjectEntityManager } from '@nestjs/typeorm';
import { SignUpDto } from 'src/api/dto/users.dto';
import { DataSource } from 'typeorm';
import { UsersEntity } from '../entities/users.entity';

@Injectable()
export class UsersRepo {
    private readonly _alias = 'users';

    constructor(@InjectEntityManager() private readonly dataSource: DataSource) {}

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

    async createNewUser(signup_dto: SignUpDto, hashed_password: string) {
        // return await this.dataSource
        // .getRepository(UsersEntity)
        // .createQueryBuilder(this._alias)
        // .insert()
        // .into()
        return null;
    }
}
