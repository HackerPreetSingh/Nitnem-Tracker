package com.nitnem.tracker.state.handler.update.nitnem;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.model.ValidationResult;
import com.nitnem.tracker.service.*;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateCountStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    private final NitnemEntryService nitnemEntryService;

    private final NitnemService nitnemService;

    private final ValidationService validationService;

    @Override
    public UserState getSupportedState() {

        return UserState.WAITING_FOR_UPDATE_NITNEM_COUNT;
    }

    @Override
    public void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception {

        ValidationResult<Integer> result =
                validationService.validatePositiveInteger(
                        message,
                        "Today Count",
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

        int currentCount = result.getValue();

        Nitnem nitnem = nitnemService
                .findByUserTelegramChatIdAndName(
                        chatId,
                        session.getNitnemName()
                );

        int thresholdCount = nitnem.getTargetCount();

        ValidationResult<Integer> result2 =
                validationService.validateCurrentCount(
                        currentCount,
                        thresholdCount
                );

        if (!result2.isValid()) {

            senderService.send(
                    chatId,
                    result.getErrorMessage(),
                    keyboardFactory.getKeyboard(chatId, message)
            );

            return;
        }

        currentCount = result.getValue();



        nitnemEntryService.saveNitnemEntry(nitnem, currentCount);

        log.info(
                "Nitnem Count Updated Successfully to : {}",
                currentCount
        );

        senderService.send(
                chatId,
                "Count Update Successfully. Total today count : "
                        + nitnemEntryService.fetchTodayCount(session.getNitnemName(), chatId),
                keyboardFactory.getMainMenuKeyboard()
        );

        sessionService.clear(
                chatId
        );
    }
}