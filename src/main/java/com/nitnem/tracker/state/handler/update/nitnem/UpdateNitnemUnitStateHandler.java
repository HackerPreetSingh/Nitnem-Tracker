package com.nitnem.tracker.state.handler.update.nitnem;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.model.NitnemUnit;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.model.ValidationResult;
import com.nitnem.tracker.service.*;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateNitnemUnitStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    private final NitnemEntryService nitnemEntryService;

    private final NitnemService nitnemService;

    private final ValidationService validationService;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_UPDATE_NITNEM_UNIT;
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

        if (nitnemUnit.contains("maala") ) {
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
                "Enter count",
                keyboardFactory.getExitMenuKeyboard()
        );

        session.setState(UserState.WAITING_FOR_UPDATE_NITNEM_COUNT);

        session.setLastProcessedMessage(message);
        session.setLastProcessedAt(LocalDateTime.now());

        sessionService.setSession(
                chatId,
                session
        );
    }
}