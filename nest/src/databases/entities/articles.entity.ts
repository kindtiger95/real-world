import {
    CreateDateColumn,
    UpdateDateColumn,
    Column,
    Entity,
    PrimaryGeneratedColumn,
    ManyToOne,
    JoinColumn,
    OneToMany,
} from 'typeorm';
import { CommentsEntity } from './comments.entity';
import { FavoritesEntity } from './favorites.entity';
import { TagsEntity } from './tags.entity';
import { UsersEntity } from './users.entity';

@Entity('Articles')
export class ArticlesEntity {
    @PrimaryGeneratedColumn()
    uid: number;

    @Column({ type: 'uuid' })
    author_id: number;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    slug: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    title: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
    })
    description: string;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
        type: 'text',
    })
    body: string;

    @CreateDateColumn({
        name: 'createdAt',
    })
    created_at: Date;

    @UpdateDateColumn({
        name: 'updatedAt',
    })
    updated_at: Date;

    @ManyToOne(() => UsersEntity, users => users.uid, {
        createForeignKeyConstraints: true,
        nullable: true,
        onUpdate: 'CASCADE',
        onDelete: 'CASCADE',
    })
    @JoinColumn({
        name: 'author_id',
    })
    users: UsersEntity;

    @OneToMany(() => CommentsEntity, comments => comments.articles)
    comments: CommentsEntity[];

    @OneToMany(() => FavoritesEntity, favorites => favorites.articles)
    favorites: FavoritesEntity[];

    @OneToMany(() => TagsEntity, tags => tags.articles)
    tags: TagsEntity[];
}
