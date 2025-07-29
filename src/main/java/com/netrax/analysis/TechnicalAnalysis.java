package com.netrax.analysis;

import com.netrax.bot.TelegramBotHandler;
import com.netrax.model.Signal;
import com.netrax.service.BybitApiClient;
import com.netrax.service.SignalTracker;
import com.netrax.analysis.strategies.*;
import com.netrax.util.Candlestick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TechnicalAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(TechnicalAnalysis.class);
    private final SignalTracker signalTracker;
    private final TelegramBotHandler botHandler;
    private final MovingAverageStrategy maStrategy;
    private final RSIStrategy rsiStrategy;
    private final BollingerBandsStrategy bbStrategy;
    private final MACDStrategy macdStrategy;

    public TechnicalAnalysis(SignalTracker signalTracker, TelegramBotHandler botHandler) {
        this.signalTracker = signalTracker;
        this.botHandler = botHandler;
        this.maStrategy = new MovingAverageStrategy();
        this.rsiStrategy = new RSIStrategy();
        this.bbStrategy = new BollingerBandsStrategy();
        this.macdStrategy = new MACDStrategy();
    }

    public void startAnalysis(BybitApiClient bybitClient) {
        new Thread(() -> {
            while (true) {
                try {
                    List<Candlestick> candles = bybitClient.getCandlestickData("BTCUSDT", "1h", 200);
                    analyzeStrategies(candles);
                    Thread.sleep(60000); // Проверяем каждую минуту
                } catch (Exception e) {
                    logger.error("Ошибка в анализе: {}", e.getMessage());
                }
            }
        }).start();
    }

    private void analyzeStrategies(List<Candlestick> candles) {
        Signal signal = null;
        if ((signal = maStrategy.analyze(candles)) != null) {
            processSignal(signal);
        }
        if ((signal = rsiStrategy.analyze(candles)) != null) {
            processSignal(signal);
        }
        if ((signal = bbStrategy.analyze(candles)) != null) {
            processSignal(signal);
        }
        if ((signal = macdStrategy.analyze(candles)) != null) {
            processSignal(signal);
        }
    }

    private void processSignal(Signal signal) {
        botHandler.sendSignal(signal);
        signalTracker.trackSignal(signal);
    }
}