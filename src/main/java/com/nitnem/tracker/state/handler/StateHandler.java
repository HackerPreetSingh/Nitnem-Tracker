package com.nitnem.tracker.state.handler;

import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;

public interface StateHandler {

    UserState getSupportedState();

    void handle(
            UserSession session,
            Long chatId,
            String message
    ) throws Exception;
}
