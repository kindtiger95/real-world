'use strict';

module.exports = (sequelize, DataTypes) => {
    const Articles = sequelize.define(
        'Articles',
        {
            uid: {
                type: DataTypes.INTEGER,
                allowNull: false,
                autoIncrement: true,
                primaryKey: true,
            },
            author_id: {
                type: DataTypes.INTEGER,
                allowNull: false,
            },
            slug: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            title: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            description: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            body: {
                type: DataTypes.TEXT,
                allowNull: false,
            },
        },
        {
            timestamps: true,
            charset: 'utf8',
            freezeTableName: true,
        }
    );
    Articles.associate = (models) => {
        Articles.hasMany(models.Favorites, {
            foreignKey: 'article_id',
            sourceKey: 'uid',
        });
        Articles.hasMany(models.Tags, {
            foreignKey: 'article_id',
            sourceKey: 'uid',
        });
        Articles.hasMany(models.Comments, {
            foreignKey: 'article_id',
            sourceKey: 'uid',
        });
    };
    return Articles;
};
