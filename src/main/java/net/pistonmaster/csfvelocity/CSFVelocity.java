package net.pistonmaster.csfvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Plugin(id = "csf-velocity", name = "ConsoleSpamFixVelocity", version = "1.0.0",
        description = "Hide unwanted text!", authors = {"AlexProgrammerDE"})
public final class CSFVelocity {
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger logger;
    @DataDirectory
    @Inject
    private Path dataDirectory;
    private ConfigurationNode config;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Plugin startup logic
        logger.info("Starting up...");
        logger.info("Please do not go and ask brunyman to support this. This plugin has no affiliation with brunyman.");
        logger.info("Please wait... Loading config...");
        Path configFilePath = dataDirectory.resolve("config.yml");
        if (!configFilePath.toFile().exists()) {
            logger.info("Config file not found, creating one...");
            try {
                Files.createDirectories(dataDirectory);
                Files.copy(getClass().getClassLoader().getResourceAsStream("config.yml"), configFilePath);
            } catch (Exception e) {
                logger.error("Failed to create config file!", e);
            }
        }

        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configFilePath).build();
        try {
            config = loader.load();
        } catch (Exception e) {
            logger.error("Failed to load config file!", e);
        }
        logger.info("Please wait... Injecting filter...");

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new LogFilter(this));

        logger.info("CSFVelocity now loaded and start working!");
    }
}
