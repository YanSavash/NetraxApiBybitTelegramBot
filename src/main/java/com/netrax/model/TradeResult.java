package com.netrax.model;

public class TradeResult {
    private final String signalType;
    private final String symbol;
    private final boolean success;
    private final double profitLoss;

    public TradeResult(String signalType, String symbol, boolean success, double profitLoss) {
        this.signalType = signalType;
        this.symbol = symbol;
        this.success = success;
        this.profitLoss = profitLoss;
    }

    public String getSignalType() {
        return signalType;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getProfitLoss() {
        return profitLoss;
    }
}