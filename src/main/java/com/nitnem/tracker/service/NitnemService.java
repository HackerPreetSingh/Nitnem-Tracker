package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.NitnemEntry;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.NitnemUnit;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.repository.NitnemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NitnemService {

    private final NitnemRepository nitnemRepository;

    private final UserService userService;

    private final NitnemEntryService nitnemEntryService;

    public String getNitnemNames(Long chatId) {

        List<Nitnem> nitnems =
                nitnemRepository.findByUserTelegramChatId(chatId);

        if (nitnems.isEmpty()) {
            return "No Nitnems Found";
        }

        StringBuilder sb =
                new StringBuilder();

        for (int i = 0; i < nitnems.size(); i++) {

            sb.append(i + 1)
                    .append(". ")
                    .append(nitnems.get(i).getName())
                    .append("\n");
        }

        return sb.toString();
    }

    public String getNitnemDetails(Long chatId) {

        List<Nitnem> nitnems =
                nitnemRepository.findByUserTelegramChatId(chatId);

        if (nitnems.isEmpty()) {
            return "No Nitnems Found";
        }

        StringBuilder sb =
                new StringBuilder();

        for (int i = 0; i < nitnems.size(); i++) {

            Nitnem nitnem =
                    nitnems.get(i);

            Integer nitnemTodayCount = nitnemEntryService.fetchTodayCount(nitnem.getName(), chatId);
            Integer nitnemTillTodayCount = nitnemEntryService.fetchTillTodayCount(nitnem.getName(), chatId);

            StringBuilder nitnemTodayCountString = new StringBuilder();
            StringBuilder nitnemTillTodayCountString = new StringBuilder();

            if (nitnem.getUnitConversionFactor() != 1) {
                nitnemTodayCount /= nitnem.getUnitConversionFactor();
                int rem = nitnemTodayCount % nitnem.getUnitConversionFactor();
                nitnemTodayCountString.append(nitnemTodayCount).append(".").append(rem).append("maala");

                nitnemTillTodayCount /= nitnem.getUnitConversionFactor();
                rem = nitnemTillTodayCount % nitnem.getUnitConversionFactor();
                nitnemTillTodayCountString.append(nitnemTillTodayCount).append(".").append(rem).append("maala");

            } else {
                nitnemTodayCountString.append(nitnemTodayCount).append("maala");
                nitnemTillTodayCountString.append(nitnemTillTodayCount).append("maala");
            }

            sb.append(i + 1)
                    .append(". ")
                    .append("Name: ")
                    .append(nitnem.getName())
                    .append("\n")
                    .append("Target: ")
                    .append(nitnem.getTargetCount())
                    .append("\n")
                    .append("Today's Count: ")
                    .append(nitnemTodayCountString)
                    .append("\n")
                    .append("Total count till date: ")
                    .append(nitnemTillTodayCountString)
                    .append("\n")
                    .append("Duration: ")
                    .append(nitnem.getDurationDays())
                    .append(" days\n\n");
        }

        return sb.toString();
    }

    @Transactional
    public Integer deleteNitnem(Long chatId, String nitnemName) {
        Nitnem nitnem = findByUserTelegramChatIdAndName(chatId, nitnemName);

        if (nitnemEntryService.deleteNitnemEntries(nitnem) >= 0) {
            return nitnemRepository.deleteByUserTelegramChatIdAndName(chatId, nitnemName);
        } else  {
            return -1;
        }
    }

    public List<String> getNitnemNameList(Long chatId) {
        List<Nitnem> nitnems =
                nitnemRepository.findByUserTelegramChatId(chatId);

        if (nitnems.isEmpty()) {
            return List.of("No Nitnems Found");
        }

        return nitnems.stream().map(Nitnem::getName).toList();
    }

    public void saveNitnem(UserSession session, Long chatId) {
        User user  = userService.findOrCreateNewUser(chatId);

        int targetCount = session.getTargetCount();
        int unitConversionFactor = 1;
        if (session.getNitnemUnit().equals(NitnemUnit.MAALA)) {
            unitConversionFactor = 110;
            targetCount = targetCount < 1500 ? targetCount : targetCount/unitConversionFactor + 1;
        }

        Nitnem nitnem = Nitnem.builder()
                .name(session.getNitnemName())
                .targetCount(targetCount)
                .unitConversionFactor(unitConversionFactor)
                .durationDays(session.getDurationDays())
                .startDate(LocalDate.now())
                .active(true)
                .user(user)
                .build();

        nitnemRepository.save(nitnem);
    }

    public Nitnem findByUserTelegramChatIdAndName(Long chatId, String nitnemName) {
        Optional<Nitnem> nitnem = nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);
        return nitnem.orElse(null);
    }

    public boolean nitnemExists(
            Long chatId,
            String nitnemName
    ) {

        Optional<Nitnem> nitnem = nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);
        return nitnem.isPresent();
    }
}
