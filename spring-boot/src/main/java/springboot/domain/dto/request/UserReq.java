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
    public static class Login {
        @NotNull private String email;
        @NotNull private String password;
    }

    @ToString
    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class Register {
        @NotNull private String username;
        @NotNull private String email;
        @NotNull private String password;
    }

    @ToString
    @Getter
    @JsonTypeName("user")
    @JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
    public static class Update {
        private String email;
        private String bio;
        private String image;
    }
}