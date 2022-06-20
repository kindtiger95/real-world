import { ConfigModule, ConfigModuleOptions, ConfigService } from '@nestjs/config';
import { TypeOrmModuleAsyncOptions } from '@nestjs/typeorm';
import { plainToInstance } from 'class-transformer';
import { validateSync } from 'class-validator';
import { EnvironmentVariables } from './config.parser';

const currentNodeEnv = process.env.DEPLOY || 'development';

function envValidate(config: Record<string, unknown>) {
    const configInstance = plainToInstance(EnvironmentVariables, config, {
        enableImplicitConversion: true,
    });

    const errors = validateSync(configInstance, {
        skipUndefinedProperties: false,
    });

    if (errors.length > 0) throw new Error(errors.toString());
    return configInstance;
}

export const configModuleOptions: ConfigModuleOptions = {
    envFilePath:
        currentNodeEnv === 'test' || currentNodeEnv === 'development'
            ? './config/dev.env'
            : currentNodeEnv === 'stage'
            ? './config/stage.env'
            : currentNodeEnv === 'production'
            ? './config/prod.env'
            : '',
    validate: envValidate,
    isGlobal: true,
};

export const typeOrmModuleOptions: TypeOrmModuleAsyncOptions = {
    imports: [ConfigModule],
    inject: [ConfigService],
    useFactory: (configService: ConfigService<EnvironmentVariables>) => {
        return {
            type: 'mysql',
            host: configService.get('DB_HOST', { infer: true }),
            port: configService.get('DB_PORT', { infer: true }),
            username: configService.get('DB_USER', { infer: true }),
            password: configService.get('DB_PASSWORD', { infer: true }),
            database: configService.get('DB_DATABASE', { infer: true }),
            entities: [],
            synchronize: false,
            autoLoadEntities: true,
            logging: currentNodeEnv === 'development' ? true : false,
        };
    },
};
