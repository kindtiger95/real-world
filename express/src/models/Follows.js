'use strict';

module.exports = (sequelize, DataTypes) => {
    const Follows = sequelize.define(
        'Follows',
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

    Follows.associate = (models) => {
        Follows.belongsTo(models.Users, {
            foreignKey: 'followee',
        });
        Follows.belongsTo(models.Users, {
            foreignKey: 'follower',
        });
    };

    Follows.findFollowing = async (followee, follower) => {
        return await Follows.findOne({
            include: [],
            where: {
                followee,
                follower,
            },
        }).dataValues;
    };
    
    return Follows;
};
