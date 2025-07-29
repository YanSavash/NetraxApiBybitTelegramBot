package com.netrax.service;

import com.netrax.bot.TelegramBotHandler;
import com.netrax.model.Signal;
import com.netrax.model.TradeResult;
import org.slf4j.Logger;
import  org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SignalTracker {
    private static final Logger logger = LoggerFactory.getLogger(SignalTracker.class);
    private final Map<Signal, TradeResult> trackedSignals = new HashMap<>();
    private final TelegramBotHandler botHandler;

    public SignalTracker() {
        this.botHandler = null; // Будет внедрен через конструктор или DI
    }

    public void trackSignal(Signal signal) {
        logger.info("Отслеживание сигнала: {}", signal.getType());
        // Для примера: сигнал считается успешным, если цена изменилась на 1%
        double targetPrice = signal.getType().equals("BUY") ? signal.getPrice() * 1.01 : signal.getPrice() * 0.99;
        double profitLoss = signal.getType().equals("BUY") ? targetPrice - signal.getPrice() : signal.getPrice() - targetPrice;
        TradeResult result = new TradeResult(signal.getType(), signal.getSymbol(), true, profitLoss);
        trackedSignals.put(signal, result);
        if (botHandler != null) {
            botHandler.sendTradeResult(result);
        }
    }
}