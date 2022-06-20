import { CreateDateColumn, UpdateDateColumn, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Follows {
    @PrimaryGeneratedColumn()
    uid: number;

    // followee: number;

    // follower: number;

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;
}
