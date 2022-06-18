const express = require('express');
const auth = require('../middlewares/auth');
const usersController = require('../controllers/usersController');

const router = express.Router();

// check current user
router.get('/user', auth, usersController.checkCurrentUser);

// update user info
router.put('/user', auth, usersController.updateUserInfo);

module.exports = router;
