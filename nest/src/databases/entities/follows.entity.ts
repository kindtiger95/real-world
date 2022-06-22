import {
    CreateDateColumn,
    UpdateDateColumn,
    Entity,
    PrimaryGeneratedColumn,
    ManyToOne,
    JoinColumn,
    Column,
} from 'typeorm';
import { UsersEntity } from './users.entity';

@Entity('Follows')
export class FollowsEntity {
    @PrimaryGeneratedColumn()
    uid: number;

    @Column({ type: 'uuid' })
    follower: number;

    @Column({ type: 'uuid' })
    followee: number;

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
        name: 'follower',
    })
    followee_users: UsersEntity[];

    @ManyToOne(() => UsersEntity, users => users.uid, {
        createForeignKeyConstraints: true,
        nullable: true,
        onUpdate: 'CASCADE',
        onDelete: 'CASCADE',
    })
    @JoinColumn({
        name: 'followee',
    })
    follower_users: UsersEntity[];
}
