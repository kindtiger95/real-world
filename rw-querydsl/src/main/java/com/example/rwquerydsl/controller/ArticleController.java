package com.example.rwquerydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.rwquerydsl.domain.dto.ArticleDto.ArticleInquiryParameter;
import com.example.rwquerydsl.domain.dto.ArticleDto.CreateArticleReqDto;
import com.example.rwquerydsl.domain.dto.ArticleDto.MultipleArticleResDto;
import com.example.rwquerydsl.domain.dto.ArticleDto.SingleArticleResDto;
import com.example.rwquerydsl.domain.dto.ArticleDto.UpdateArticleReqDto;
import com.example.rwquerydsl.domain.dto.CommentDto.CreateCommentReqDto;
import com.example.rwquerydsl.domain.dto.CommentDto.MultipleCommentDto;
import com.example.rwquerydsl.domain.dto.CommentDto.SingleCommentDto;
import com.example.rwquerydsl.service.ArticleService;
import com.example.rwquerydsl.service.CommentService;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping("/articles")
    SingleArticleResDto createArticle(@RequestBody @Validated CreateArticleReqDto createArticleReqDto) {
        return this.articleService.createArticle(createArticleReqDto);
    }

    @PutMapping("/articles/{slug}")
    SingleArticleResDto updateArticle(@RequestBody @Validated UpdateArticleReqDto updateArticleReqDto,
        @PathVariable("slug") String slug) {
        return this.articleService.updateArticle(updateArticleReqDto, slug);
    }

    @DeleteMapping("/articles/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteArticle(@PathVariable("slug") String slug) {
        this.articleService.deleteArticle(slug);
    }

    @GetMapping("/articles")
    MultipleArticleResDto getArticles(@ModelAttribute ArticleInquiryParameter parameter) {
        return this.articleService.getArticles(parameter.getAuthor(), parameter.getTag(), parameter.getFavorited(), parameter.getLimit(), parameter.getOffset());
    }

    @GetMapping("/articles/{slug}")
    SingleArticleResDto getArticle(@PathVariable("slug") String slug) {
        return this.articleService.getArticle(slug);
    }

    @GetMapping("/articles/feed")
    MultipleArticleResDto getArticlesFeed(@ModelAttribute ArticleInquiryParameter parameter) {
        return this.articleService.getArticlesFeed(parameter.getLimit(), parameter.getOffset());
    }

    // Related Favorite //
    @PostMapping("/articles/{slug}/favorite")
    SingleArticleResDto favoriteArticle(@PathVariable("slug") String slug) {
        return this.articleService.favoriteArticle(slug);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unFavoriteArticle(@PathVariable("slug") String slug) {
        this.articleService.unFavoriteArticle(slug);
    }

    // Related Comments //
    @PostMapping("/articles/{slug}/comments")
    SingleCommentDto addComment(@PathVariable("slug") String slug, @RequestBody @Validated CreateCommentReqDto createCommentReqDto) {
        return this.commentService.addComment(slug, createCommentReqDto);
    }

    @GetMapping("/articles/{slug}/comments")
    MultipleCommentDto getComments(@PathVariable("slug") String slug) {
        return this.commentService.getComments(slug);
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable("slug") String slug, @PathVariable("id") Long id) {
        this.commentService.deleteComment(slug, id);
    }
}
