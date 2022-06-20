'use strict';

const express = require('express');
const router = express.Router();

router.get('/');

router.post('/');

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