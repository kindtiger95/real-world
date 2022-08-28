package springboot.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class AuthResDto {
    private String code;
    private String message;
}
