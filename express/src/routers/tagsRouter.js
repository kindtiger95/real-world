const express = require('express');
const tagsController = require('../controllers/tagsController');

const router = express.Router();

// get all tags
router.get('/', tagsController.getAllTagList);

module.exports = router;