const express = require('express');
const { validate } = require('../middlewares/validator');
const { body } = require('express-validator');
const usersController = require('../controllers/usersController');

const validateSignUp = [
    body('user').notEmpty().withMessage('user field be required.'),
    validate,
];

const router = express.Router();

// check current user
router.get('/');

// signup
router.post('/', validateSignUp, usersController.signUp);

// update user info
router.put('/');

// login
router.post('/login');

module.exports = router;
