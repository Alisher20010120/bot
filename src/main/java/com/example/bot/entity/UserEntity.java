package com.example.bot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    private Long chatId;
    private String firstName;
    private String username;
    private String city;
    private boolean notificationsEnabled;
    private int tasbehCount = 0;
    private int tasbehFullCount = 0;
    private LocalDateTime registeredAt;
}