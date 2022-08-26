package springboot.config;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "custom")
public class ConfigProvider {

    @NotEmpty private String jwtSecretKey;
    @NotEmpty private String jwtRole;
    @NotEmpty private String securityRole;
    @NotEmpty private String basicRole;
    @NotEmpty private String basicUser;
    @NotEmpty private String basicPassword;
}
