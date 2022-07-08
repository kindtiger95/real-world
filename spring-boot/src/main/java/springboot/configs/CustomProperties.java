package springboot.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("custom")
public class CustomProperties {

    private String jwtSecretKey;

    private String jwtRole;

    private String securityRole;

    private String basicRole;

    private String basicUser;

    private String basicPassword;
}
