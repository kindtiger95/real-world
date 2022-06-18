const jwt = require('jsonwebtoken');
const config = require('../config.loader');

const AUTH_ERROR = 'Authentication Error';

const authCheck = async (req, res, next) => {
    const authHeader = req.get('Authorization');
    if (!(authHeader || authHeader.startsWith('Token '))) {
        return res.status(401).json({
            errors: {
                body: AUTH_ERROR,
            },
        });
    }

    jwt.verify(token, config.jwt.secretKey, async (error, decoded) => {
        if (error) {
            return res.status(401).json(AUTH_ERROR);
        }
        const user = await userRepository.findById(decoded.id);
        if (!user) {
            return res.status(401).json(AUTH_ERROR);
        }
        req.userId = user.id; // req.customData
        next();
    });
};