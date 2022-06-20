const jwt = require('jsonwebtoken');
const config = require('../config.loader');
const models = require('../models');

const AUTH_ERROR = 'Authentication Error';

function getTokenString(req) {
    const authHeader = req.get('Authorization');
    if (!(authHeader && authHeader.startsWith('Token '))) return null;
    const token = authHeader.split(' ')[1];
    return token;
}

async function verifyToken(token, req, res, next) {
    jwt.verify(token, config.jwt.secretKey, async (error, decoded) => {
        if (error) {
            return res.status(401).json({
                errors: {
                    body: AUTH_ERROR,
                },
            });
        }
        const user = await models.Users.findOne({
            where: decoded.user_uid,
        });
        if (!user) {
            return res.status(401).json({
                errors: {
                    body: AUTH_ERROR,
                },
            });
        }
        req.user_uid = user.dataValues.uid; // req.customData
        req.token = token;
        next();
    });
}

module.exports.requireAuth = async (req, res, next) => {
    const token = getTokenString(req);
    if (!token)
        return res.status(401).json({
            errors: {
                body: AUTH_ERROR,
            },
        });

    await verifyToken(token, req, res, next);
};

module.exports.optionalAuth = async (req, res, next) => {
    const token = getTokenString(req);
    if (!token) {
        next();
        return;
    }

    await verifyToken(token, req, res, next);
};
