package springboot.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UserDto {
    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class LoginDto {
        @NotNull
        private String email;
        @NotNull private String password;
    }

    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class RegisterDto {
        @NotNull private String email;
        @NotNull private String username;
        @NotNull private String password;
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
