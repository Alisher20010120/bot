package com.example.bot.service;

import com.example.bot.entity.UserEntity;
import com.example.bot.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userRepository;

    public void saveUser(User telegramUser, Long chatId) {
        if (!userRepository.existsById(chatId)) {
            UserEntity user = UserEntity.builder()
                    .chatId(chatId)
                    .firstName(telegramUser.getFirstName())
                    .username(telegramUser.getUserName())
                    .registeredAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
    }

    public void updateUserCity(Long chatId, String city) {
        userRepository.findById(chatId).ifPresentOrElse(user -> {
            user.setCity(city);
            userRepository.save(user);
        }, () -> {
            UserEntity newUser = UserEntity.builder()
                    .chatId(chatId)
                    .city(city)
                    .registeredAt(LocalDateTime.now())
                    .build();
            userRepository.save(newUser);
        });
    }

    public void updateNotification(Long chatId, boolean status) {
        userRepository.findById(chatId).ifPresent(user -> {
            user.setNotificationsEnabled(status);
            userRepository.save(user);
        });
    }
    public String getNotificationStatusEmoji(Long chatId) {
        return userRepository.findById(chatId)
                .map(user -> user.isNotificationsEnabled() ? "Yoqilgan ✅" : "O'chirilgan ❌")
                .orElse("O'chirilgan ❌"); // Agar user topilmasa, default o'chiq deb qaytaramiz
    }

    public boolean isNotificationEnabled(Long chatId) {
        return userRepository.findById(chatId)
                .map(UserEntity::isNotificationsEnabled)
                .orElse(false);
    }

    public String getUserCity(Long chatId) {
        return userRepository.findById(chatId)
                .map(UserEntity::getCity)
                .orElse("aniqlanmagan");
    }


    public boolean isFirstTimeUser(Long chatId) {
        return userRepository.findById(chatId)
                .map(user -> user.getCity() == null) // Agar shahar hali yo'q bo'lsa - demak birinchi marta
                .orElse(true);
    }
    @Transactional
    public int[] incrementTasbeh(Long chatId) {
        return userRepository.findById(chatId).map(user -> {
            int current = user.getTasbehCount() + 1;
            int full = user.getTasbehFullCount();

            if (current > 99) {
                current = 0;
                full = full + 1;
                user.setTasbehFullCount(full);
            }

            user.setTasbehCount(current);
            userRepository.save(user);

            return new int[]{current, full};
        }).orElse(new int[]{0, 0});
    }

    public void resetTasbeh(Long chatId) {
        userRepository.findById(chatId).ifPresent(user -> {
            user.setTasbehCount(0);
            userRepository.save(user);
        });
    }


    public int getTasbehCount(Long chatId) {
        return userRepository.findById(chatId)
                .map(UserEntity::getTasbehCount)
                .orElse(0);
    }

    public int getTasbehFullCount(Long chatId) {
        return userRepository.findById(chatId)
                .map(UserEntity::getTasbehFullCount)
                .orElse(0);
    }

    public boolean isCityNotSet(Long chatId) {
        return userRepository.findById(chatId)
                .map(user -> user.getCity() == null || user.getCity().equals("aniqlanmagan"))
                .orElse(true);
    }
}