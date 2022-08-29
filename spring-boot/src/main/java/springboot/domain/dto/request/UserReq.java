package springboot.domain.dto.request;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

public class UserReq {
    @ToString
    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class LoginDto {
        @NotNull private String email;
        @NotNull private String password;
    }

    @ToString
    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class RegisterDto {
        @NotNull private String email;
        @NotNull private String username;
        @NotNull private String password;
    }

    @ToString
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
}
