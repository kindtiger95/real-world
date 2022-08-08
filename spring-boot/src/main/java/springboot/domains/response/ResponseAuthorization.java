package springboot.domains.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class ResponseAuthorization {
    private String code;
    private String message;
}
