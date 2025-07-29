package com.netrax.model;

public class Signal {
    private final String type; // BUY or SELL
    private final String symbol;
    private final double price;
    private final String strategy;

    public Signal(String type, String symbol, double price, String strategy) {
        this.type = type;
        this.symbol = symbol;
        this.price = price;
        this.strategy = strategy;
    }

    public String getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public String getStrategy() {
        return strategy;
    }
}