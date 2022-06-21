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

async function createResponseArticle(article, user_uid) {
    const favorite_info = await getFavoriteInfo(article.dataValues.uid, user_uid);
    const tag_list = await models.Tags.findAllByArticleId(article.dataValues.uid);
    const is_following = await models.Follows.findFollowing(user_uid, article.User.dataValues.uid);

    return {
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
                username: article.User.dataValues.username,
                bio: article.User.dataValues.bio,
                image: article.User.dataValues.image,
                following: is_following ? true : false,
            },
        },
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
        let tag_list = await models.Tags.findAllByArticleId(article.uid);
        if (tag) {
            const ret = tag_list.some((elem_tag) => elem_tag === tag);
            if (!ret) continue;
        }

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
        const response_elem = await createResponseArticle(article, user_uid);
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
    const response = await createResponseArticle(article, user_uid);
    return res.status(200).json({
        ...response,
    });
};

module.exports.updateArticleBySlug = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;

    const article = await models.Articles.findBySlug(slug, models.Users);

    if (article.dataValues.author_id !== user_uid) {
        return res.status(401).json({
            errors: {
                body: '401 error.',
            },
        });
    }

    await models.Articles.updatedBySlug(slug, req.body.article);
    const result_article = await models.Articles.findBySlug(slug, models.Users);

    const response = await createResponseArticle(result_article, user_uid);
    return res.status(200).json({
        ...response,
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

module.exports.getComments = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;
    const comments = await models.Comments.findAllByArticle(models.Articles, slug);
    const response = {
        comments: [],
    };

    for (const comment of comments) {
        const author = await models.Users.findByPk(comment.Article.dataValues.author_id);
        let following = false;
        if (user_uid) following = (await models.Follows.findFollowing(author.uid, user_uid)) ? true : false;

        const response_elem = {
            id: comment.dataValues.uid,
            createdAt: comment.dataValues.createdAt,
            updatedAt: comment.dataValues.updatedAt,
            body: comment.dataValues.body,
            author: {
                username: author.dataValues.username,
                bio: author.dataValues.bio,
                image: author.dataValues.image,
                following,
            },
        };
        response.comments.push(response_elem);
    }

    return res.status(200).json({
        ...response,
    });
};

module.exports.deleteComment = async (req, res) => {
    const id = req.params.id;
    const user_uid = req.user_uid;
    const comment = await models.Comments.findByPk(id);
    if (comment.dataValues.author_id !== user_uid)
        return res.status(401).json({
            errors: {
                body: '401 error.',
            },
        });
    await models.Comments.destroy({
        where: {
            uid: comment.dataValues.uid,
        },
    });
    return res.status(200).send();
};

module.exports.favorite = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;
    const article = await models.Articles.findBySlug(slug, models.Users);
    if (!article)
        return res.status(400).json({
            errors: {
                body: '400 error.',
            },
        });
    await models.Favorites.create({
        article_id: article.dataValues.uid,
        user_id: user_uid,
    });

    const response = await createResponseArticle(article, user_uid);
    return res.status(200).json({
        ...response,
    });
};

module.exports.unFavorite = async (req, res) => {
    const slug = req.params.slug;
    const user_uid = req.user_uid;
    const article = await models.Articles.findBySlug(slug, models.Users);
    if (!article)
        return res.status(400).json({
            errors: {
                body: '400 error.',
            },
        });
    await models.Favorites.destroy({
        where: {
            article_id: article.dataValues.uid,
            user_id: user_uid,
        },
    });

    const response = await createResponseArticle(article, user_uid);
    return res.status(200).json({
        ...response,
    });
};
