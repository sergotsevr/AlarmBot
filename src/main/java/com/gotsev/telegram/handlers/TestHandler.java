package com.gotsev.telegram.handlers;

import com.gotsev.service.interfaces.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gotsev.telegram.BotConfig.TEST_TOKEN;
import static com.gotsev.telegram.BotConfig.TEST_USER;

@Component
@Slf4j
public class TestHandler extends TelegramLongPollingBot {
    @Autowired
    private WeatherService weatherService;

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("update");

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {

                if (message.getText().equals("/start")) {
                    prepareAndSendMenuMessage(message.getChatId(),  weatherService.getWindDirection(51.667102,39.327199).toString());
                } else {
                    log.debug("message command to start - " + message.getText());
                }
                log.debug("get update");
            } else {
                log.debug("message has no text");
            }

        }
    }

    @Override
    public String getBotUsername() {
        return TEST_USER;
    }

    @Override
    public String getBotToken() {
        return TEST_TOKEN;
    }

    private void prepareAndSendMenuMessage(Long chatId, String text) {
        SendMessage greetingMessage = new SendMessage();
        greetingMessage.setChatId(chatId.toString());
        //greetingMessage.setReplyMarkup(mainOptions());
        greetingMessage.setText(text);
        greetingMessage.enableMarkdown(true);
        try {
            execute(greetingMessage);
        } catch (TelegramApiException e) {
            log.error("Error" + e);
        }
    }
}
