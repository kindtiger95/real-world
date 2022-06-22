import {
    CreateDateColumn,
    UpdateDateColumn,
    Column,
    Entity,
    PrimaryGeneratedColumn,
    ManyToOne,
    JoinColumn,
} from 'typeorm';
import { ArticlesEntity } from './articles.entity';
import { UsersEntity } from './users.entity';

@Entity('Comments')
export class CommentsEntity {
    @PrimaryGeneratedColumn()
    uid: number;

    @Column({ type: 'uuid' })
    author_id: number;

    @Column({ type: 'uuid' })
    article_id: number;

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

    @ManyToOne(() => ArticlesEntity, articles => articles.uid, {
        createForeignKeyConstraints: true,
        nullable: true,
        onUpdate: 'CASCADE',
        onDelete: 'CASCADE',
    })
    @JoinColumn({
        name: 'article_id',
    })
    articles: ArticlesEntity;
}
