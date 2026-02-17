package com.example.bot.bot;

import com.example.bot.command.BotCommand;
import com.example.bot.service.KeyboardService;
import com.example.bot.service.PrayerTimeService;
import com.example.bot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final Map<String, BotCommand> commands;
    private final UserService userService;
    private final KeyboardService keyboardService;
    private final PrayerTimeService prayerTimeService;

    public MyTelegramBot(@Value("${bot.token}") String botToken,
                         List<BotCommand> commandList,
                         UserService userService,
                         KeyboardService keyboardService, PrayerTimeService prayerTimeService) {
        super(botToken);
        this.userService = userService;
        this.keyboardService = keyboardService;
        this.commands = commandList.stream()
                .collect(Collectors.toMap(BotCommand::getCommandName, Function.identity()));
        this.prayerTimeService = prayerTimeService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasLocation()) {
            handleLocation(update);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();


            if (text.equals("/start")) {
                executeMessage(commands.get(text).execute(update));
            }
            else if (isCity(text)) {
                boolean firstTime = userService.isCityNotSet(chatId);

                userService.updateUserCity(chatId, text);

                if (firstTime) {
                    SendMessage response = new SendMessage();
                    response.setChatId(chatId.toString());
                    response.setText("✅ Shahar saqlandi: " + text + "\n\nSizga har kuni namoz vaqtlarini avtomatik yuborib turishimizni xohlaysizmi? 🔔");
                    response.setReplyMarkup(keyboardService.getNotificationApprovalKeyboard());
                    executeMessage(response);
                } else {
                    SendMessage response = new SendMessage();
                    response.setChatId(chatId.toString());
                    response.setText("✅ Shahar o'zgartirildi: " + text);
                    response.setReplyMarkup(keyboardService.getMainMenuKeyboard());
                    executeMessage(response);

                    sendAnswer(chatId, prayerTimeService.getPrayerTimes(text));
                }
            }
            else {
                switch (text) {
                    case "📅 Namoz vaqtlari":
                        String userCity = userService.getUserCity(chatId);
                        if (userCity == null || userCity.equals("aniqlanmagan")) {
                            sendAnswer(chatId, "Iltimos, avval sozlamalardan shaharni tanlang! 📍");
                        } else {
                            sendPrayerTimesResponse(chatId, userCity);
                        }
                        break;
                    case "🌙 Ro'za vaqtlari":
                        sendAnswer(chatId, "Tez orada ishga tushadi");
                        break;
                    case "📿 Tasbeh":
                        int current = userService.getTasbehCount(chatId);
                        int full = userService.getTasbehFullCount(chatId);

                        String dhikr;
                        if (current <= 33 && current != 0) dhikr = "Subhanallah";
                        else if (current <= 66 && current != 0) dhikr = "Alhamdulillah";
                        else if (current <= 99 && current != 0) dhikr = "Allohu Akbar";
                        else dhikr = "Subhanallah";

                        String tasbehText = String.format("Allohni zikr qiling 🤲\n(Umumiy tugatilgan: %d marta)", full);

                        SendMessage tasbehMsg = new SendMessage();
                        tasbehMsg.setChatId(chatId.toString());
                        tasbehMsg.setText(tasbehText);
                        tasbehMsg.setReplyMarkup(keyboardService.getTasbehKeyboard(current, dhikr));
                        executeMessage(tasbehMsg);
                        break;
                    case "🤲 Kundalik duolar":
                        sendAnswer(chatId, "🤲 Kundalik duolar to'plami tez orada qo'shiladi.");
                        break;
                    case "🕌 Yaqindagi masjidlar":
                        SendMessage locationRequest = new SendMessage();
                        locationRequest.setChatId(chatId.toString());
                        locationRequest.setText("Eng yaqin masjidlarni topish uchun lokatsiyangizni yuboring 👇");
                        locationRequest.setReplyMarkup(keyboardService.getLocationRequestKeyboard());
                        executeMessage(locationRequest);
                        break;
                    case "💰 Zakot hisoblagich":
                        sendAnswer(chatId, "Tez orada ishga tushadi");
                        break;
                    case "⚙️ Sozlamalar":
                        SendMessage settingsMsg = new SendMessage();
                        settingsMsg.setChatId(chatId.toString());
                        settingsMsg.setText("Sozlamalar bo'limi. Nimani o'zgartirmoqchisiz? 👇");
                        settingsMsg.setReplyMarkup(keyboardService.getSettingsKeyboard());
                        executeMessage(settingsMsg);
                        break;

                    case "📍 Shaharni o'zgartirish":
                        String currentCity = userService.getUserCity(chatId);

                        SendMessage cityMsg = new SendMessage();
                        cityMsg.setChatId(chatId.toString());
                        cityMsg.setText("Sizning hozirgi shahringiz: **" + currentCity + "**\n\n" +
                                "Yangi shahringizni tanlang: 👇");

                        cityMsg.setReplyMarkup(keyboardService.getCityKeyboard());
                        executeMessage(cityMsg);
                        break;

                    case "🔔 Bildirishnomalar":
                        boolean isEnabled = userService.isNotificationEnabled(chatId);
                        String statusText = isEnabled ? "faol ✅" : "o'chirilgan ❌";

                        SendMessage notifyMsg = new SendMessage();
                        notifyMsg.setChatId(chatId.toString());
                        notifyMsg.setText("🔔 **Bildirishnomalar sozlamalari**\n\n" +
                                "Sizga bildirishnoma yuborish hozirda: **" + statusText + "**\n\n" +
                                "Holatni o'zgartirishni xohlaysizmi? 👇");

                        notifyMsg.setReplyMarkup(keyboardService.getNotificationSwitchKeyboard(isEnabled));
                        executeMessage(notifyMsg);
                        break;

                    case "⬅️ Orqaga":
                        SendMessage backMsg = new SendMessage();
                        backMsg.setChatId(chatId.toString());
                        backMsg.setText("Asosiy menyuga qaytdingiz.");
                        backMsg.setReplyMarkup(keyboardService.getMainMenuKeyboard());
                        executeMessage(backMsg);
                        break;
                }
            }
        }

        else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (callData.equals("notify_on") || callData.equals("notify_off")) {
                boolean newState = callData.equals("notify_on");
                userService.updateNotification(chatId, newState);

                String statusText = newState ? "faol ✅" : "o'chirilgan ❌";
                String newText = "🔔 **Bildirishnomalar sozlamalari**\n\n" +
                        "Sizga bildirishnoma yuborish hozirda: **" + statusText + "**\n\n" +
                        "Holatni o'zgartirishni xohlaysizmi? 👇";

                executeEditMessage(chatId, messageId, newText, keyboardService.getNotificationSwitchKeyboard(newState));
            }

            else if (callData.equals("notify_yes") || callData.equals("notify_no")) {
                userService.updateNotification(chatId, callData.equals("notify_yes"));

                SendMessage response = new SendMessage();
                response.setChatId(chatId.toString());
                response.setText("Tanlovingiz saqlandi! Asosiy menyu 👇");
                response.setReplyMarkup(keyboardService.getMainMenuKeyboard());
                executeMessage(response);
                String userCity = userService.getUserCity(chatId);
                String prayerTimes = prayerTimeService.getPrayerTimes(userCity);
                sendAnswer(chatId, prayerTimes);
            }
            else if (callData.equals("tasbeh_count")) {
                int[] counts = userService.incrementTasbeh(chatId);
                int current = counts[0];
                int full = counts[1];

                String dhikr;
                if (current > 0 && current <= 33) dhikr = "Subhanallah";
                else if (current > 33 && current <= 66) dhikr = "Alhamdulillah";
                else if (current > 66 && current <= 99) dhikr = "Allohu Akbar";
                else dhikr = "Subhanallah";

                String text = String.format("Allohni zikr qiling 🤲\n(Umumiy tugatilgan: %d marta)", full);

                executeEditMessage(chatId, messageId, text,
                        keyboardService.getTasbehKeyboard(current, dhikr));
            }

            // 4. Tasbehni nolga tushirish
            else if (callData.equals("tasbeh_reset")) {
                userService.resetTasbeh(chatId);
                int full = userService.getTasbehFullCount(chatId);

                String text = String.format("Allohni zikr qiling 🤲\n(Umumiy tugatilgan: %d marta)", full);

                executeEditMessage(chatId, messageId, text,
                        keyboardService.getTasbehKeyboard(0, "Subhanallah"));
            }
        }
    }

    private void sendAnswer(Long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText(text);
        executeMessage(msg);
    }

    private boolean isCity(String text) {
        return List.of(
                "Toshkent", "Buxoro", "Samarqand", "Navoiy", "Jizzax", "Sirdaryo",
                "Qashqadaryo(Shahrisabz)", "Surxondaryo", "Xorazm(Xiva)",
                "Qoraqalpog'iston(Nukus)", "Farg'ona", "Andijon", "Namangan"
        ).contains(text);
    }

    private void executeMessage(SendMessage message) {
        try {
            if (message != null) execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "SizningBot_Username";
    }

    private void executeEditMessage(Long chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        editMessage.setReplyMarkup(markup);
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("message is not modified")) {
            } else {
                e.printStackTrace();
            }
        }
    }

    private void handleLocation(Update update) {
        Long chatId = update.getMessage().getChatId();
        Double lat = update.getMessage().getLocation().getLatitude();
        Double lon = update.getMessage().getLocation().getLongitude();

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("📍 Lokatsiyangiz qabul qilindi.\n\nAtrofingizdagi eng yaqin masjidlarni quyidagi xaritalar orqali ko'rishingiz mumkin: 👇");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton googleBtn = new InlineKeyboardButton();
        googleBtn.setText("🕌 Google Maps (Masjidlar)");
        googleBtn.setUrl("https://www.google.com/maps/search/masjid/@" + lat + "," + lon + ",15z");

        InlineKeyboardButton yandexBtn = new InlineKeyboardButton();
        yandexBtn.setText("🗺 Yandex Maps (Masjidlar)");
        yandexBtn.setUrl("https://yandex.com/maps/?ll=" + lon + "," + lat + "&mode=search&sll=" + lon + "," + lat + "&text=masjid&z=14");

        InlineKeyboardButton navBtn = new InlineKeyboardButton();
        navBtn.setText("🚗 Eng yaqiniga yo'l chizish");
        navBtn.setUrl("https://www.google.com/maps/dir/?api=1&destination=masjid&travelmode=driving");

        rows.add(List.of(googleBtn));
        rows.add(List.of(yandexBtn));
        rows.add(List.of(navBtn));

        markup.setKeyboard(rows);
        response.setReplyMarkup(markup);

        executeMessage(response);
    }

    private void sendPrayerTimesResponse(Long chatId, String city) {
        String timesText = prayerTimeService.getPrayerTimes(city);

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(timesText);
        response.setReplyMarkup(keyboardService.getMainMenuKeyboard());
        executeMessage(response);
    }
}