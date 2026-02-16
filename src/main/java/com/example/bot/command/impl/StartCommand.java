package com.example.bot.command.impl;

import com.example.bot.command.BotCommand;
import com.example.bot.service.KeyboardService;
import com.example.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommand implements BotCommand {

    private final UserService userService;
    private final KeyboardService keyboardService;

    @Override
    public SendMessage execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getFrom().getFirstName();

        userService.saveUser(update.getMessage().getFrom(), chatId);

        String text = "Assalomu alaykum, " + firstName + "!\n" +
                      "Botimizga xush kelibsiz. Iltimos, shahringizni tanlang 👇";

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        
        message.setReplyMarkup(keyboardService.getCityKeyboard());

        return message;
    }

    @Override
    public String getCommandName() {
        return "/start";
    }
}