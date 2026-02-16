package com.example.bot.bot;

import com.example.bot.command.BotCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final Map<String, BotCommand> commands;

    public MyTelegramBot(@Value("${bot.token}") String botToken,
                         List<BotCommand> commandList) {
        super(botToken);
        this.commands = commandList.stream()
                .collect(Collectors.toMap(BotCommand::getCommandName, Function.identity()));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            BotCommand command = commands.get(text);
            if (command != null) {
                send(command.execute(update));
            } else {
                send(new SendMessage(chatId.toString(), "Noma'lum buyruq."));
            }
        }
    }

    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "your_bot_username";
    }
}