import {
    CreateDateColumn,
    UpdateDateColumn,
    Entity,
    PrimaryGeneratedColumn,
    ManyToOne,
    JoinColumn,
    Column,
} from 'typeorm';
import { ArticlesEntity } from './articles.entity';
import { UsersEntity } from './users.entity';

@Entity('Favorites')
export class FavoritesEntity {
    @PrimaryGeneratedColumn()
    uid: number;

    @Column({ type: 'uuid' })
    user_id: number;

    @Column({ type: 'uuid' })
    article_id: number;

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
        name: 'user_id',
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
