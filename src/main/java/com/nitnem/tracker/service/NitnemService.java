package com.nitnem.tracker.service;

import com.nitnem.tracker.entity.Nitnem;
import com.nitnem.tracker.entity.User;
import com.nitnem.tracker.model.UserSession;
import com.nitnem.tracker.repository.NitnemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NitnemService {

    private final NitnemRepository nitnemRepository;

    private final UserService userService;

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

            sb.append(i + 1)
                    .append(". ")
                    .append("Name: ")
                    .append(nitnem.getName())
                    .append("\n")
                    .append("Target: ")
                    .append(nitnem.getTargetCount())
                    .append("\n")
                    .append("Duration: ")
                    .append(nitnem.getDurationDays())
                    .append(" days\n\n");
        }

        return sb.toString();
    }

    @Transactional
    public Integer deleteNitnem(Long chatId, String nitnemName) {
        return nitnemRepository.deleteByUserTelegramChatIdAndName(chatId, nitnemName);
    }

    public List<String> getNitnemNameList(Long chatId) {
        List<Nitnem> nitnems =
                nitnemRepository.findByUserTelegramChatId(chatId);

        if (nitnems.isEmpty()) {
            return List.of("No Nitnems Found");
        }

        return nitnems.stream().map(Nitnem::getName).toList();
    }

    public Nitnem getNitnem(String nitnemName) {
        return nitnemRepository.findByName(nitnemName);
    }

    public void saveNitnem(UserSession session, Long chatId) {
        User user  = userService.findOrCreateNewUser(chatId);

        Nitnem nitnem = Nitnem.builder()
                .name(session.getNitnemName())
                .targetCount(session.getTargetCount())
                .durationDays(session.getDurationDays())
                .startDate(LocalDate.now())
                .active(true)
                .user(user)
                .build();

        nitnemRepository.save(nitnem);
    }

    public Nitnem findByUserTelegramChatIdAndName(Long chatId, String nitnemName) {
        return nitnemRepository.findByUserTelegramChatIdAndName(chatId, nitnemName);
    }
}
