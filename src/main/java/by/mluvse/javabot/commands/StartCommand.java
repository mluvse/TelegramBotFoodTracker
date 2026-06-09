package by.mluvse.javabot.commands;

import by.mluvse.javabot.events.MessageEvent;
import by.mluvse.javabot.service.KeyboardService;
import by.mluvse.javabot.service.UserSessionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command{
    private final ApplicationEventPublisher publisher;
    private final KeyboardService keyboardService;
    private final UserSessionService userSessionService;

    public StartCommand(ApplicationEventPublisher publisher, KeyboardService keyboardService, UserSessionService userSessionService) {
        this.publisher = publisher;
        this.keyboardService = keyboardService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage()||!update.getMessage().hasText()){
            return false;
        }
        userSessionService.getOrCreate(update.getMessage().getChatId());
        return update.getMessage().getText().equals("/start");
    }

    @Override
    public void handle(Update update) {
        SendMessage message= SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .replyMarkup(keyboardService.mainMenu())
                .text("Главное меню:")
                .build();
        publisher.publishEvent(new MessageEvent(this,message));
    }

    @Override
    public String getCommand() {
        return CommandName.START.getName();
    }
}
