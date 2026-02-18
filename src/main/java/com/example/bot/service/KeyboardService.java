package com.example.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
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

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Toshkent");
        row1.add("Buxoro");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Samarqand");
        row2.add("Navoiy");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Jizzax");
        row3.add("Sirdaryo");

        KeyboardRow row4 = new KeyboardRow();
        row4.add("Qashqadaryo(Shahrisabz)");
        row4.add("Surxondaryo");

        KeyboardRow row5 = new KeyboardRow();
        row5.add("Xorazm(Xiva)");
        row5.add("Qoraqalpog'iston(Nukus)");

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


    public InlineKeyboardMarkup getNotificationApprovalKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton yesBtn = new InlineKeyboardButton();
        yesBtn.setText("Ha, albatta ✅");
        yesBtn.setCallbackData("notify_yes");

        InlineKeyboardButton noBtn = new InlineKeyboardButton();
        noBtn.setText("Yo'q, rahmat ❌");
        noBtn.setCallbackData("notify_no");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(yesBtn);
        row.add(noBtn);

        rowsInline.add(row);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📅 Namoz vaqtlari");
        row1.add("🌙 Ro'za vaqtlari");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📿 Tasbeh");
        row2.add("🤲 Kundalik duolar");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("🕌 Yaqindagi masjidlar");
        row3.add("💰 Zakot hisoblagich");

        KeyboardRow row4 = new KeyboardRow();
        row4.add("⚙️ Sozlamalar");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        markup.setKeyboard(keyboard);
        return markup;
    }

    public ReplyKeyboardMarkup getSettingsKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📍 Shaharni o'zgartirish");
        row1.add("🔔 Bildirishnomalar");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("⬅️ Orqaga");

        keyboard.add(row1);
        keyboard.add(row2);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public InlineKeyboardMarkup getNotificationSwitchKeyboard(boolean isEnabled) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();

        if (isEnabled) {
            button.setText("O'chirish ❌");
            button.setCallbackData("notify_off");
        } else {
            button.setText("Yoqish ✅");
            button.setCallbackData("notify_on");
        }

        row.add(button);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }

    public InlineKeyboardMarkup getTasbehKeyboard(int count, String dhikr) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton countBtn = new InlineKeyboardButton();
        countBtn.setText("📿 " + dhikr + ": " + count);
        countBtn.setCallbackData("tasbeh_count");

        InlineKeyboardButton resetBtn = new InlineKeyboardButton();
        resetBtn.setText("🔄 Nolga tushirish");
        resetBtn.setCallbackData("tasbeh_reset");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(countBtn);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(resetBtn);

        rows.add(row1);
        rows.add(row2);
        markup.setKeyboard(rows);
        return markup;
    }

    public ReplyKeyboardMarkup getLocationRequestKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("📍 Lokatsiyamni yuborish");
        locationButton.setRequestLocation(true);
        row.add(locationButton);
        keyboard.add(row);

        KeyboardRow rowBack = new KeyboardRow();
        rowBack.add("⬅️ Orqaga");
        keyboard.add(rowBack);

        markup.setKeyboard(keyboard);
        return markup;
    }

    public ReplyKeyboardMarkup getDuaMenu() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        List<KeyboardRow> rows = new ArrayList<>();

        // 1-qator
        KeyboardRow row1 = new KeyboardRow();
        row1.add("☀️ Tong va Kech");
        row1.add("🍽 Ovqatlanish");

        // 2-qator
        KeyboardRow row2 = new KeyboardRow();
        row2.add("🏠 Uy va Ko'cha");
        row2.add("💤 Uyqu duolari");

        // 3-qator
        KeyboardRow row3 = new KeyboardRow();
        row3.add("🕌 Masjid va Namoz");
        row3.add("🤲 Shifo va Himoya");

        // 4-qator
        KeyboardRow row4 = new KeyboardRow();
        row4.add("⬅️ Orqaga");

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        markup.setKeyboard(rows);
        return markup;
    }
}