package springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.dto.ArticleDto.CreateArticleReqDto;
import springboot.domain.dto.ArticleDto.MultipleArticleResDto;
import springboot.domain.dto.ArticleDto.SingleArticleResDto;
import springboot.domain.dto.ArticleDto.UpdateArticleReqDto;
import springboot.service.ArticleService;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    SingleArticleResDto createArticle(@RequestBody @Validated CreateArticleReqDto createArticleReqDto) {
        return this.articleService.createArticle(createArticleReqDto);
    }

    @PutMapping("/articles/{slug}")
    SingleArticleResDto updateArticle(@RequestBody @Validated UpdateArticleReqDto updateArticleReqDto,
        @PathVariable("slug") String slug) {
        return this.articleService.updateArticle(updateArticleReqDto, slug);
    }

    @GetMapping("/articles")
    MultipleArticleResDto getArticles(@RequestParam(value = "tag", required = false) String tag,
        @RequestParam(value = "author", required = false) String author, @RequestParam(value = "favorited", required = false) String favorited,
        @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "offset", required = false) Integer offset) {

        if (limit == null)
            limit = 20;

        if (offset == null)
            offset = 0;

        return this.articleService.getArticle(author, tag, favorited, limit, offset);
    }
}