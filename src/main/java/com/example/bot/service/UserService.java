package com.example.bot.service;

import com.example.bot.entity.UserEntity;
import com.example.bot.repository.UserEntityRepository;
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
        userRepository.findById(chatId).ifPresent(user -> {
            user.setCity(city);
            userRepository.save(user);
        });
    }
}