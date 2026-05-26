package com.nitnem.tracker.state.handler.create.nitnem;

import com.nitnem.tracker.model.NitnemUnit;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserSessionService;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNitnemUnitStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_CREATE_NITNEM_UNIT;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        log.info(
                "Received Nitnem Unit : {}",
                message
        );

        if (message.equals(session.getLastProcessedMessage())
                &&
                LocalDateTime.now()
                        .minusSeconds(10)
                        .isBefore(session.getLastProcessedAt())
        ) {

            return;
        }

        String nitnemUnit = message.toLowerCase();

        if (nitnemUnit.equals("maala") ) {
            session.setNitnemUnit(NitnemUnit.MAALA);
        } else {
            session.setNitnemUnit(NitnemUnit.RAW);
        }

        senderService.send(
                chatId,
                "Received Nitnem Unit : "
                        + nitnemUnit,
                keyboardFactory.getKeyboard(chatId, nitnemUnit)
        );

        senderService.send(
                chatId,
                "Enter the Total Count",
                keyboardFactory.getKeyboard(chatId, message)
        );

        session.setState(UserState.WAITING_FOR_CREATE_TOTAL_COUNT);
        session.setLastProcessedMessage(message);
        session.setLastProcessedAt(LocalDateTime.now());

        sessionService.setSession(
                chatId,
                session
        );
    }
}