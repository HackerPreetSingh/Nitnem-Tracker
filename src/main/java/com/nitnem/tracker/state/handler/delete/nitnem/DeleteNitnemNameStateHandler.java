package com.nitnem.tracker.state.handler.delete.nitnem;

import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.model.ValidationResult;
import com.nitnem.tracker.service.NitnemService;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserSessionService;
import com.nitnem.tracker.service.ValidationService;
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
    private final ValidationService validationService;

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

        if (nitnemService.deleteNitnem(chatId, nitnemName)!=1) {
            senderService.send(
                    chatId,
                    "No Such Nitnem Exists!",
                    keyboardFactory.getKeyboard(chatId, nitnemName)
            );
        } else {
            senderService.send(
                    chatId,
                    "Nitnem deleted successfully.",
                    keyboardFactory.getMainMenuKeyboard()
            );
        }

        sessionService.clear(
                chatId
        );
    }
}