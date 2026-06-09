package by.mluvse.javabot.commands;

import by.mluvse.javabot.events.MessageEvent;
import by.mluvse.javabot.service.UserSessionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingsCommand implements Command{
    private final ApplicationEventPublisher publisher;
    private final UserSessionService userSessionService;

    public SettingsCommand(ApplicationEventPublisher publisher, UserSessionService userSessionService) {
        this.publisher = publisher;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage()||!update.getMessage().hasText()) return false;
        userSessionService.getOrCreate(update.getMessage().getChatId());
        return update.getMessage().getText().equals("/settings");
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage sendMessage= SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(settingsInline())
                .build();
        publisher.publishEvent(new MessageEvent(this, sendMessage));
    }

    private ReplyKeyboard settingsInline() {
        List<InlineKeyboardRow> rows= new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("Обновить профиль")
                .callbackData("update_profile")
                .build()));
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                        .text("Уведомления")
                        .callbackData("notify")
                        .build()));
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public String getCommand() {
        return CommandName.SETTINGS.getName();
    }
}
