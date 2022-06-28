package springboot.entities;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
public class UserDto {
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String bio;

    @Getter
    @Setter
    private String image;

    @Getter
    @ToString
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class Login {
        private String email;

        private String password;
    }
}
