import { Body, Controller, Delete, Get, Param, Post, Put, UsePipes, ValidationPipe } from '@nestjs/common';
import { UsersDto } from '../dto/users.dto';
import { ArticlesService } from '../services/articles.service';

@Controller('/api/articles')
@UsePipes(ValidationPipe)
export class ArticlesController {
    constructor(private readonly articlesService: ArticlesService) {}

    @Get()
    async getAllAriticles() {
        return await this.articlesService.getAllAriticles();
    }

    @Post()
    async createArticle(@Body('user') users_dto: UsersDto) {
        return await this.articlesService.createArticle(users_dto);
    }

    @Get('feed')
    async recentArticles() {
        return await this.articlesService.recentArticles();
    }

    @Get(':slug')
    async getArticleBySlug(@Param('slug') slug: string) {
        return await this.articlesService.getArticleBySlug(slug);
    }

    @Put(':slug')
    async modifiedArticleBySlug(@Param('slug') slug: string) {
        return await this.articlesService.modifiedArticleBySlug(slug);
    }

    @Delete(':slug')
    async deleteArticleBySlug(@Param('slug') slug: string) {
        return await this.articlesService.deleteArticleBySlug(slug);
    }

    @Post(':slug/comments')
    async createComment(@Param('slug') slug: string) {
        return await this.articlesService.createComment(slug);
    }

    @Get(':slug/comments')
    async getComments(@Param('slug') slug: string) {
        return await this.articlesService.getComments(slug);
    }

    @Delete(':slug/comments/:id')
    async deleteCommentsById(@Param('slug') slug: string, @Param('id') id: number) {
        return await this.articlesService.deleteCommentsById(slug, id);
    }

    @Post(':slug/favorite')
    async postFavorite(@Param('slug') slug: string) {
        return await this.articlesService.postFavorite(slug);
    }

    @Delete(':slug/favorite')
    async deleteFavorite(@Param('slug') slug: string) {
        return await this.articlesService.deleteFavorite(slug);
    }
}
