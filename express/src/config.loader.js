const dotenv = require('dotenv');

dotenv.config({
    path: 'config/dev.env',
});

function importConfigField(key, defaultValue = undefined) {
    const value = process.env[key] || defaultValue;
    if (value == null) {
        throw new Error(`Key ${key} is undefined`);
    }
    return value;
}

const config = {
    jwt: {
        secretKey: importConfigField('JWT_SECRET'),
        expiresInSec: parseInt(importConfigField('JWT_EXPIRES_SEC', 86400)),
    },
    bcrypt: {
        saltRounds: parseInt(importConfigField('BCRYPT_SALT_ROUNDS', 10)),
    },
    host: {
        port: parseInt(importConfigField('HOST_PORT', 8080)),
    },
    db: {
        host: importConfigField('DB_HOST'),
        user: importConfigField('DB_USER'),
        database: importConfigField('DB_DATABASE'),
        password: importConfigField('DB_PASSWORD'),
        port: importConfigField('DB_PORT'),
    },
};

module.exports = config;
