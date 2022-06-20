'use strict';

module.exports = (sequelize, DataTypes) => {
    const Comments = sequelize.define(
        'Comments',
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
            article_id: {
                type: DataTypes.INTEGER,
                allowNull: false,
            },
            body: {
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
    return Comments;
};