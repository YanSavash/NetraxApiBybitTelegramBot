package com.netrax.util;

public class TechnicalIndicatorCalculator {

    /**
     * Рассчитывает простую скользящую среднюю (SMA) для заданного периода.
     *
     * @param prices Массив цен закрытия.
     * @param period Период для расчета SMA.
     * @return Значение SMA.
     */
    public static double calculateSMA(double[] prices, int period) {
        if (prices.length < period) return 0.0;
        double sum = 0.0;
        for (int i = prices.length - period; i < prices.length; i++) {
            sum += prices[i];
        }
        return sum / period;
    }

    /**
     * Рассчитывает SMA с учетом смещения (для предыдущих свечей).
     *
     * @param prices Массив цен закрытия.
     * @param period Период для расчета SMA.
     * @param offset Смещение назад для расчета (0 = текущий период).
     * @return Значение SMA.
     */
    public static double calculateSMA(double[] prices, int period, int offset) {
        if (prices.length < period + offset) return 0.0;
        double sum = 0.0;
        for (int i = prices.length - period - offset; i < prices.length - offset; i++) {
            sum += prices[i];
        }
        return sum / period;
    }

    /**
     * Рассчитывает индекс относительной силы (RSI) для заданного периода.
     *
     * @param prices Массив цен закрытия.
     * @param period Период для расчета RSI (обычно 14).
     * @return Значение RSI.
     */
    public static double calculateRSI(double[] prices, int period) {
        if (prices.length < period + 1) return 50.0; // Значение по умолчанию

        double gains = 0.0;
        double losses = 0.0;
        int countGains = 0;
        int countLosses = 0;

        for (int i = prices.length - period; i < prices.length; i++) {
            double change = prices[i] - prices[i - 1];
            if (change > 0) {
                gains += change;
                countGains++;
            } else if (change < 0) {
                losses -= change;
                countLosses++;
            }
        }

        double avgGain = countGains > 0 ? gains / countGains : 0.0;
        double avgLoss = countLosses > 0 ? losses / countLosses : 0.0;

        if (avgLoss == 0) return 100.0; // Избегаем деления на ноль
        double rs = avgGain / avgLoss;
        return 100.0 - (100.0 / (1.0 + rs));
    }

    /**
     * Рассчитывает полосы Боллинджера для заданного периода и стандартного отклонения.
     *
     * @param prices Массив цен закрытия.
     * @param period Период для расчета SMA (обычно 20).
     * @param stdDev Множитель стандартного отклонения (обычно 2).
     * @return Массив [верхняя полоса, нижняя полоса].
     */
    public static double[] calculateBollingerBands(double[] prices, int period, double stdDev) {
        if (prices.length < period) return new double[]{0.0, 0.0};

        double sma = calculateSMA(prices, period);
        double sumSquaredDiff = 0.0;

        for (int i = prices.length - period; i < prices.length; i++) {
            double diff = prices[i] - sma;
            sumSquaredDiff += diff * diff;
        }

        double variance = sumSquaredDiff / period;
        double standardDeviation = Math.sqrt(variance);

        double upperBand = sma + stdDev * standardDeviation;
        double lowerBand = sma - stdDev * standardDeviation;

        return new double[]{upperBand, lowerBand};
    }

    /**
     * Рассчитывает MACD (линия MACD, сигнальная линия, гистограмма).
     *
     * @param prices Массив цен закрытия.
     * @param fastPeriod Период быстрой EMA (обычно 12).
     * @param slowPeriod Период медленной EMA (обычно 26).
     * @param signalPeriod Период сигнальной линии (обычно 9).
     * @return Массив [MACD линия, сигнальная линия, гистограмма].
     */
    public static double[] calculateMACD(double[] prices, int fastPeriod, int slowPeriod, int signalPeriod) {
        return calculateMACD(prices, fastPeriod, slowPeriod, signalPeriod, 0);
    }

    /**
     * Рассчитывает MACD с учетом смещения.
     *
     * @param prices Массив цен закрытия.
     * @param fastPeriod Период быстрой EMA.
     * @param slowPeriod Период медленной EMA.
     * @param signalPeriod Период сигнальной линии.
     * @param offset Смещение назад для расчета.
     * @return Массив [MACD линия, сигнальная линия, гистограмма].
     */
    public static double[] calculateMACD(double[] prices, int fastPeriod, int slowPeriod, int signalPeriod, int offset) {
        if (prices.length < slowPeriod + signalPeriod + offset) return new double[]{0.0, 0.0, 0.0};

        double[] fastEMA = calculateEMA(prices, fastPeriod, offset);
        double[] slowEMA = calculateEMA(prices, slowPeriod, offset);
        double macdLine = fastEMA[fastEMA.length - 1] - slowEMA[slowEMA.length - 1];

        double[] macdValues = new double[prices.length - slowPeriod - offset];
        for (int i = 0; i < macdValues.length; i++) {
            macdValues[i] = calculateEMA(prices, fastPeriod, i + offset)[prices.length - slowPeriod - offset - i - 1]
                    - calculateEMA(prices, slowPeriod, i + offset)[prices.length - slowPeriod - offset - i - 1];
        }

        double signalLine = calculateEMA(macdValues, signalPeriod, 0)[macdValues.length - 1];
        double histogram = macdLine - signalLine;

        return new double[]{macdLine, signalLine, histogram};
    }

    /**
     * Рассчитывает экспоненциальную скользящую среднюю (EMA).
     *
     * @param prices Массив цен закрытия.
     * @param period Период для расчета EMA.
     * @param offset Смещение назад для расчета.
     * @return Массив значений EMA.
     */
    private static double[] calculateEMA(double[] prices, int period, int offset) {
        if (prices.length < period + offset) return new double[]{0.0};

        double[] ema = new double[prices.length - offset];
        double multiplier = 2.0 / (period + 1);
        ema[0] = calculateSMA(prices, period, offset);

        for (int i = 1; i < ema.length; i++) {
            ema[i] = (prices[prices.length - offset - i] * multiplier) + (ema[i - 1] * (1 - multiplier));
        }

        return ema;
    }
}