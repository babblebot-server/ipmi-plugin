package net.babblebot.plugin.ipmi.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "ipmi")
@Configuration
@Data
@Slf4j
public class IpmiConfigurationProperties {
    private String path;
    private String ip;
    private String username;
    private String password;
    private List<Long> authUsers;
}
