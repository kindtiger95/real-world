'use strict';

const express = require('express');
const auth = require('../middlewares/auth');
const profilesController = require('../controllers/profilesController');

const router = express.Router();

// get profile except user private information
router.get('/:username', auth.optionalAuth, profilesController.getProfileByUserUid);

// following the user from current user
router.post('/:username/follow', auth.requireAuth, profilesController.following);

// unfollowing the user from current user
router.delete('/:username/follow', auth.requireAuth, profilesController.unfollowing);

module.exports = router;
