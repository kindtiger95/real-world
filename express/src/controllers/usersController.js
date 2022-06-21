'use strict';

const models = require('../models');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const config = require('../config.loader');

const AUTH_ERROR = 'Authentication Error';

function createJwtToken(user_uid) {
    return jwt.sign({ user_uid }, config.jwt.secretKey, {
        expiresIn: config.jwt.expiresInSec,
    });
}

module.exports.checkCurrentUser = async (req, res) => {
    const user = await models.Users.findByPk(req.user_uid);
    if (!user)
        return res.status(400).json({
            message: 'uid is not exists.',
        });
    return res.status(200).json({
        user: {
            email: user.dataValues.email,
            token: req.token,
            username: user.dataValues.username,
            bio: user.dataValues.bio,
            image: user.dataValues.image,
        },
    });
};

module.exports.updateUserInfo = async (req, res) => {
    const { email, bio, image } = req.body.user;
    const updated_ret = await models.Users.update(
        {
            email,
            bio,
            image,
        },
        {
            where: {
                uid: req.user_uid,
            },
        }
    );
    if (!updated_ret)
        return res.status(400).json({
            message: 'uid is not exists.',
        });

    const select_ret = await models.Users.findByPk(req.user_uid);
    return res.status(200).json({
        user: {
            email: select_ret.dataValues.email,
            token: req.token,
            username: select_ret.dataValues.username,
            bio: select_ret.dataValues.bio,
            image: select_ret.dataValues.image,
        },
    });
};

module.exports.signUp = async (req, res) => {
    const { email, password, username } = req.body.user;
    const find_ret = await models.Users.findByUsername(username);

    if (find_ret) {
        res.status(407).json({
            message: 'username is already exists.',
        });
    }
    const hashed_pw = await bcrypt.hash(password, config.bcrypt.saltRounds);
    const insert_ret = await models.Users.create({
        username,
        email,
        password: hashed_pw,
    });
    const created_token = createJwtToken(insert_ret.dataValues.uid);
    res.status(200).json({
        user: {
            email: insert_ret.dataValues.email,
            token: created_token,
            username: insert_ret.dataValues.username,
            bio: '',
            image: null,
        },
    });
};

module.exports.login = async (req, res) => {
    const { email, password } = req.body.user;
    const user = await models.Users.findOne({
        where: {
            email,
        },
    });
    if (!user) return res.status(401).json({ message: AUTH_ERROR });

    const is_valid_pw = await bcrypt.compare(password, user.dataValues.password);
    if (!is_valid_pw) return res.status(401).json({ message: AUTH_ERROR });

    const token = createJwtToken(user.dataValues.uid);
    res.status(200).json({
        user: {
            email: user.dataValues.email,
            token,
            username: user.dataValues.username,
            bio: user.dataValues.bio,
            image: user.dataValues.image,
        },
    });
};
