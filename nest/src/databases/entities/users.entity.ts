import { CreateDateColumn, UpdateDateColumn, Column, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { ArticlesEntity } from './articles.entity';
import { CommentsEntity } from './comments.entity';
import { FavoritesEntity } from './favorites.entity';
import { FollowsEntity } from './follows.entity';

@Entity('Users')
export class UsersEntity {
    @PrimaryGeneratedColumn()
    uid: number;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    email: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    username: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    password: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    bio: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    image: string;

    @CreateDateColumn({
        name: 'createdAt',
    })
    created_at: Date;

    @UpdateDateColumn({
        name: 'updatedAt',
    })
    updated_at: Date;

    @OneToMany(() => ArticlesEntity, articles => articles.users)
    articles: ArticlesEntity[];

    @OneToMany(() => CommentsEntity, comments => comments.users)
    comments: ArticlesEntity[];

    @OneToMany(() => FavoritesEntity, favorites => favorites.users)
    favorites: FavoritesEntity[];

    @OneToMany(() => FollowsEntity, follows => follows.followee_users)
    followee_users: FollowsEntity[];

    @OneToMany(() => FollowsEntity, follows => follows.follower_users)
    follower_users: FollowsEntity[];
}
