'use strict';

const express = require('express');
const router = express.Router();
const auth = require('../middlewares/auth');
const articlesController = require('../controllers/articlesController');

router.get('/', auth.optionalAuth, articlesController.getArticles);

router.post('/', auth.requireAuth, articlesController.createArticle);

router.get('/feed', auth.requireAuth, articlesController.feedArticle);

router.get('/:slug', auth.optionalAuth, articlesController.getArticleBySlug);

router.put('/:slug', auth.requireAuth, articlesController.updateArticleBySlug);

router.delete('/:slug', auth.requireAuth, articlesController.deleteArticleBySlug);

router.post('/:slug/comments', auth.requireAuth, articlesController.addComments);

router.get('/:slug/comments', auth.optionalAuth, articlesController.getComments);

router.delete('/:slug/comments/:id', auth.requireAuth, articlesController.deleteComment);

router.post('/:slug/favorite', auth.requireAuth, articlesController.favorite);

router.delete('/:slug/favorite', auth.requireAuth, articlesController.unFavorite);

module.exports = router;
