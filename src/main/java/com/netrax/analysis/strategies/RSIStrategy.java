package com.netrax.analysis.strategies;

import com.netrax.model.Signal;
import com.netrax.util.Candlestick;
import com.netrax.util.TechnicalIndicatorCalculator;

import java.util.List;

public class RSIStrategy {
    public Signal analyze(List<Candlestick> candles) {
        if (candles.size() < 14) return null;

        double[] closes = candles.stream().mapToDouble(Candlestick::getClose).toArray();
        double rsi = TechnicalIndicatorCalculator.calculateRSI(closes, 14);

        if (rsi < 30) {
            return new Signal("BUY", "BTCUSDT", candles.get(candles.size() - 1).getClose(), "RSI");
        } else if (rsi > 70) {
            return new Signal("SELL", "BTCUSDT", candles.get(candles.size() - 1).getClose(), "RSI");
        }
        return null;
    }
}