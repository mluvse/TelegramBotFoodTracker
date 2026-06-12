package by.mluvse.javabot.commands;

import by.mluvse.javabot.enums.ActivityType;
import by.mluvse.javabot.enums.Gender;
import by.mluvse.javabot.enums.Goal;
import by.mluvse.javabot.enums.UserState;
import by.mluvse.javabot.events.MessageEvent;

import by.mluvse.javabot.service.RationCalculateService;
import by.mluvse.javabot.service.UserSessionService;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;
import java.util.Map;


public class CalculateRationCallbackCommand implements Command{

    private final ApplicationEventPublisher publisher;
    private final UserSessionService userSessionService;
    private final RationCalculateService rationCalculateService;

    public CalculateRationCallbackCommand(ApplicationEventPublisher publisher, UserSessionService userSessionService, RationCalculateService rationCalculateService) {
        this.publisher = publisher;
        this.userSessionService = userSessionService;
        this.rationCalculateService = rationCalculateService;
    }

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasCallbackQuery()) return false;
        String data= update.getCallbackQuery().getData();
        return data.startsWith("gender:")
                || data.startsWith("activity_type:")
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
            sendMessage(chatId,"Увядзіце ваш узрост (поўных гадоў):",null);
        } else if (data.startsWith("goal:")) {
            Goal goal= Goal.valueOf(data.split(":")[1]);
            userSessionService.putTempData(chatId,"goal",goal.getValue());
            userSessionService.setState(chatId,UserState.WAITING_GOAL);
            sendMessage(chatId,"Абярыце мэту:",goalInline());
        } else if (data.startsWith("activityType:")) {
            ActivityType activityType= ActivityType.valueOf(data.split(":")[1]);
            userSessionService.putTempData(chatId,"activityType",activityType.name());

            switch (activityType){
                case SEDENTARY -> showResult(chatId);
                case STEPS, MIXED -> askSteps(chatId);
                case STRENGTH -> askForWorkouts(chatId);
            }
        } else if (data.startsWith("coefficient:")) {
            int coefficient= Integer.parseInt(data.split(":")[1]);
            userSessionService.putTempData(chatId,"restCoefficient",coefficient);
            userSessionService.setState(chatId,UserState.SHOWING_RESULT);
            showResult(chatId);
        }
    }

    private void askForWorkouts(long chatId) {
        userSessionService.setState(chatId, UserState.WAITING_WORKOUTS_PER_WEEK);
        sendMessage(chatId,"Увядзіце колькасць трэніровак у тыдзень: ", null);
    }

    private void askSteps(long chatId) {
        userSessionService.setState(chatId,UserState.WAITING_STEPS);
        sendMessage(chatId,"Увядзіце сярэднюю колькасць крокаў у месяц: ", null);
    }

    @Override
    public String getCommand() {
        return CommandName.CALCULATE_RATION.getName();
    }

    private void showResult(long chatId){
        Map<String, Object> data= userSessionService.getTempData(chatId);
        double tdee= rationCalculateService.calculateTdee(data);
        Goal goal= Goal.valueOf((String) data.get("goal"));
        double target= tdee+ goal.getCalorieDelta();

        userSessionService.putTempData(chatId,"tdee",tdee);
        userSessionService.setState(chatId, UserState.SHOWING_RESULT);

        String text= String.format("Рэзультат з учотам актыўнасці: %.0f ккал\n "
                +"Базовы абмен рэчываў: %.0f ккал",target,tdee);

        sendMessage(chatId,text,saveInline());
    }

    private ReplyKeyboard saveInline() {
        List<InlineKeyboardRow> rows= List.of(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("Захаваць рэзультат у профілі")
                .callbackData("save_ration")
                .build()),
                new InlineKeyboardRow(InlineKeyboardButton.builder()
                        .text("Не захоўваць")
                        .callbackData("discard_ration")
                        .build()));

        return new InlineKeyboardMarkup(rows);
    }

    private ReplyKeyboard goalInline(){
        List<InlineKeyboardRow> rows= List.of(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text("Пахудзенне")
                .callbackData(Goal.LOSS.getValue())
                .build()),
                new InlineKeyboardRow(InlineKeyboardButton.builder()
                        .text("Падтрымка вагі")
                        .callbackData(Goal.MAINTAIN.getValue())
                        .build()),
                new InlineKeyboardRow(InlineKeyboardButton.builder()
                        .text("Набор вагі")
                        .callbackData(Goal.GAIN.getValue())
                        .build()));
        return new InlineKeyboardMarkup(rows);
    }
    public void sendMessage(long chatId, String text, ReplyKeyboard markup){
        SendMessage.SendMessageBuilder<?, ?> builder=SendMessage.builder()
                .chatId(chatId)
                .text(text);
                if(markup!= null) builder.replyMarkup(markup);
                publisher.publishEvent(new MessageEvent(this, builder.build()));
    }
}
