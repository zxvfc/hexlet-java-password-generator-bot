package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBot extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        System.out.println("Starting telegram bot");
        TelegramBot telegramBot = new TelegramBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramBot);
    }

    @Override
    public String getBotUsername() {
        return "MyPasswordGeneratorBot";
    }

    @Override
    public String getBotToken() {
        return "здесь необходимо разместить токен своего бота полученный у BotFather";
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;
        Message message = update.getMessage();
        String messageText = message.getText();
        String userName = message.getFrom().getUserName();
        System.out.println("From: '" + userName + "'\ntext: '" + messageText + "'" + "\n-------------");

        if (messageText.startsWith("/start")) {
            final String helloText = "Hello and Welcome! To generate new password send command '/generate'";
            sendMessage(message.getChatId(), helloText, false);
        }

        if (messageText.startsWith("/generate")) {
            String[] messageParts = messageText.split(" ");
            int passwordLength = 10;
            if (messageParts.length > 1) {
                passwordLength = Integer.parseInt(messageParts[1]);
            }
            PasswordGenerator passwordGenerator = new PasswordGenerator();
            String password = passwordGenerator.generateNewPassword(passwordLength);
            sendMessage(message.getChatId(), "||" + password + "||", true);
        }
    }

    void sendMessage(long chatId, String messageText, boolean isMarkdown) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdownV2(isMarkdown);
        sendMessage.setText(messageText);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Failed to send a message");
        }
    }
}
