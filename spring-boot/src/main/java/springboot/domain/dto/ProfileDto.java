package springboot.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonTypeName("profile")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class ProfileDto {
    private String username;
    private String bio;
    private String image;
    private Boolean following;
}
