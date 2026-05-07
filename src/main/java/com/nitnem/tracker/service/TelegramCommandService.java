package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import com.nitnem.tracker.repository.NitnemRepository;
import com.nitnem.tracker.state.handler.StateHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramCommandService {

    private final TelegramSenderService telegramSenderService;

    private final UserSessionService sessionService;
    private final List<StateHandler> stateHandlerList;
    private Map<UserState, StateHandler> stateHandlerMap;
    private final NitnemRepository nitnemRepository;

    @PostConstruct
    public void setup() {
        stateHandlerMap = stateHandlerList.stream().collect(Collectors.toMap(StateHandler::getSupportedState, Function.identity()));
    }

    public void processMessage(Long chatId, String message)
            throws Exception {

        if (handleCurrentSession(chatId, message)) return;

        String text = handleCommand(chatId, message);

        telegramSenderService.send(chatId, text);
    }

    private @NonNull String handleCommand(Long chatId, String message) {
        return switch (message) {

            case "/start" -> """
                    Welcome to Nitnem Tracker 🙏
                    
                    Commands:
                    /create
                    /list
                    /update
                    /delete
                    """;

            case "/create" -> {
                sessionService.setState(chatId, UserState.WAITING_FOR_CREATE_NITNEM_NAME);
                yield "Enter nitnem name"; // 'yield' returns the value from the block
            }

            case "/list" -> nitnemRepository.findByUserTelegramChatId(chatId).
                    stream()
                    .map(nitnem ->
                            """
                            Name: %s
                            Target: %d
                            Duration: %d days
                            """
                                    .formatted(
                                            nitnem.getName(),
                                            nitnem.getTargetCount(),
                                            nitnem.getDurationDays()
                                    )
                    )
                    .collect(Collectors.joining("\n"));

            case "/update" -> {
                sessionService.setState(chatId, UserState.WAITING_FOR_UPDATE_NITNEM_NAME);
                yield "Enter Nitnem Name to Update";
            }

            case "/delete" -> {
                sessionService.setState(chatId, UserState.WAITING_FOR_DELETE_NITNEM_NAME);
                yield "Enter Nitnem Name to delete";
            }

            default -> "Unknown command";
        };
    }

    private boolean handleCurrentSession(Long chatId, String message) throws Exception {
        UserSession currentSession =
                sessionService.getSession(chatId);

        if (!currentSession.isNew()) {

            StateHandler handler =
                    stateHandlerMap.get(currentSession.getState());

            if (handler != null) {

                handler.handle(currentSession, chatId, message);

                return true;
            }
        }
        return false;
    }
}
