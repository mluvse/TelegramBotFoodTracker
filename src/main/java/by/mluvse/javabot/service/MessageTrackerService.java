package by.mluvse.javabot.service;

import by.mluvse.javabot.events.MessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageTrackerService {
    private final Map<Long,Integer> lastBotMessages= new ConcurrentHashMap<>();
    private final ApplicationEventPublisher publisher;

    public MessageTrackerService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void saveLastMessage(Long chatId, Integer messageId){
        lastBotMessages.put(chatId,messageId);
    }

    public void getLastMessage(Long chatId){
        Integer lastMessageId=lastBotMessages.get(chatId);
        if(lastMessageId !=null){
            DeleteMessage deleteMessage= DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(lastMessageId)
                    .build();
            publisher.publishEvent(new MessageEvent(this, deleteMessage));
        }
    }
}
