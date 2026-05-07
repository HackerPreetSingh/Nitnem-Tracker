package com.nitnem.tracker.state.handler.create.nitnem;

import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserSessionService;
import com.nitnem.tracker.state.handler.StateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateThresholdCountStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_CREATE_THRESHOLD_COUNT;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        log.info(
                "Received Nitnem Threshold Count : {}",
                message
        );

        int thresholdCount;

        try {
            thresholdCount = Integer.parseInt(message);

            if (thresholdCount <= 0) {

                senderService.send(
                        chatId,
                        "Threshold must be greater than 0"
                );

                return;
            }

        } catch (NumberFormatException e) {

            senderService.send(
                    chatId,
                    "Please enter a valid number"
            );

            return;
        }

        senderService.send(
                chatId,
                "Received Nitnem Threshold Count : "
                        + thresholdCount
        );

        senderService.send(
                chatId,
                "Enter the total duration of nitnem"
        );

        session.setState(UserState.WAITING_FOR_CREATE_DURATION_DAYS);
        session.setTargetCount(thresholdCount);

        sessionService.setSession(
                chatId,
                session
        );
    }
}