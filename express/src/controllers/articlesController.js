'use strict';

const models = require('../models');

async function getFavoriteInfo(article_id, user_id) {
    let is_favorited = false;
    const find_favorited_by_article_id = await models.Favorites.findAll({
        where: {
            article_id,
        },
    });
    for (const favorite of find_favorited_by_article_id) {
        if (favorite.dataValues.user_id === user_id) {
            is_favorited = true;
            break;
        }
    }

    return {
        is_favorited,
        count: find_favorited_by_article_id.length,
    };
}

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
    if (!article_list)
        return res.status(400).json({
            errors: {
                body: 'cant find articles.',
            },
        });

    const response = {
        articles: [],
    };

    for (const article of article_list) {
        let isExistTags = false;

        let tag_list = await models.Tags.findAllByArticleId(article.uid);
        tag_list = tag_list.map((tag_elem) => {
            if (tag && tag_elem.dataValues.tag === tag) isExistTags = true;
            return tag_elem.dataValues.tag;
        });
        if (tag && !isExistTags) continue;

        const favorited_user_id = find_favorited_user ? find_favorited_user.uid : '';
        const favorite_info = await getFavoriteInfo(article.uid, favorited_user_id);
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
            favorited: favorite_info.is_favorited,
            favoritesCount: favorite_info.count,
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

module.exports.feedArticle = async (req, res) => {
    const limit = req.query.limit ? req.query.limit : 20;
    const offset = req.query.offset ? req.query.offset : 0;
    const user_uid = req.user_uid;
    const feed_articles = await models.Articles.findAllArticles(models.Users, limit, offset);

    const response = {
        articles: [],
    };

    for (const article of feed_articles) {
        const tag_list = await models.Tags.findAllByArticleId(article.uid);

        const favorite_info = await getFavoriteInfo(article.uid, user_uid);
        const is_following = await models.Follows.findFollowing(user_uid, article.dataValues.User.uid);

        const response_elem = {
            slug: article.dataValues.slug,
            title: article.dataValues.title,
            description: article.dataValues.description,
            body: article.dataValues.body,
            tagList: tag_list,
            createdAt: article.dataValues.createdAt,
            updatedAt: article.dataValues.updatedAt,
            favorited: favorite_info.is_favorited,
            favoritesCount: favorite_info.count,
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

module.exports.getArticleBySlug = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;

    const article = await models.Articles.findBySlug(slug);
    const tag_list = await models.Tags.findAllByArticleId(article.dataValues.uid);

    const favorite_info = await getFavoriteInfo(article.dataValues.uid, user_uid);
    const is_following = await models.Follows.findFollowing(user_uid, article.dataValues.User.uid);

    return res.status(200).json({
        article: {
            slug: article.dataValues.slug,
            title: article.dataValues.title,
            description: article.dataValues.description,
            body: article.dataValues.body,
            tagList: tag_list,
            createdAt: article.dataValues.createdAt,
            updatedAt: article.dataValues.updatedAt,
            favorited: favorite_info.is_favorited,
            favoritesCount: favorite_info.count,
            author: {
                username: article.dataValues.User.username,
                bio: article.dataValues.User.bio,
                image: article.dataValues.User.image,
                following: is_following ? true : false,
            },
        },
    });
};

module.exports.updateArticleBySlug = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;

    const article = await models.Articles.findBySlug(slug, models.Users);
    const tag_list = await models.Tags.findAllByArticleId(article.dataValues.uid);

    if (article.dataValues.author_id !== user_uid) {
        return res.status(401).json({
            errors: {
                body: '401 error.',
            },
        });
    }

    await models.Articles.updatedBySlug(slug, req.body.article);
    const favorite_info = await getFavoriteInfo(article.dataValues.uid, user_uid);
    const is_following = await models.Follows.findFollowing(user_uid, article.dataValues.User.uid);
    const result_article = await models.Articles.findBySlug(slug, models.Users);
    return res.status(200).json({
        article: {
            slug: result_article.dataValues.slug,
            title: result_article.dataValues.title,
            description: result_article.dataValues.description,
            body: result_article.dataValues.body,
            tagList: tag_list,
            createdAt: result_article.dataValues.createdAt,
            updatedAt: result_article.dataValues.updatedAt,
            favorited: favorite_info.is_favorited,
            favoritesCount: favorite_info.count,
            author: {
                username: result_article.dataValues.User.username,
                bio: result_article.dataValues.User.bio,
                image: result_article.dataValues.User.image,
                following: is_following ? true : false,
            },
        },
    });
};

module.exports.deleteArticleBySlug = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;

    const article = await models.Articles.findBySlug(slug, models.Users);
    if (!article)
        return res.status(400).json({
            errors: {
                body: 'not exist article.',
            },
        });
    if (article.dataValues.author_id !== user_uid) {
        return res.status(401).json({
            errors: {
                body: '401 error.',
            },
        });
    }

    await models.Articles.destroy({
        where: {
            slug,
        },
    });
    await models.Tags.destroy({
        where: {
            article_id: article.dataValues.uid,
        },
    });
    await models.Favorites.destroy({
        where: {
            article_id: article.dataValues.uid,
        },
    });
    await models.Comments.destroy({
        where: {
            article_id: article.dataValues.uid,
        },
    });

    return res.status(200).send();
};

module.exports.addComments = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;
    const article = await models.Articles.findBySlug(slug, models.Users);
    if (!article)
        return res.status(400).json({
            errors: {
                body: 'not exist article.',
            },
        });
    const comment = await models.Comments.create({
        author_id: user_uid,
        article_id: article.dataValues.uid,
        body: req.body.comment.body,
    });
    const user = await models.Users.findByPk(user_uid);
    const following = await models.Follows.findFollowing(user_uid, user_uid);
    return res.status(200).json({
        comment: {
            id: comment.uid,
            createdAt: comment.createdAt,
            updatedAt: comment.updatedAt,
            body: comment.body,
            author: {
                username: user.dataValues.username,
                bio: user.dataValues.bio,
                image: user.dataValues.image,
                following: following ? true : false,
            },
        },
    });
};
