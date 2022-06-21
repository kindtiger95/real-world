'use strict';

const models = require('../models');

module.exports.getAllTagList = async (req, res) => {
    const all_tags = await models.Tags.findAllSanitization();
    return res.status(200).json({
        tags: [...all_tags],
    });
};
