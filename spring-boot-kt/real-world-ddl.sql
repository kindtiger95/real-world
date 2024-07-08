CREATE TABLE `tag` (
                       `id` bigint NOT NULL AUTO_INCREMENT,
                       `tag` varchar(255) DEFAULT NULL,
                       `createdAt` datetime(6) NOT NULL,
                       `updatedAt` datetime(6) NOT NULL,
                       PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `bio` varchar(255) DEFAULT NULL,
                        `email` varchar(255) NOT NULL,
                        `image` varchar(255) DEFAULT NULL,
                        `password` varchar(255) NOT NULL,
                        `username` varchar(255) NOT NULL,
                        `createdAt` datetime(6) NOT NULL,
                        `updatedAt` datetime(6) NOT NULL,
                        PRIMARY KEY (`id`)
);

CREATE TABLE `article` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `body` varchar(255) DEFAULT NULL,
                           `description` varchar(255) DEFAULT NULL,
                           `slug` varchar(255) NOT NULL,
                           `title` varchar(255) NOT NULL,
                           `username` varchar(255) NOT NULL,
                           `user_id` bigint DEFAULT NULL,
                           `createdAt` datetime(6) NOT NULL,
                           `updatedAt` datetime(6) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `article_fk_user_id` (`user_id`),
                           CONSTRAINT `article_fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `article_tag` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `article_id` bigint NOT NULL,
                               `tag_id` bigint NOT NULL,
                               `createdAt` datetime(6) NOT NULL,
                               `updatedAt` datetime(6) NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `article_tag_fk_article_id` (`article_id`),
                               KEY `article_tag_fk_tag_id` (`tag_id`),
                               CONSTRAINT `article_tag_fk_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
                               CONSTRAINT `article_tag_fk_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
);

CREATE TABLE `comment` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `body` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `article_id` bigint NOT NULL,
                           `user_id` bigint DEFAULT NULL,
                           `createdAt` datetime(6) NOT NULL,
                           `updatedAt` datetime(6) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `comment_fk_article_id` (`article_id`),
                           KEY `comment_fk_user_id` (`user_id`),
                           CONSTRAINT `comment_fk_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
                           CONSTRAINT `comment_fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `favorite` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `article_id` bigint NOT NULL,
                            `user_id` bigint DEFAULT NULL,
                            `createdAt` datetime(6) NOT NULL,
                            `updatedAt` datetime(6) NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `favorite_fk_article_id` (`article_id`),
                            KEY `favorite_fk_user_id` (`user_id`),
                            CONSTRAINT `favorite_fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                            CONSTRAINT `favorite_fk_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
);

CREATE TABLE `follow` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `followee_id` bigint NOT NULL,
                          `follower_id` bigint NOT NULL,
                          `createdAt` datetime(6) NOT NULL,
                          `updatedAt` datetime(6) NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uniq_followee_id` (`followee_id`),
                          UNIQUE KEY `uniq_follower_id` (`follower_id`),
                          CONSTRAINT `follow_fk_follower_id` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`),
                          CONSTRAINT `follow_fk_followee_id` FOREIGN KEY (`followee_id`) REFERENCES `user` (`id`)
);