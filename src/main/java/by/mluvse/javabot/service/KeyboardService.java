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
        rows.add(new KeyboardRow("Расчет КБЖУ с ИИ","Дневной рацион"));
        rows.add(new KeyboardRow("Настройки"));
        ReplyKeyboardMarkup markup= new ReplyKeyboardMarkup(rows);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        return markup;
    }


}
