package com.nitnem.tracker.utils;

import com.nitnem.tracker.service.NitnemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramKeyboardFactory {

    private final NitnemService nitnemService;

    public ReplyKeyboardMarkup getMainMenuKeyboard() {

        KeyboardRow row1 = new KeyboardRow();
        row1.add("/create");
        row1.add("/list");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("/update");
        row2.add("/delete");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("/exit");

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
    public ReplyKeyboardMarkup getModifyMenuKeyboard(Long chatId) {

        List<String> nitnemNames =
                nitnemService.getNitnemNameList(chatId);

        List<KeyboardRow> rows =
                new ArrayList<>();

        for (int i = 0; i < nitnemNames.size(); i += 2) {

            KeyboardRow row =
                    new KeyboardRow();

            row.add(nitnemNames.get(i));

            if (i + 1 < nitnemNames.size()) {
                row.add(nitnemNames.get(i + 1));
            }

            rows.add(row);
        }

        KeyboardRow exitRow = new KeyboardRow();
        exitRow.add("/exit");

        rows.add(exitRow);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
    public ReplyKeyboardMarkup getExitMenuKeyboard() {

        KeyboardRow row = new KeyboardRow();
        row.add("/exit");

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
//    public ReplyKeyboardMarkup getCountMenuKeyboard() {
//
//        KeyboardRow row1 = new KeyboardRow();
//        row1.add("/Total today's count");
//        KeyboardRow row2 = new KeyboardRow();
//        row2.add("/total count");
//
//        return ReplyKeyboardMarkup.builder()
//                .keyboard(List.of(row1, row2))
//                .resizeKeyboard(true)
//                .oneTimeKeyboard(false)
//                .build();
//    }


    public ReplyKeyboard getKeyboard(Long chatId, String message) {

        return switch (message) {

            case "/start", "/exit", "/list" ->
                    getMainMenuKeyboard();
            case "/update", "/delete" -> getModifyMenuKeyboard(chatId);
//            case "/list" -> getCountMenuKeyboard();
            default -> getExitMenuKeyboard();
        };
    }
}