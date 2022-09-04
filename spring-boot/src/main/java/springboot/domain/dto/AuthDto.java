package springboot.domain.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonTypeName("errors")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class AuthDto {

    List<String> body;
}
