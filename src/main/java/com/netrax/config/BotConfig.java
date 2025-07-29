package com.netrax.config;

import java.io.IOException;
import java.util.Properties;

public class BotConfig {
    private final Properties properties;

    public BotConfig() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }

    public String getTelegramToken() {
        return properties.getProperty("telegram.bot.token");
    }

    public String getBybitApiKey() {
        return properties.getProperty("bybit.api.key");
    }

    public String getBybitApiSecret() {
        return properties.getProperty("bybit.api.secret");
    }
}