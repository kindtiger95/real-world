import { Controller, Get } from '@nestjs/common';
import { TagsService } from '../services/tags.service';

@Controller('/api/tags')
export class TagsController {
    constructor(private readonly tagsService: TagsService) {}

    @Get()
    async getAllTags() {
        return this.tagsService.getAllTags();
    }
}
