package com.nitnem.tracker.repository;

import com.nitnem.tracker.entity.Nitnem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NitnemRepository extends JpaRepository<Nitnem, Long> {

    List<Nitnem> findByUserTelegramChatId(
            Long telegramChatId
    );

    @Modifying
    Integer deleteByUserTelegramChatIdAndName(
            Long telegramChatId,
            String name

    );

    Nitnem findByName(String nitnemName);

    Nitnem findByUserTelegramChatIdAndName(Long TelegramChatId, String name);
}
