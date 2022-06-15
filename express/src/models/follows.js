'use strict';

module.exports = (sequelize, DataTypes) => {
    const follows = sequelize.define(
        'follows',
        {
            uid: {
                type: DataTypes.INTEGER,
                allowNull: false,
                autoIncrement: true,
                primaryKey: true,
            },
            followee: {
                type: DataTypes.INTEGER,
                allowNull: false,
            },
            follower: {
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

    return follows;
};
