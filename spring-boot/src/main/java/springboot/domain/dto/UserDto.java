package springboot.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class LoginDto {
        @NotNull
        @NotEmpty
        private String email;

        @NotNull
        @NotEmpty
        private String password;
    }

    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class RegisterDto {
        @NotNull
        @NotEmpty
        private String email;

        @NotNull
        @NotEmpty
        private String username;

        @NotNull
        @NotEmpty
        private String password;
    }

    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class UpdateDto {
        private String email;
        private String bio;
        private String image;
        private String username;
        private String password;
    }

    @Builder
    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class UserResDto {
        private String email;
        private String token;
        private String username;
        private String bio;
        private String image;
    }
}
