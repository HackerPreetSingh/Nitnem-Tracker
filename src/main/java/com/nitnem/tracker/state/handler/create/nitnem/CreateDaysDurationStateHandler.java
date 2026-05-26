package com.nitnem.tracker.state.handler.create.nitnem;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.model.ValidationResult;
import com.nitnem.tracker.repository.NitnemRepository;
import com.nitnem.tracker.service.*;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateDaysDurationStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final NitnemService nitnemService;

    private final TelegramKeyboardFactory keyboardFactory;
    private final ValidationService validationService;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_CREATE_DURATION_DAYS;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        log.info(
                "Received Nitnem Duration Days : {}",
                message
        );

        ValidationResult<Integer> result =
                validationService.validatePositiveInteger(
                        message,
                        "Duration Days",
                        1,
                        3650
                );

        if (!result.isValid()) {

            senderService.send(
                    chatId,
                    result.getErrorMessage(),
                    keyboardFactory.getKeyboard(chatId, message)
            );

            return;
        }

        int durationDays = result.getValue();

        senderService.send(
                chatId,
                "Creating your nitnem...",
                keyboardFactory.getKeyboard(chatId, message)
        );

        session.setDurationDays(durationDays);
        nitnemService.saveNitnem(session, chatId);

        senderService.send(
                chatId,
                "Nitnem Created Successfully",
                keyboardFactory.getMainMenuKeyboard()
        );

        sessionService.clear(
                chatId
        );
    }
}