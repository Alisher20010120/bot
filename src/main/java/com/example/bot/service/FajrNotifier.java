package com.example.bot.service;

import com.example.bot.bot.MyTelegramBot;
import com.example.bot.entity.UserEntity;
import com.example.bot.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class FajrNotifier {

    private final UserEntityRepository userRepository;
    private final MyTelegramBot myBot;
    private final PrayerTimeService prayerTimeService;

    @Autowired
    public FajrNotifier(UserEntityRepository userRepository,
                        @Lazy MyTelegramBot myBot, 
                        PrayerTimeService prayerTimeService) {
        this.userRepository = userRepository;
        this.myBot = myBot;
        this.prayerTimeService = prayerTimeService;
    }

    @Scheduled(cron = "0 55 14 * * *", zone = "Asia/Tashkent")
    public void sendDailyPrayerTimes() {
        List<UserEntity> activeUsers = userRepository.findAllByNotificationsEnabledTrue();

        for (UserEntity user : activeUsers) {
            try {
                String city = user.getCity();
                
                if (city == null || city.equals("aniqlanmagan")) {
                    continue;
                }

                String prayerTimesMessage = prayerTimeService.getPrayerTimes(city);

                SendMessage sm = new SendMessage();
                sm.setChatId(user.getChatId().toString());
                sm.setText(prayerTimesMessage);
                
                myBot.execute(sm);

            } catch (Exception e) {
                System.err.println("Xabarnoma yuborishda xatolik (User: " + user.getChatId() + "): " + e.getMessage());
            }
        }
    }
}