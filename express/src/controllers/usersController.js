const models = require('../models');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const config = require('../config.loader');

function createJwtToken(user_uid) {
    return jwt.sign({ user_uid }, config.jwt.secretKey, {
        expiresIn: config.jwt.expiresInSec,
    });
}

async function login(req, res) {
    const { email, password } = req.body.user;
    const user = await models.Users.findOne({
        where: {
            email,
        },
    });
    if (!user) return res.status(401).json({ message: 'Invalid user or password.' });

    const is_valid_pw = await bcrypt.compare(password, user.dataValues.password);
    if (!is_valid_pw) return res.status(401).json({ message: 'Invalid user or password.' });

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
}

async function signUp(req, res) {
    const { email, password, username } = req.body.user;
    const find_ret = await models.Users.findOne({
        where: {
            username,
        },
    });

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
}

module.exports.login = login;
module.exports.signUp = signUp;
