package springboot.controllers.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

    @Getter
    @ToString
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class ReqLoginDto {
        private String email;

        private String password;
    }

    @Getter
    @ToString
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class ReqRegisterDto {
        private String username;

        private String email;

        private String password;
    }

    @Getter
    @ToString
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class ReqUpdateDto {
        private String email;

        private String bio;

        private String image;
    }
}
