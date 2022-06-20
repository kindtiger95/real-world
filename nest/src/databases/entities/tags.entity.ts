import { CreateDateColumn, UpdateDateColumn, Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Tags {
    @PrimaryGeneratedColumn()
    uid: number;

    // article_id: number;

    @Column({
        charset: 'utf8',
        collation: 'utf8_general_ci',
        nullable: false,
        type: 'text',
    })
    tag: string;

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;
}
