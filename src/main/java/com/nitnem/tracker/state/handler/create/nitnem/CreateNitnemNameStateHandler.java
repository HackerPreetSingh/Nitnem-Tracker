package com.nitnem.tracker.state.handler.create.nitnem;

import com.nitnem.tracker.entity.User;
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
public class CreateNitnemNameStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;
    private final TelegramKeyboardFactory keyboardFactory;
    private final ValidationService validationService;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_CREATE_NITNEM_NAME;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        log.info(
                "Received Nitnem name : {}",
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

        ValidationResult<String> result =
                validationService.validateNitnemName(message);

        if (!result.isValid()) {

            senderService.send(
                    chatId,
                    result.getErrorMessage()
            );

            return;
        }

        String nitnemName = result.getValue();

        senderService.send(
                chatId,
                "Received Nitnem name : "
                        + nitnemName,
                keyboardFactory.getKeyboard(chatId, nitnemName)
        );

        senderService.send(
                chatId,
                "Enter the nitnem type: Maala/Raw",
                keyboardFactory.getKeyboard(chatId, "/unit")
        );

        session.setState(UserState.WAITING_FOR_CREATE_NITNEM_UNIT);
        session.setNitnemName(nitnemName);

        session.setLastProcessedMessage(nitnemName);
        session.setLastProcessedAt(LocalDateTime.now());

        sessionService.setSession(
                chatId,
                session
        );
    }
}