package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.TagDto.TagsResDto;
import springboot.service.TagService;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/tags")
    public TagsResDto getAllTags() {
        return this.tagService.getAllTags();
    }
}
