import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { ReqSignUpDto, ReqUpdateDto } from 'src/commons/dto/users.dto';
import { DataSource } from 'typeorm';
import { UsersEntity } from '../entities/users.entity';

@Injectable()
export class UsersRepo {
    private readonly _alias = 'users';

    constructor(@InjectDataSource() private readonly _dataSource: DataSource) {}

    async findByUsername(username: string): Promise<UsersEntity | null> {
        try {
            return await this._dataSource
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

    async createNewUser(user: ReqSignUpDto, hashed: string) {
        const { email, username } = user;
        try {
            return await this._dataSource
                .createQueryBuilder()
                .insert()
                .into(UsersEntity)
                .values({
                    email,
                    username,
                    password: hashed,
                })
                .execute();
        } catch (error) {
            console.log({
                status: '에러',
            });
            return null;
        }
    }

    async findUsersByPk(uid: number) {
        try {
            return await this._dataSource
                .getRepository(UsersEntity)
                .createQueryBuilder(this._alias)
                .select()
                .where(`${this._alias}.uid = :uid`, {
                    uid,
                })
                .getOne();
        } catch (error) {
            console.log({
                status: '에러',
            });
            return null;
        }
    }

    async findUsersByEmail(email: string) {
        try {
            return await this._dataSource
                .getRepository(UsersEntity)
                .createQueryBuilder(this._alias)
                .select()
                .where(`${this._alias}.email = :email`, {
                    email,
                })
                .getOne();
        } catch (error) {
            console.log({
                status: '에러',
            });
            return null;
        }
    }

    async updateByUserId(uid: number, user: ReqUpdateDto) {
        const updated_data = {};
        user.username ? (updated_data['username'] = user.username) : '';
        user.email ? (updated_data['email'] = user.email) : '';
        user.password ? (updated_data['password'] = user.password) : '';
        user.image ? (updated_data['image'] = user.image) : '';
        user.bio ? (updated_data['bio'] = user.bio) : '';

        try {
            return await this._dataSource
                .getRepository(UsersEntity)
                .createQueryBuilder()
                .update()
                .set(updated_data)
                .where(`uid = :uid`, { uid })
                .execute();
        } catch (error) {
            console.log({
                status: '에러',
            });
            return null;
        }
    }
}
