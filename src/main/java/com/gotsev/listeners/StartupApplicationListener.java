package com.gotsev.listeners;

import com.gotsev.service.WeatherServiceImpl;
import com.gotsev.service.interfaces.WeatherService;
import com.gotsev.telegram.handlers.TestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class StartupApplicationListener {

    @Autowired
    private TestHandler testHandler;

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("r");
        try {
            TelegramBotsApi telegramBotsApi = createTelegramBotsApi();
            try {
                // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
                telegramBotsApi.registerBot(testHandler);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TelegramBotsApi createTelegramBotsApi() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi;
        telegramBotsApi = createLongPollingTelegramBotsApi();
        return telegramBotsApi;
    }

    /**
     * @return TelegramBotsApi to register the bots.
     * @brief Creates a Telegram Bots Api to use Long Polling (getUpdates) bots.
     */
    private static TelegramBotsApi createLongPollingTelegramBotsApi() throws TelegramApiException {

        return new TelegramBotsApi(DefaultBotSession.class);
    }
}
