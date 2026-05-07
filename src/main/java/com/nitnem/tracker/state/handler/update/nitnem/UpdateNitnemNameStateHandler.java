package com.nitnem.tracker.state.handler.update.nitnem;

import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserSessionService;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateNitnemNameStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_UPDATE_NITNEM_NAME;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        senderService.send(
                chatId,
                "Enter count",
                keyboardFactory.getExitMenuKeyboard()
        );

        session.setNitnemName(message);
        session.setState(UserState.WAITING_FOR_UPDATE_NITNEM_COUNT);

        sessionService.setSession(
                chatId,
                session
        );
    }
}