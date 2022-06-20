import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ApiModule } from './api/api.module';
import { typeOrmModuleOptions } from './loader';

@Module({
    imports: [ApiModule, TypeOrmModule.forRootAsync(typeOrmModuleOptions)],
    controllers: [],
    providers: [],
})
export class AppModule {}
