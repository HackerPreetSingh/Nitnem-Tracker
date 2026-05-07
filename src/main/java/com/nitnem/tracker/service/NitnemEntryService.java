package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.NitnemEntry;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.repository.NitnemEntryRepository;
import com.nitnem.tracker.repository.NitnemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NitnemEntryService {

    private final NitnemEntryRepository nitnemEntryRepository;

    private final NitnemService nitnemService;

    public void saveNitnemEntry(UserSession session, String message) {
        NitnemEntry nitnemEntry = NitnemEntry.builder()
                .date(LocalDate.now())
                .completedCount(Integer.valueOf(message))
                .nitnem(nitnemService.getNitnem(session.getNitnemName()))
                .doneAt(LocalDate.now())
                .build();

        nitnemEntryRepository.save(nitnemEntry);
    }


    public Integer fetchTodayCount(UserSession session, Long chatId) {
        Nitnem nitnem = nitnemService.findByUserTelegramChatIdAndName(chatId, session.getNitnemName());
        return nitnemEntryRepository.getTodayCompletedCount(nitnem.getId(), LocalDate.now());
    }
}
