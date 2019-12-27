package org.maxgamer.sticks.common;

import org.maxgamer.sticks.common.config.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(ServerConfig.class)
public class Launcher {
    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
