'use strict';

const express = require('express');
const router = express.Router();
const auth = require('../middlewares/auth');
const articlesController = require('../controllers/articlesController');

router.get('/', auth.optionalAuth, articlesController.getArticles);

router.post('/', auth.requireAuth, articlesController.createArticle);

router.get('/feed');

router.get('/:slug');

router.put('/:slug');

router.delete('/:slug');

router.post('/:slug/comments');

router.get('/:slug/comments');

router.delete('/:slug/comments/:id');

router.post('/:slug/favorite');

router.delete('/:slug/favorite');

module.exports = router;
