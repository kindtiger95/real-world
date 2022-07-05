package springboot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("custom")
public class CustomProperties {

    @Getter
    @Setter
    private String jwtSecretKey;

    @Getter
    @Setter
    private String jwtRoles;
}
