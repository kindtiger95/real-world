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
        },
    );

    Tags.associate = (models) => {
        Tags.belongsTo(models.Articles, {
            foreignKey: 'article_id',
        });
    };

    Tags.findAllByArticleId = async (article_id) => {
        let tag_list = await Tags.findAll({
            where: {
                article_id,
            },
        });
        tag_list = tag_list.map((tag) => {
            return tag.dataValues.tag;
        });
        return tag_list;
    };

    Tags.findAllSanitization = async () => {
        let tag_list = await Tags.findAll();
        tag_list = tag_list.map((tag) => {
            return tag.dataValues.tag;
        });
        return tag_list;
    };

    return Tags;
};
