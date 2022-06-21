'use strict';

const models = require('../models');

module.exports.getProfileByUserUid = async (req, res) => {
    const username = req.params.username;
    const user = await models.Users.findByUsername(username);

    if (!user) {
        res.status(400).json({
            message: `Cant found user (username: ${username})`,
        });
    }
    let following = false;
    if (req.user_uid) {
        const follow_search = await models.Follows.findOne({
            where: {
                followee: user.uid,
                follower: req.user_uid,
            },
        });
        if (follow_search) following = true;
    }

    return res.status(200).json({
        profile: {
            username: user.username,
            bio: user.bio,
            image: user.image,
            following,
        },
    });
};

module.exports.following = async (req, res) => {
    const username = req.params.username;
    const find_followee = await models.Users.findByUsername(username);

    if (!find_followee)
        return res.status(400).json({
            errors: {
                body: 'cant find followee.',
            },
        });

    await models.Follows.findOrCreate({
        where: {
            followee: find_followee.uid,
            follower: req.user_uid,
        },
    });
    return res.status(200).json({
        profile: {
            username: find_followee.username,
            bio: find_followee.bio,
            image: find_followee.image,
            following: true,
        },
    });
};

module.exports.unfollowing = async (req, res) => {
    const username = req.params.username;
    const find_followee = await models.Users.findByUsername(username);

    if (!find_followee.uid)
        return res.status(400).json({
            errors: {
                body: 'cant find followee.',
            },
        });

    const find_following_data = await models.Follows.findOne({
        where: {
            followee: find_followee.uid,
            follower: req.user_uid,
        },
    });
    if (find_following_data) {
        await models.Follows.destroy({
            where: {
                followee: find_followee.uid,
                follower: req.user_uid,
            },
        });
    }
    return res.status(200).json({
        profile: {
            username: find_followee.username,
            bio: find_followee.bio,
            image: find_followee.image,
            following: false,
        },
    });
};
