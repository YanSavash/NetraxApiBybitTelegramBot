package com.netrax.bot;

import com.netrax.config.BotConfig;
import com.netrax.service.BybitApiClient;
import com.netrax.service.SignalTracker;
import com.netrax.analysis.TechnicalAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetraxApiBybitTelegramBot {
    private static final Logger logger = LoggerFactory.getLogger(NetraxApiBybitTelegramBot.class);
    private final TelegramBotHandler botHandler;
    private final BybitApiClient bybitClient;
    private final SignalTracker signalTracker;
    private final TechnicalAnalysis technicalAnalysis;

    public NetraxApiBybitTelegramBot() {
        BotConfig config = new BotConfig();
        this.botHandler = new TelegramBotHandler(config.getTelegramToken());
        this.bybitClient = new BybitApiClient(config.getBybitApiKey(), config.getBybitApiSecret());
        this.signalTracker = new SignalTracker();
        this.technicalAnalysis = new TechnicalAnalysis(signalTracker, botHandler);
    }

    public void start() {
        logger.info("Starting NetraxApiBybitTelegramBot...");
        botHandler.registerBot();
        technicalAnalysis.startAnalysis(bybitClient);
    }

    public static void main(String[] args) {
        new NetraxApiBybitTelegramBot().start();
    }
}