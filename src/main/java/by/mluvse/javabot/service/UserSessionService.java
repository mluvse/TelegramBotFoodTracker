package by.mluvse.javabot.service;

import by.mluvse.javabot.enums.UserState;
import by.mluvse.javabot.model.User;
import by.mluvse.javabot.model.UserSession;
import by.mluvse.javabot.model.repository.UserRepository;
import by.mluvse.javabot.model.repository.UserSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    public UserSessionService(UserSessionRepository userSessionRepository, UserRepository userRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    public UserSession getOrCreate(Long chatId){
        Optional<UserSession> userSession=userSessionRepository.findUserSessionByChatId(chatId);
        return userSession.orElseGet(()-> {
            if(!userRepository.existsByChatId(chatId)){
                User newUser= new User();
                newUser.setChatId(chatId);
                userRepository.save(newUser);
            }
            UserSession newSession= new UserSession();
            newSession.setChatId(chatId);
            return userSessionRepository.save(newSession);
        });
    }


    @Transactional
    public void setState(long chatId, UserState userState) {
        UserSession session= getOrCreate(chatId);
        session.setState(userState);
        userSessionRepository.save(session);
    }

    public UserState getUserState(Long chatId){
        return getOrCreate(chatId).getState();
    }

    @Transactional
    public void clearData(long chatId) {
        UserSession session= getOrCreate(chatId);
        session.getTempData().clear();
        session.setState(UserState.IDLE);
        userSessionRepository.save(session);
    }

    @Transactional
    public void putTempData(Long chatId, String key, Object value){
        UserSession session= getOrCreate(chatId);
        session.getTempData().put(key,value);
        userSessionRepository.save(session);
    }

    public Map<String, Object> getTempData(Long chatId) {
        return getOrCreate(chatId).getTempData();
    }
}
