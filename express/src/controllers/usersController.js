const models = require('../models');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

async function signUp(req, res) {
    const { email, password, username } = req.body.user;
    console.log(email, password, username);
    res.status(200).json({
        message: 'hello',
    });
}

module.exports.signUp = signUp;
