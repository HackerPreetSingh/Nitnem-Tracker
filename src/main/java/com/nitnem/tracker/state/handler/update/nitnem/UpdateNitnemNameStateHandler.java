package com.nitnem.tracker.state.handler.update.nitnem;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.NitnemUnit;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateNitnemNameStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    private final ValidationService validationService;

    private final NitnemService nitnemService;

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

        Optional<Nitnem> nitnemOpt = nitnemService.findByUserTelegramChatIdAndName(chatId, nitnemName);

        if (nitnemOpt.isEmpty()) {

            senderService.send(
                    chatId,
                    "Nitnem not found",
                    keyboardFactory.getKeyboard(
                            chatId,
                            "/update"
                    )
            );

            return;
        }

        Nitnem nitnem = nitnemOpt.get();

        session.setNitnem(nitnem);

//        session.setNitnemId(nitnem.getId());
//        session.setNitnemName(nitnemName);

        if (nitnem.getUnitConversionFactor() > 1) {
            senderService.send(
                    chatId,
                    "Enter Nitnem Unit: Maala/Raw",
                    keyboardFactory.getKeyboard(chatId, "/unit")
            );

            session.setState(UserState.WAITING_FOR_UPDATE_NITNEM_UNIT);
        } else {
            senderService.send(
                    chatId,
                    "Enter Nitnem Count:",
                    keyboardFactory.getExitMenuKeyboard()
            );

            session.setNitnemUnit(NitnemUnit.RAW);
            session.setState(UserState.WAITING_FOR_UPDATE_NITNEM_COUNT);
        }

        session.setLastProcessedMessage(message);
        session.setLastProcessedAt(LocalDateTime.now());

        sessionService.setSession(
                chatId,
                session
        );
    }
}