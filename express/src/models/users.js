'use strict';

module.exports = (sequelize, DataTypes) => {
    const users = sequelize.define(
        'users',
        {
            uid: {
                type: DataTypes.INTEGER,
                allowNull: false,
                autoIncrement: true,
                primaryKey: true,
            },
            email: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            password: {
                type: DataTypes.STRING,
                allowNull: false,
            },
            bio: {
                type: DataTypes.STRING,
                allowNull: true,
            },
            image: {
                type: DataTypes.STRING,
                allowNull: true,
            },
        },
        {
            timestamps: true,
            charset: 'utf8',
            freezeTableName: true,
        },
    );

    users.associate = (models) => {
        users.hasMany(models.follows, {
            foreignKey: 'followee',
            sourceKey: 'uid',
        });
        users.hasMany(models.follows, {
            foreignKey: 'follower',
            sourceKey: 'uid',
        });
    };
    return users;
};
