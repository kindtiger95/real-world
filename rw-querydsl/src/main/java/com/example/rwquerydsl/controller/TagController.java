package com.example.rwquerydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.rwquerydsl.domain.dto.TagDto.TagsResDto;
import com.example.rwquerydsl.service.TagService;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/tags")
    public TagsResDto getAllTags() {
        return this.tagService.getAllTags();
    }
}
