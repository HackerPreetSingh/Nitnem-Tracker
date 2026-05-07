package com.nitnem.tracker.repository;

import com.nitnem.tracker.entity.Nitnem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NitnemRepository extends JpaRepository<Nitnem, Long> {

    List<Nitnem> findByUserTelegramChatId(
            Long telegramChatId
    );
}
