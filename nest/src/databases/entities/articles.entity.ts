import { CreateDateColumn, UpdateDateColumn, Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Articles {
    @PrimaryGeneratedColumn()
    uid: number;

    //author_id: number;

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

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;
}
