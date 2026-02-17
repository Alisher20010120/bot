package com.example.bot.service;

import com.example.bot.bot.MyTelegramBot;
import com.example.bot.entity.UserEntity;
import com.example.bot.repository.UserEntityRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FajrNotifier {

    private final UserEntityRepository userRepository;
    private final MyTelegramBot myBot;

    public FajrNotifier(UserEntityRepository userRepository, @Lazy MyTelegramBot myBot) {
        this.userRepository = userRepository;
        this.myBot = myBot;
    }

    @Scheduled(cron = "0 30 14 * * *", zone = "Asia/Tashkent")
    public void sendMorningNotification() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String messageText = "🌙 Assalomu alaykum!\n\n" +
                             "Bugun: " + today + "\n" +
                             "Bomdod vaqti bo'ldi. Namoz uyqudan afzaldir! 🤲";

        List<UserEntity> enabledUsers = userRepository.findAllByNotificationsEnabledTrue();

        for (UserEntity user : enabledUsers) {
            try {
                SendMessage sm = new SendMessage();
                sm.setChatId(user.getChatId().toString());
                sm.setText(messageText);
                
                myBot.execute(sm);
            } catch (Exception e) {
                System.err.println("Xabarnoma yuborilmadi ChatId: " + user.getChatId());
            }
        }
        System.out.println("Bomdod bildirishnomasi " + enabledUsers.size() + " ta userga yuborildi.");
    }
}