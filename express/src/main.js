'use strict';

const express = require('express');
const config = require('./config.loader');
const models = require('./models');

const app = express();

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
