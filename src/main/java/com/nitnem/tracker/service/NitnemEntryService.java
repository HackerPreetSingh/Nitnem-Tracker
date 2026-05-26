package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.NitnemEntry;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.repository.NitnemEntryRepository;
import com.nitnem.tracker.repository.NitnemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NitnemEntryService {

    private final NitnemEntryRepository nitnemEntryRepository;

    private final NitnemRepository nitnemRepository;

    public void saveNitnemEntry(Nitnem nitnem, Integer todayCount) {
        NitnemEntry nitnemEntry = NitnemEntry.builder()
                .date(LocalDate.now())
                .completedCount(todayCount)
                .nitnem(nitnem)
                .doneAt(LocalDate.now())
                .build();

        nitnemEntryRepository.save(nitnemEntry);
    }

    public Integer fetchTodayCount(String nitnemName, Long chatId) {
        Optional<Nitnem> nitnemOpt = nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);

        return nitnemOpt.isPresent()
                ? nitnemEntryRepository.getTodayCompletedCount(nitnemOpt.get().getId(), LocalDate.now())
                :-1;
    }

    public Integer fetchTillTodayCount(String nitnemName, Long chatId) {
        Optional<Nitnem> nitnemOpt = nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);
        return nitnemOpt.isPresent()?nitnemEntryRepository.getTillTodayCompletedCount(nitnemOpt.get().getId()):-1;
    }

    public Integer deleteNitnemEntries(Nitnem nitnem) {
        return nitnemEntryRepository.deleteByNitnem(nitnem);
    }
}
