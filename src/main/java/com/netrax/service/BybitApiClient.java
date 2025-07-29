package com.netrax.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.*;
import com.bybit.api.client.domain.market.response.kline.MarketKlineResult;
import com.bybit.api.client.restApi.BybitApiAsyncMarketDataRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.netrax.util.Candlestick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BybitApiClient {
    private static final Logger logger = LoggerFactory.getLogger(BybitApiClient.class);
    private final BybitApiAsyncMarketDataRestClient client;

    public BybitApiClient(String apiKey, String apiSecret) {
        // Ключи не нужны для публичного endpoint
        this.client = BybitApiClientFactory
                .newInstance(null, null, "https://api.bybit.com", false)
                .newAsyncMarketDataRestClient();
    }

    public List<Candlestick> getCandlestickData(String symbol, String interval, int limit) {
        logger.info("Получение данных свечей для {} с интервалом {} и лимитом {}", symbol, interval, limit);
        List<Candlestick> candles = new ArrayList<>();
        try {
            MarketDataRequest request = MarketDataRequest.builder()
                    .category(CategoryType.LINEAR)
                    .symbol(symbol)
                    .marketInterval(MarketInterval.WEEKLY)
                    .limit(limit)
                    .build();
            MarketKlineResult result = client.getMarketLinesData(request);
            List<List<String>> klineData = result.getList();
            for (List<String> kline : klineData) {
                long timestamp = Long.parseLong(kline.get(0));
                double open = Double.parseDouble(kline.get(1));
                double high = Double.parseDouble(kline.get(2));
                double low = Double.parseDouble(kline.get(3));
                double close = Double.parseDouble(kline.get(4));
                double volume = Double.parseDouble(kline.get(5));
                candles.add(new Candlestick(timestamp, open, high, low, close, volume));
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении данных свечей: {}", e.getMessage());
        }
        return candles;
    }
}