'use strict';

module.exports = (sequelize, DataTypes) => {
    const Users = sequelize.define(
        'Users',
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
            username: {
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
        }
    );

    Users.associate = (models) => {
        Users.hasMany(models.Follows, {
            foreignKey: 'followee',
            sourceKey: 'uid',
        });
        Users.hasMany(models.Follows, {
            foreignKey: 'follower',
            sourceKey: 'uid',
        });
        Users.hasMany(models.Articles, {
            foreignKey: 'author_id',
            sourceKey: 'uid',
        });
        Users.hasMany(models.Favorites, {
            foreignKey: 'user_id',
            sourceKey: 'uid',
        });
        Users.hasMany(models.Comments, {
            foreignKey: 'author_id',
            sourceKey: 'uid',
        });
    };

    Users.findByUsername = async (username) => {
        const find_ret = await Users.findOne({
            where: {
                username,
            },
        });
        return find_ret ? find_ret.dataValues : null;
    };

    return Users;
};
