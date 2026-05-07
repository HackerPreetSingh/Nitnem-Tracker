package com.nitnem.tracker.state.handler.create.nitnem;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.repository.NitnemRepository;
import com.nitnem.tracker.service.NitnemService;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserService;
import com.nitnem.tracker.service.UserSessionService;
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

        int durationDays;

        try {
            durationDays = Integer.parseInt(message);

            if (durationDays <= 0) {

                senderService.send(
                        chatId,
                        "Days Duration must be greater than 0",
                        keyboardFactory.getKeyboard(chatId, message)
                );

                return;
            }

        } catch (NumberFormatException e) {

            senderService.send(
                    chatId,
                    "Please enter a valid Days Duration",
                    keyboardFactory.getKeyboard(chatId, message)
            );

            return;
        }

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