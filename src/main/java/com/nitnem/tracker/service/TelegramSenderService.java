package com.nitnem.tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Service
@RequiredArgsConstructor
public class TelegramSenderService {

    private final TelegramClient telegramClient;

    public void send(Long chatId, String text)
            throws Exception {

        SendMessage sendMessage =
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(text)
                        .build();

        telegramClient.execute(sendMessage);
    }
}
