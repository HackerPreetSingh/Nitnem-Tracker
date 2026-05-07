package com.nitnem.tracker.state.handler.update.nitnem;

import com.nitnem.tracker.entity.NitnemEntry;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.repository.NitnemEntryRepository;
import com.nitnem.tracker.service.NitnemEntryService;
import com.nitnem.tracker.service.NitnemService;
import com.nitnem.tracker.service.TelegramSenderService;
import com.nitnem.tracker.service.UserSessionService;
import com.nitnem.tracker.state.handler.StateHandler;
import com.nitnem.tracker.utils.TelegramKeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateCountStateHandler
        implements StateHandler {

    private final TelegramSenderService senderService;

    private final UserSessionService sessionService;

    private final TelegramKeyboardFactory keyboardFactory;

    private final NitnemEntryService nitnemEntryService;

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

        log.info(
                "Nitnem Count Updated Successfully to : {}",
                message
        );

        nitnemEntryService.saveNitnemEntry(session, message);

        senderService.send(
                chatId,
                "Count Update Successfully. Total today count : "
                        + nitnemEntryService.fetchTodayCount(session, chatId),
                keyboardFactory.getMainMenuKeyboard()
        );

        sessionService.clear(
                chatId
        );
    }
}