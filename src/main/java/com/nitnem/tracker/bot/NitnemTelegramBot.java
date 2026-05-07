package com.nitnem.tracker.bot;

import com.nitnem.tracker.service.TelegramCommandService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NitnemTelegramBot
        implements SpringLongPollingBot,
        LongPollingUpdateConsumer {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramCommandService commandService;

    @PostConstruct
    public void init() {
        log.info("BOT INITIALIZED");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(List<Update> updates) {

        for (Update update : updates) {

            if (update.hasMessage()
                    && update.getMessage().hasText()) {

                String message =
                        update.getMessage().getText();

                Long chatId =
                        update.getMessage().getChatId();

                log.info("Message: {}", message);

                try {
                    commandService.processMessage(
                            chatId,
                            message
                    );
                } catch (Exception e) {
                    log.error(
                            "Error processing message",
                            e
                    );
                }
            }
        }
    }
}
