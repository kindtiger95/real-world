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
    let following = false;
    if (req.user_uid) {
        const follow_search = await models.Follows.findOne({
            where: {
                followee: user.dataValues.uid,
                follower: req.user_uid,
            },
        });
        if (follow_search) following = true;
    }

    return res.status(200).json({
        profile: {
            username: user.dataValues.username,
            bio: user.dataValues.bio,
            image: user.dataValues.image,
            following,
        },
    });
};

module.exports.following = async (req, res) => {
    const username = req.params.username;
    const find_followee = await models.Users.findOne({
        where: {
            username,
        },
    });
    if (!find_followee.dataValues.uid)
        return res.status(400).json({
            errors: {
                body: 'cant find followee.',
            },
        });

    await models.Follows.findOrCreate({
        where: {
            followee: find_followee.dataValues.uid,
            follower: req.user_uid,
        },
    });
    return res.status(200).json({
        profile: {
            username: find_followee.dataValues.username,
            bio: find_followee.dataValues.bio,
            image: find_followee.dataValues.image,
            following: true,
        },
    });
};

module.exports.unfollowing = async (req, res) => {
    const username = req.params.username;
    const find_followee = await models.Users.findOne({
        where: {
            username,
        },
    });

    if (!find_followee.dataValues.uid)
        return res.status(400).json({
            errors: {
                body: 'cant find followee.',
            },
        });

    const find_following_data = await models.Follows.findOne({
        where: {
            followee: find_followee.dataValues.uid,
            follower: req.user_uid,
        },
    });
    if (find_following_data) {
        await models.Follows.destroy({
            where: {
                followee: find_followee.dataValues.uid,
                follower: req.user_uid,
            },
        });
    }
    return res.status(200).json({
        profile: {
            username: find_followee.dataValues.username,
            bio: find_followee.dataValues.bio,
            image: find_followee.dataValues.image,
            following: false,
        },
    });
};
