package com.netrax.bot;

import com.netrax.model.Signal;
import com.netrax.model.TradeResult;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotHandler extends TelegramLongPollingBot {
    private final String botToken;
    private final String botUsername = "@NetraxBybitBot";

    public TelegramBotHandler(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            if (message.equals("/start")) {
                sendMessage(chatId, "Привет! Я бот для торговых сигналов Bybit. Отправляю сигналы и отчеты.");
            }
        }
    }

    public void sendSignal(Signal signal) {
        String message = String.format("📊 Сигнал: %s\nИнструмент: %s\nЦена: %.2f\nСтратегия: %s",
                signal.getType(), signal.getSymbol(), signal.getPrice(), signal.getStrategy());
        sendMessageToAll(message);
    }

    public void sendTradeResult(TradeResult result) {
        String message = String.format("📈 Результат сигнала: %s\nИнструмент: %s\nРезультат: %s\nПрибыль/Убыток: %.2f",
                result.getSignalType(), result.getSymbol(), result.isSuccess() ? "Отработал" : "Не отработал",
                result.getProfitLoss());
        sendMessageToAll(message);
    }

    private void sendMessageToAll(String text) {
        // Здесь должен быть список chatId подписчиков. Для примера отправляем в один chatId.
        String chatId = "YOUR_CHAT_ID"; // Замени на реальный chatId
        sendMessage(chatId, text);
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void registerBot() {
        // Регистрация бота выполняется автоматически при создании экземпляра
    }
}