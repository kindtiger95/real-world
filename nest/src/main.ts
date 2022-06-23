import { ConfigService } from '@nestjs/config';
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { EnvironmentVariables } from './config.parser';

async function bootstrap() {
    const app = await NestFactory.create(AppModule);
    const configService: ConfigService<EnvironmentVariables> = app.get(ConfigService);
    const port = configService.get('HOST_PORT');
    await app.listen(port);
}
bootstrap();
