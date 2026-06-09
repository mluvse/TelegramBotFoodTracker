package by.mluvse.javabot.commands;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

@Service
public class CommandHandler {
    private final Collection<Command> commands;

    public CommandHandler(Collection<Command> commands) {
        this.commands = commands;
    }

    public void handleUpdate(Update update){
        for (Command command : commands) {
            if (command.canHandle(update)) {
                command.handle(update);
                return;
            }
        }
    }
}
