package com.netrax.analysis.strategies;

import com.netrax.model.Signal;
import com.netrax.util.Candlestick;
import com.netrax.util.TechnicalIndicatorCalculator;

import java.util.List;

public class MovingAverageStrategy {
    public Signal analyze(List<Candlestick> candles) {
        if (candles.size() < 50) return null;

        double[] closes = candles.stream().mapToDouble(Candlestick::getClose).toArray();
        double shortMA = TechnicalIndicatorCalculator.calculateSMA(closes, 10);
        double longMA = TechnicalIndicatorCalculator.calculateSMA(closes, 50);

        double prevShortMA = TechnicalIndicatorCalculator.calculateSMA(closes, 10, 1);
        double prevLongMA = TechnicalIndicatorCalculator.calculateSMA(closes, 50, 1);

        if (prevShortMA <= prevLongMA && shortMA > longMA) {
            return new Signal("BUY", "BTCUSDT", candles.get(candles.size() - 1).getClose(), "MovingAverage");
        } else if (prevShortMA >= prevLongMA && shortMA < longMA) {
            return new Signal("SELL", "BTCUSDT", candles.get(candles.size() - 1).getClose(), "MovingAverage");
        }
        return null;
    }
}