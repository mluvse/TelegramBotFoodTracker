package by.mluvse.javabot.events;

import by.mluvse.javabot.service.MessageTrackerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class EventListener {

    private final TelegramClient telegramClient;
    private final MessageTrackerService messageTrackerService;

    public EventListener(TelegramClient telegramClient, MessageTrackerService messageTrackerService) {
        this.telegramClient = telegramClient;
        this.messageTrackerService = messageTrackerService;
    }

    @org.springframework.context.event.EventListener
    public void on(MessageEvent event) throws TelegramApiException {
        Object sentMessage=telegramClient.execute(event.getMessage());
        if(sentMessage instanceof Message &&  ((Message) sentMessage).getMessageId()!= null){
            messageTrackerService.saveLastMessage(((Message) sentMessage).getChatId(),
                    ((Message) sentMessage).getMessageId());
        }
    }
}
