package com.netrax.analysis.strategies;

import com.netrax.model.Signal;
import com.netrax.util.Candlestick;
import com.netrax.util.TechnicalIndicatorCalculator;

import java.util.List;

public class MACDStrategy {
    public Signal analyze(List<Candlestick> candles) {
        if (candles.size() < 26) return null;

        double[] closes = candles.stream().mapToDouble(Candlestick::getClose).toArray();
        double[] macd = TechnicalIndicatorCalculator.calculateMACD(closes, 12, 26, 9);
        double macdLine = macd[0];
        double signalLine = macd[1];
        double prevMacdLine = TechnicalIndicatorCalculator.calculateMACD(closes, 12, 26, 9, 1)[0];
        double prevSignalLine = TechnicalIndicatorCalculator.calculateMACD(closes, 12, 26, 9, 1)[1];

        if (prevMacdLine <= prevSignalLine && macdLine > signalLine) {
            return new Signal("BUY", "BTCUSDT", candles.get(candles.size() - 1).getClose(), "MACD");
        } else if (prevMacdLine >= prevSignalLine && macdLine < signalLine) {
            return new Signal("SELL", "BTCUSDT", candles.get(candles.size() - 1).getClose(), "MACD");
        }
        return null;
    }
}