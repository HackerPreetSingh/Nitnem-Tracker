package com.nitnem.tracker.state.handler.update.nitnem;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.model.NitnemUnit;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.model.ValidationResult;
import com.nitnem.tracker.service.*;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateCountStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    private final NitnemEntryService nitnemEntryService;

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
                        "Today Count",
                        1,
                        200000
                );

        if (!result.isValid()) {

            senderService.send(
                    chatId,
                    result.getErrorMessage(),
                    keyboardFactory.getKeyboard(chatId, message)
            );

            return;
        }

        Nitnem nitnem = session.getNitnem();

        int currentCount = result.getValue();
        if (session.getNitnemUnit() == NitnemUnit.MAALA) {
            currentCount *= nitnem.getUnitConversionFactor();
            log.info("Nitnem Unit MAALA: Current Count: {}", currentCount);
        }

//        Optional<Nitnem> nitnemOpt = nitnemService
//                .findByUserTelegramChatIdAndName(
//                        chatId,
//                        session.getNitnemName()
//                );

//        Nitnem nitnem = nitnemOpt.get();

        int thresholdCount = nitnem.getTargetCount();

        ValidationResult<Integer> result2 =
                validationService.validateCurrentCount(
                        currentCount,
                        thresholdCount
                );

        if (!result2.isValid()) {

            senderService.send(
                    chatId,
                    result2.getErrorMessage(),
                    keyboardFactory.getKeyboard(chatId, message)
            );

            return;
        }

        currentCount = result2.getValue();
        log.info("Nitnem Unit MAALA: Current Count before saving: {}", currentCount);

        nitnemEntryService.saveNitnemEntry(nitnem, currentCount);

        log.info(
                "Nitnem Count Updated Successfully to : {}",
                currentCount
        );

//        senderService.send(
//                chatId,
//                "Count Update Successfully. Total today count : "
//                        + nitnemEntryService.fetchTodayCount(session.getNitnemName(), chatId),
//                keyboardFactory.getMainMenuKeyboard()
//        );
        senderService.send(
                chatId,
                "Count Update Successfully. Total today count : "
                        + nitnemEntryService.fetchTodayCount(nitnem),
                keyboardFactory.getMainMenuKeyboard()
        );


        session.setLastProcessedMessage(message);
        session.setLastProcessedAt(LocalDateTime.now());

        sessionService.clear(
                chatId
        );
    }
}