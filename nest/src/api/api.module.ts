import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { MysqlModule } from 'src/databases/mysql.module';
import { ArticlesController } from './controllers/articles.controller';
import { ProfilesController } from './controllers/profiles.controller';
import { TagsController } from './controllers/tags.controller';
import { UsersController } from './controllers/users.controller';
import { ArticlesService } from './services/articles.service';
import { ProfilesService } from './services/profiles.service';
import { TagsService } from './services/tags.service';
import { UsersService } from './services/users.service';

@Module({
    imports: [
        MysqlModule,
        JwtModule.register({
            secret: 'test',
        }),
    ],
    providers: [ArticlesService, ProfilesService, TagsService, UsersService],
    controllers: [ArticlesController, ProfilesController, TagsController, UsersController],
})
export class ApiModule {}
