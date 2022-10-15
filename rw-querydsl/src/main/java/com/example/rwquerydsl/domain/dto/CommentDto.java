package com.example.rwquerydsl.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import com.example.rwquerydsl.domain.dto.ArticleDto.AuthorDto;

public class CommentDto {

    @Getter
    @JsonTypeName("comment")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class CreateCommentReqDto {
        @NotEmpty
        @NotNull
        private String body;
    }

    @Builder
    @Getter
    public static class CommentResDto {
        private Long id;
        private String body;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private AuthorDto author;
    }

    @Builder
    @Getter
    public static class SingleCommentDto {
        private CommentResDto comment;
    }

    @Getter
    public static class MultipleCommentDto {
        private final List<CommentResDto> comments = new ArrayList<>();
    }
}
