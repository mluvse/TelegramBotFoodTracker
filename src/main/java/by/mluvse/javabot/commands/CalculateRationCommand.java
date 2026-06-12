package by.mluvse.javabot.commands;

import by.mluvse.javabot.enums.UserState;
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
public class CalculateRationCommand implements Command {
    private final ApplicationEventPublisher publisher;
    private final UserSessionService userSessionService;

    public CalculateRationCommand(ApplicationEventPublisher publisher, UserSessionService userSessionService) {
        this.publisher = publisher;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage()||!update.getMessage().hasText()){
            return false;
        }
        userSessionService.getOrCreate(update.getMessage().getChatId());
        return update.getMessage().getText().equals("Днеўны рацыён");
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        userSessionService.clearData(chatId);
        userSessionService.setState(chatId, UserState.WAITING_GENDER);
        SendMessage sendMessage= SendMessage.builder()
                .chatId(chatId)
                .text("Абярыце пол:")
                .replyMarkup(dailyRationInline())
                .build();

        publisher.publishEvent(new MessageEvent(this,sendMessage));
    }

    private ReplyKeyboard dailyRationInline() {
        List<InlineKeyboardRow> rows= new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("Жаночы")
                .callbackData("gender:female")
                .build(),
                InlineKeyboardButton.builder()
                .text("Мужскі")
                .callbackData("gender:male")
                .build()));
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }


    @Override
    public String getCommand() {
        return CommandName.CALCULATE_RATION.getName();
    }
}
