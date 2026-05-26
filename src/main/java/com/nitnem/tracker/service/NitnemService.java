package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.NitnemEntry;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.NitnemUnit;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.repository.NitnemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
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

//            Integer nitnemTodayCount = nitnemEntryService.fetchTodayCount(nitnem.getName(), chatId);
//            Integer nitnemTillTodayCount = nitnemEntryService.fetchTillTodayCount(nitnem.getName(), chatId);
            Integer nitnemTodayCount = nitnemEntryService.fetchTodayCount(nitnem);
            Integer nitnemTillTodayCount = nitnemEntryService.fetchTillTodayCount(nitnem);

            StringBuilder nitnemTodayCountString = new StringBuilder();
            StringBuilder nitnemTillTodayCountString = new StringBuilder();

            if (nitnem.getUnitConversionFactor() > 1) {
                int newNitnemTodayCount = nitnemTodayCount/nitnem.getUnitConversionFactor();
                int rem = nitnemTodayCount % nitnem.getUnitConversionFactor();
                nitnemTodayCountString.append(newNitnemTodayCount);
                if (rem != 0) {
                    nitnemTodayCountString.append(":").append(rem);
                }
                nitnemTodayCountString.append(" maala");
                log.info("Nitnem today count string formed: {}",nitnemTodayCountString);

                int newNitnemTillTodayCount = nitnemTillTodayCount/nitnem.getUnitConversionFactor();
                rem = nitnemTillTodayCount % nitnem.getUnitConversionFactor();
                nitnemTillTodayCountString.append(newNitnemTillTodayCount);
                if (rem != 0){
                    nitnemTillTodayCountString.append(":").append(rem);
                }
                nitnemTillTodayCountString.append(" maala");
                log.info("Nitnem Till today count string formed: {}",nitnemTillTodayCountString);

            } else {
                nitnemTodayCountString.append(nitnemTodayCount);
                nitnemTillTodayCountString.append(nitnemTillTodayCount);
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
        Optional<Nitnem> nitnem = findByUserTelegramChatIdAndName(chatId, nitnemName);

        if (nitnem.isEmpty()) {
            return -1;
        }

        if (nitnemEntryService.deleteNitnemEntries(nitnem.get()) >= 0) {
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

    public Optional<Nitnem> findByUserTelegramChatIdAndName(Long chatId, String nitnemName) {
        return nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);
    }

    public boolean nitnemExists(
            Long chatId,
            String nitnemName
    ) {

        Optional<Nitnem> nitnem = nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);
        return nitnem.isPresent();
    }
}
