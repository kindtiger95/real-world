'use strict';

const models = require('../models');

module.exports.getArticles = async (req, res) => {
    const limit = req.query.limit ? req.query.limit : 20;
    const author = req.query.author;
    const tag = req.query.tag;
    const favorited = req.query.favorited;
    const offset = req.query.offset;
};

module.exports.createArticle = async (req, res) => {
    const { title, description, body, tagList } = req.body.article;
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
            tag: value,
        };
        tags_object.push(tag_object);
    });
    await models.Tags.bulkCreate(tags_object);

    
};
