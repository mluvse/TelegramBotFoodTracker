package by.mluvse.javabot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    public ReplyKeyboard mainMenu(){
        List<KeyboardRow> rows= new ArrayList<>();
        rows.add(new KeyboardRow("Разлік стравы з ШІ","Днеўны рацыён"));
        rows.add(new KeyboardRow("Налады"));
        ReplyKeyboardMarkup markup= new ReplyKeyboardMarkup(rows);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        return markup;
    }


}
