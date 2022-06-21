'use strict';

const models = require('../models');

module.exports.getArticles = async (req, res) => {
    const limit = req.query.limit ? req.query.limit : 20;
    const author = req.query.author;
    const tag = req.query.tag;
    const favorited = req.query.favorited;
    const offset = req.query.offset ? req.query.offset : 0;
    const user_uid = req.user_uid;

    let find_favorited_user;
    if (favorited) {
        find_favorited_user = await models.Users.findByUsername(favorited);
        if (!find_favorited_user) {
            res.status(400).json({
                errors: {
                    body: 'cant find favorited user',
                },
            });
        }
    }

    const article_list = await models.Articles.findAllArticles(models.Users, limit, offset, author);
    if (!article_list) return res.status(400).json({
        errors: {
            body: 'cant find articles.'
        }
    });
    
    const response = {
        articles: [],
    };
    for (const article of article_list) {
        let tag_list = await models.Tags.findAll({
            where: {
                article_id: article.uid,
            },
        });
        let isExistTags = false;
        tag_list = tag_list.map((tag_elem) => {
            if (tag && tag_elem.dataValues.tag === tag) isExistTags = true;
            return tag_elem.dataValues.tag;
        });
        if (tag && !isExistTags) continue;

        let search_favorited = false;
        const find_favorited_by_article_id = await models.Favorites.findAll({
            where: {
                article_id: article.uid,
            },
        });
        if (favorited) {
            for (const favorite of find_favorited_by_article_id) {
                if (favorite.dataValues.user_id === find_favorited_user.uid) {
                    search_favorited = true;
                    break;
                }
            }
        }
        const is_following = user_uid
            ? await models.Follows.findFollowing(user_uid, article.dataValues.User.uid)
            : false;

        const response_elem = {
            slug: article.dataValues.slug,
            title: article.dataValues.title,
            description: article.dataValues.description,
            body: article.dataValues.body,
            tagList: tag_list,
            createdAt: article.dataValues.createdAt,
            updatedAt: article.dataValues.updatedAt,
            favorited: search_favorited,
            favoritesCount: find_favorited_by_article_id.length,
            author: {
                username: article.dataValues.User.username,
                bio: article.dataValues.User.bio,
                image: article.dataValues.User.image,
                following: is_following ? true : false,
            },
        };
        response.articles.push(response_elem);
    }

    response.articlesCount = response.articles.length;

    return res.status(200).json({
        ...response,
    });
};

module.exports.createArticle = async (req, res) => {
    const { title, description, body, tagList } = req.body.article;
    const author = await models.Users.findByPk(req.user_uid);
    if (!author) {
        return res.status(400).json({
            errors: {
                body: 'cant find author.',
            },
        });
    }

    const { username, bio, image } = author.dataValues;
    const slug = title.replace(/ /g, '-');
    const new_article = await models.Articles.create({
        author_id: req.user_uid,
        slug,
        title,
        description,
        body,
    });
    const tags_object = [];
    tagList.forEach((value) => {
        const tag_object = {
            article_id: new_article.dataValues.uid,
            tag: value,
        };
        tags_object.push(tag_object);
    });
    await models.Tags.bulkCreate(tags_object);

    return res.status(200).json({
        article: {
            slug,
            title,
            description,
            body,
            tagList,
            createdAt: new_article.dataValues.createdAt,
            updatedAt: new_article.dataValues.updatedAt,
            favorited: false,
            favoritesCount: 0,
            author: {
                username,
                bio,
                image,
                following: false,
            },
        },
    });
};
