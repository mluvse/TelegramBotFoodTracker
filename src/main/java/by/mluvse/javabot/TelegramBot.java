package by.mluvse.javabot;

import by.mluvse.javabot.commands.CommandHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {


    private final CommandHandler commandHandler;

    @Value("${bot.token}")
    private String telegramBotToken;

    public TelegramBot(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }


    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }


    @Override
    public void consume(Update update) {
        commandHandler.handleUpdate(update);
    }
}
