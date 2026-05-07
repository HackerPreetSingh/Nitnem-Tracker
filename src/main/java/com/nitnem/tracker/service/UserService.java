package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;

    public @NonNull User findOrCreateNewUser(Long chatId) {
        return userRepository.findByTelegramChatId(chatId)
                .orElseGet(() ->
                {
                    User u = new User();
                    u.setTelegramChatId(chatId);

                    u.setCreatedAt(LocalDateTime.now());

                    return userRepository.save(u);
                });
    }
}
