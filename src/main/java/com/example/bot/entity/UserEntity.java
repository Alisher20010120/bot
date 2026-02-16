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
    private LocalDateTime registeredAt;
}