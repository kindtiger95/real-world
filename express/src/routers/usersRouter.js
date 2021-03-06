'use strict';

const express = require('express');
const { validate } = require('../middlewares/validator');
const { body } = require('express-validator');
const usersController = require('../controllers/usersController');

const validateSignUp = [
    body('user.username').notEmpty().withMessage('user name be required.'),
    body('user.email').notEmpty().withMessage('user email be required.'),
    body('user.password').notEmpty().withMessage('user password be required.'),
    validate,
];

const validateLogin = [
    body('user.email').notEmpty().withMessage('user email be required.'),
    body('user.password').notEmpty().withMessage('user password be required.'),
    validate,
];

const router = express.Router();

// signup
router.post('/', validateSignUp, usersController.signUp);

// login
router.post('/login', validateLogin, usersController.login);

module.exports = router;
