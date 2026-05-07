package com.nitnem.tracker.state.handler.delete.nitnem;

import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.service.NitnemService;
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
public class DeleteNitnemNameStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;
    private final NitnemService nitnemService;
    private final TelegramKeyboardFactory keyboardFactory;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_DELETE_NITNEM_NAME;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        if (nitnemService.deleteNitnem(chatId, message)!=1) {
            senderService.send(
                    chatId,
                    "No Such Nitnem Exists!",
                    keyboardFactory.getKeyboard(chatId, message)
            );
        } else {
            senderService.send(
                    chatId,
                    "Nitnem deleted successfully.",
                    keyboardFactory.getKeyboard(chatId, message)
            );
        }

        sessionService.clear(
                chatId
        );
    }
}