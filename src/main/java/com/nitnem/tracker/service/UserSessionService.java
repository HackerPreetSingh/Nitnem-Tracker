package com.nitnem.tracker.service;

import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.model.UserState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {

    private final Map<Long, UserSession> sessions =
            new ConcurrentHashMap<>();

    public UserSession getSession(Long chatId) {

        return sessions.getOrDefault(
                chatId,
                UserSession.builder().state(UserState.IDLE).build()
        );
    }

    public void setState(
            Long chatId,
            UserState state
    ) {
        UserSession session = getSession(chatId);
        session.setState(state);
        sessions.put(chatId, session);
    }

    public void setSession(
            Long chatId,
            UserSession session
    ) {

        sessions.put(chatId, session);
    }

    public void clear(Long chatId) {

        sessions.remove(chatId);
    }
}