import { CreateDateColumn, UpdateDateColumn, Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
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

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;
}
