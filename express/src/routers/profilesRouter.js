'use strict';

const express = require('express');
const auth = require('../middlewares/auth');
const profilesController = require('../controllers/profilesController');

const router = express.Router();

// get profile except user private information
router.get('/profiles/:username', profilesController.getProfileByUserUid);

// following the user from current user
router.post('/profiles/:username/follow');

// unfollowing the user from current user
router.delete('/profiles/:username/follow');

module.exports = router;
