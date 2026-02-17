package com.example.bot.repository;

import com.example.bot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByNotificationsEnabledTrue();}