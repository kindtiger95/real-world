'use strict';

module.exports = (sequelize, DataTypes) => {
    const Favorites = sequelize.define(
        'Favorites',
        {
            uid: {
                type: DataTypes.INTEGER,
                allowNull: false,
                autoIncrement: true,
                primaryKey: true,
            },
            article_id: {
                type: DataTypes.INTEGER,
                allowNull: false,
            },
            user_id: {
                type: DataTypes.INTEGER,
                allowNull: false,
            },
        },
        {
            timestamps: true,
            charset: 'utf8',
            freezeTableName: true,
        },
    );
    Favorites.associate = (models) => {
        Favorites.belongsTo(models.Articles, {
            foreignKey: 'article_id',
        });
        Favorites.belongsTo(models.Users, {
            foreignKey: 'user_id',
        });
    };
    return Favorites;
};
