package springboot.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonTypeName("user")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class UserResDto {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;
}
