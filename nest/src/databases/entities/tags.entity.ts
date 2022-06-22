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

@Entity('Tags')
export class TagsEntity {
    @PrimaryGeneratedColumn()
    uid: number;

    @Column({ type: 'uuid' })
    article_id: number;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
        type: 'text',
    })
    tag: string;

    @CreateDateColumn({
        name: 'createdAt',
    })
    created_at: Date;

    @UpdateDateColumn({
        name: 'updatedAt',
    })
    updated_at: Date;

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
