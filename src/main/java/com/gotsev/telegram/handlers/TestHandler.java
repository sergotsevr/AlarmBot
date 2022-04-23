package com.gotsev.telegram.handlers;

import com.gotsev.service.interfaces.MessageService;
import com.gotsev.service.interfaces.WeatherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.gotsev.telegram.BotConfig.TEST_TOKEN;
import static com.gotsev.telegram.BotConfig.TEST_USER;

@Component
@Slf4j
public class TestHandler extends TelegramLongPollingBot {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MessageService messageService;

    protected static final Pattern LATITUDE_PATTERN = Pattern.compile("^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$");
    protected static final Pattern LONGITUDE_PATTERN = Pattern.compile("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        log.debug("update");

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                if ("/start".equals(message.getText())) {
                    String greetingMessage = "Для получения текущего прогноза погоды пришлите координаты в формате \"широта:долгота\". Пример 44.508395:44.154497";
                    prepareAndSendMenuMessage(message.getChatId(), greetingMessage);
                } else {
                    String messageText = message.getText();
                    try {
                        validateMessageWithCoordinates(messageText);
                        double[] coordinates = getCoordinatesFromMessage(messageText);
                        prepareAndSendMenuMessage(message.getChatId(), messageService.prepareCurrentWeatherMessage(coordinates));
                    } catch (ValidationException exception) {
                        prepareAndSendMenuMessage(message.getChatId(), exception.getMessage());
                    }
                    log.debug("get update");
                }
            } else {
                log.debug("message has no text");
            }

        }
    }

    private double[] getCoordinatesFromMessage(String message) {
        return Arrays.stream(message.split(":")).mapToDouble(Double::parseDouble).toArray();
    }

    private void validateMessageWithCoordinates(String message) throws ValidationException {
        String[] coordinates = message.split(":");
        if (coordinates.length != 2) {
            throw new ValidationException("Неверный формат координат");
        }
        if (!LATITUDE_PATTERN.matcher(coordinates[0]).matches()) {
            throw new ValidationException("Неверный формат широты (первая координата)");
        }
        if (!LONGITUDE_PATTERN.matcher(coordinates[1]).matches()) {
            throw new ValidationException("Неверный формат широты (вторая координата)");
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
