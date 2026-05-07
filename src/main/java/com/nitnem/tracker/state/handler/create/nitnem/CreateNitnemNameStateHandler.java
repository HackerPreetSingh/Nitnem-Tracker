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
public class CreateNitnemNameStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

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

        senderService.send(
                chatId,
                "Received Nitnem name : "
                        + message
        );

        senderService.send(
                chatId,
                "Enter the daily threshold count"
        );

        session.setState(UserState.WAITING_FOR_CREATE_THRESHOLD_COUNT);
        session.setNitnemName(message);

        sessionService.setSession(
                chatId,
                session
        );
    }
}