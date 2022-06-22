import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { configModuleOptions } from 'src/loader';
import { ArticlesEntity } from './entities/articles.entity';
import { CommentsEntity } from './entities/comments.entity';
import { FavoritesEntity } from './entities/favorites.entity';
import { FollowsEntity } from './entities/follows.entity';
import { TagsEntity } from './entities/tags.entity';
import { UsersEntity } from './entities/users.entity';
import { UsersRepo } from './repositories/users.repo';

@Module({
    imports: [
        ConfigModule.forRoot(configModuleOptions),
        TypeOrmModule.forFeature([
            ArticlesEntity,
            CommentsEntity,
            FavoritesEntity,
            FollowsEntity,
            TagsEntity,
            UsersEntity,
        ]),
    ],
    providers: [UsersRepo],
    exports: [UsersRepo],
})
export class MysqlModule {}
