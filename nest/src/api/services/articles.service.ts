import { Injectable } from '@nestjs/common';
import { UsersDto } from '../dto/users.dto';

@Injectable()
export class ArticlesService {
    async getAllAriticles() {
        return '';
    }

    async createArticle(users_dto: UsersDto) {
        return users_dto;
    }

    async recentArticles() {
        return '';
    }

    async getArticleBySlug(slug: string) {
        return slug;
    }

    async modifiedArticleBySlug(slug: string) {
        return slug;
    }

    async deleteArticleBySlug(slug: string) {
        return slug;
    }

    async createComment(slug: string) {
        return slug;
    }

    async getComments(slug: string) {
        return slug;
    }

    async deleteCommentsById(slug: string, id: number) {
        return {
            slug,
            id,
        };
    }

    async postFavorite(slug: string) {
        return slug;
    }

    async deleteFavorite(slug: string) {
        return slug;
    }
}
