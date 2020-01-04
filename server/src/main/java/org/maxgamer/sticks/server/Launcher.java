package org.maxgamer.sticks.server;

import org.maxgamer.sticks.server.config.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(ServerConfig.class)
public class Launcher {
    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
