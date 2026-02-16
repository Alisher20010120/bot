package com.example.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    public ReplyKeyboardMarkup getCityKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // 1-qator
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Toshkent");
        row1.add("Buxoro");

        // 2-qator
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Samarqand");
        row2.add("Navoiy");

        // 3-qator
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Jizzax");
        row3.add("Sirdaryo");

        // 4-qator
        KeyboardRow row4 = new KeyboardRow();
        row4.add("Qashqadaryo(Shahrisabz)");
        row4.add("Surxondaryo");

        // 5-qator
        KeyboardRow row5 = new KeyboardRow();
        row5.add("Xorazm(Xiva)");
        row5.add("Qoraqalpog'iston(Nukus)");

        // 6-qator
        KeyboardRow row6 = new KeyboardRow();
        row6.add("Farg'ona");
        row6.add("Andijon");

        KeyboardRow row7 = new KeyboardRow();
        row7.add("Namangan");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);
        keyboard.add(row7);

        markup.setKeyboard(keyboard);
        return markup;
    }
}