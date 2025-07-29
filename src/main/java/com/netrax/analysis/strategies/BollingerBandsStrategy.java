package com.netrax.analysis.strategies;

import com.netrax.model.Signal;
import com.netrax.util.Candlestick;
import com.netrax.util.TechnicalIndicatorCalculator;

import java.util.List;

public class BollingerBandsStrategy {
    public Signal analyze(List<Candlestick> candles) {
        if (candles.size() < 20) return null;

        double[] closes = candles.stream().mapToDouble(Candlestick::getClose).toArray();
        double[] bands = TechnicalIndicatorCalculator.calculateBollingerBands(closes, 20, 2);
        double upperBand = bands[0];
        double lowerBand = bands[1];
        double currentPrice = closes[closes.length - 1];

        if (currentPrice > upperBand) {
            return new Signal("BUY", "BTCUSDT", currentPrice, "BollingerBands");
        } else if (currentPrice < lowerBand) {
            return new Signal("SELL", "BTCUSDT", currentPrice, "BollingerBands");
        }
        return null;
    }
}