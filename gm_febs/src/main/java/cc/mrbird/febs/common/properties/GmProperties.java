package cc.mrbird.febs.common.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author MrBird
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:gm.properties"})
@ConfigurationProperties()
public class GmProperties {
    private LanServerProperties login = new LanServerProperties();
}
