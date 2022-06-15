'use strict';

module.exports = (sequelize, DataTypes) => {
    const Tags = sequelize.define(
        'Tags',
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
            tag: {
                type: DataTypes.STRING,
                allowNull: false,
            },
        },
        {
            timestamps: true,
            charset: 'utf8',
            freezeTableName: true,
        }
    );

    return Tags;
};
