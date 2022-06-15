'use strict';

const express = require('express');
const config = require('./config.loader');
const models = require('./models');
const usersRouter = require('./routers/usersRouter');
const articlesRouter = require('./routers/articlesRouter');
const profilesRouter = require('./routers/profilesRouter');
const tagsRouter = require('./routers/tagsRouter');

const app = express();

app.use(express.json());

app.use('/api/users', usersRouter);
app.use('/api/articles', articlesRouter);
app.use('/api/profiles', profilesRouter);
app.use('/api/tags', tagsRouter);

app.use((req, res, next) => {
    res.sendStatus(404);
});

app.use((error, req, res, next) => {
    console.error(error);
    res.sendStatus(500);
});

models.sequelize.sync().then(() => {
    app.listen(config.host.port);
});
