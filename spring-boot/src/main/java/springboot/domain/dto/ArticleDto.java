package springboot.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

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
        private ProfileDto author;
    }

    @Builder
    @Getter
    public static class MultipleArticleResDto {
        private Integer articlesCount;
        private List<SingleArticleResDto> articles;
    }
}
