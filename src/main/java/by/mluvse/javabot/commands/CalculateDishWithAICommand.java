package by.mluvse.javabot.commands;

import by.mluvse.javabot.events.MessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;


import java.util.List;

@Component
public class CalculateDishWithAICommand implements Command {
    private final ApplicationEventPublisher publisher;

    public CalculateDishWithAICommand( ApplicationEventPublisher publisher) {

        this.publisher = publisher;
    }

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage()||!update.getMessage().hasText()){
            return false;
        }
        return update.getMessage().getText().equals("/calculate_dish");
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage sendMessage= SendMessage.builder()
                .chatId(chatId)
                .build();

        publisher.publishEvent(new MessageEvent(this, sendMessage));
    }

    private ReplyKeyboard AIInline(){
        InlineKeyboardButton button1= InlineKeyboardButton.builder()
                .text("Сфотографировать блюдо")
                .callbackData("photo_ai")
                .build();
        InlineKeyboardButton button2= InlineKeyboardButton.builder()
                .text("Ввести вручную")
                .callbackData("input_ai")
                .build();
        InlineKeyboardButton button3= InlineKeyboardButton.builder()
                .text("Открыть сканер")
                .callbackData("scanner")
                .build();

        List<InlineKeyboardRow> rows= List.of(new InlineKeyboardRow(button1,button2),
                new InlineKeyboardRow(button3));

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public String getCommand() {
        return CommandName.CALCULATE_WITH_AI.getName();
    }
}
