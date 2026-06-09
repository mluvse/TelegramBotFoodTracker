package by.mluvse.javabot.commands;

import by.mluvse.javabot.enums.Gender;
import by.mluvse.javabot.enums.UserState;
import by.mluvse.javabot.model.repository.UserRepository;

import by.mluvse.javabot.service.UserSessionService;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CalculateRationCallbackCommand implements Command{

    private final ApplicationEventPublisher publisher;
    private final UserSessionService userSessionService;
    private final UserRepository userRepository;

    public CalculateRationCallbackCommand(ApplicationEventPublisher publisher, UserSessionService userSessionService, UserRepository userRepository) {
        this.publisher = publisher;
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasCallbackQuery()) return false;
        String data= update.getCallbackQuery().getData();
        return data.startsWith("gender:") || data.startsWith("activity_type:")
                || data.startsWith("goal:")
                || data.equals("save_ration")
                || data.equals("discard_ration");
    }

    @Override
    public void handle(Update update) {
        String data=update.getCallbackQuery().getData();
        long chatId= update.getCallbackQuery().getMessage().getChatId();

        if(data.startsWith("gender:")){
            Gender gender=Gender.valueOf(data.split(":")[1]);
            userSessionService.putTempData(chatId,"gender",gender.name());
            userSessionService.setState(chatId,UserState.WAITING_AGE);
            
        }
    }

    @Override
    public String getCommand() {
        return "";
    }
}
