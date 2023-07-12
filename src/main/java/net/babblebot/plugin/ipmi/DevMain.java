package net.babblebot.plugin.ipmi;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.babblebot.BabblebotApplication;
import net.babblebot.api.IApplication;
import net.babblebot.api.config.EPluginPermission;
import net.babblebot.api.connect.ConnectQueue;
import net.babblebot.api.plugins.PluginType;
import net.babblebot.plugin.ipmi.config.ExamplePluginConfig;
import net.babblebot.plugins.PluginConfigParser;
import net.babblebot.plugins.PluginModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Dev Main class for Development Only will not be used inside the
 * main application when importing the plugin
 *
 * @author me@bdavies (Ben Davies)
 * @since 1.0.0
 */

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
@Import(BabblebotApplication.class)
@EnableJpaRepositories(basePackages = {"net.babblebot"})
@EntityScan(basePackages = {"net.babblebot"})

public class DevMain {
    public static void main(String[] args) {
        IApplication app = BabblebotApplication.make(DevMain.class, args);
    }

    @Bean
    CommandLineRunner onBoot(GenericApplicationContext gac, IApplication app, PluginConfigParser parser) {
        return args -> {
            gac.registerBean(IpmiPlugin.class);
            IpmiPlugin plugin = app.get(IpmiPlugin.class);
            val configObj = ExamplePluginConfig.builder()
                    .someValue("Test")
                    .build();
            gac.registerBean(ExamplePluginConfig.class, () -> configObj);
            String config = parser.pluginConfigToString(configObj);
            app.getPluginContainer()
                    .addPlugin(
                            plugin,
                            PluginModel
                                    .builder()
                                    .name("ipmi")
                                    .pluginType(PluginType.JAVA)
                                    .config(config)
                                    .namespace("ipmi")
                                    .pluginPermissions(EPluginPermission.all())
                                    .build()
                    );
        };
    }
}