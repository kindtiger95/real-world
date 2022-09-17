package springboot.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ArticleDto {

    @Getter
    @JsonTypeName("article")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class CreateArticleReqDto {

        @NotEmpty private String title;
        @NotNull private String description;
        private String body;
        private List<String> tagList;
    }

    @Getter
    @JsonTypeName("article")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class UpdateArticleReqDto {
        private String title;
        private String description;
        private String body;
    }

    @Builder
    @Getter
    @JsonTypeName("article")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class SingleArticleResDto {

        private String slug;
        private String title;
        private String description;
        private String body;
        private List<String> tagList;
        private String createdAt;
        private String updatedAt;
        private Boolean favorite;
        private Integer favoritesCount;
        private AuthorDto author;
    }

    @Builder
    @Getter
    public static class AuthorDto {
        private String username;
        private String bio;
        private String image;
        private Boolean following;
    }

    @Setter
    @Getter
    public static class MultipleArticleResDto {
        private Integer articlesCount;
        private List<SingleArticleResDto> articles = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class ArticleInquiryParameter {
        private String author;
        private String tag;
        private String favorited;
        private Integer limit = 20;
        private Integer offset = 0;
    }
}
