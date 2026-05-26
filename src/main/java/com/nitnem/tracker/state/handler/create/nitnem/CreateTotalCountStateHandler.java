package com.nitnem.tracker.state.handler.create.nitnem;

import com.nitnem.tracker.model.NitnemUnit;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.model.ValidationResult;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserSessionService;
import com.nitnem.tracker.service.ValidationService;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTotalCountStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    private final ValidationService validationService;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_CREATE_TOTAL_COUNT;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        log.info(
                "Received Nitnem Total Count : {}",
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

        ValidationResult<Integer> result =
                validationService.validatePositiveInteger(
                        message,
                        "Total Count",
                        1,
                        999999
                );

        if (!result.isValid()) {

            senderService.send(
                    chatId,
                    result.getErrorMessage(),
                    keyboardFactory.getKeyboard(chatId, message)
            );

            return;
        }

        int totalCount = result.getValue();

        senderService.send(
                chatId,
                "Received Nitnem Total Count : "
                        + totalCount,
                keyboardFactory.getKeyboard(chatId, message)
        );

        senderService.send(
                chatId,
                "Enter the total duration of nitnem in days : ",
                keyboardFactory.getKeyboard(chatId, message)
        );

//        int convertedCount = session.getNitnemUnit() == NitnemUnit.MAALA?totalCount*110:totalCount;

        session.setState(UserState.WAITING_FOR_CREATE_DURATION_DAYS);
        session.setTargetCount(totalCount);

        session.setLastProcessedMessage(message);
        session.setLastProcessedAt(LocalDateTime.now());

        sessionService.setSession(
                chatId,
                session
        );
    }
}