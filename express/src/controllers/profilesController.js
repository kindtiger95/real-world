'use strict';

const models = require('../models');

module.exports.getProfileByUserUid = async (req, res) => {
    const username = req.params.username;
    const user = await models.Users.findOne({
        where: {
            username,
        },
    });
    if (!user) {
        res.status(400).json({
            message: `Cant found user (username: ${username})`,
        });
    }
    return res.status(200).json({
        profile: {
            username: user.dataValues.username,
            bio: user.dataValues.bio,
            image: user.dataValues.image,
        }
    })
};
