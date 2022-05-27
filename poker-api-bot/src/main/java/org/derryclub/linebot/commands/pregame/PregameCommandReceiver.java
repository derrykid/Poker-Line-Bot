package org.derryclub.linebot.commands.pregame;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.commands.CommandReceiver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Getter
public final class PregameCommandReceiver implements CommandReceiver {

    private final List<PregameCommand> pregameCommands;
    private final List<String> pregameCommandStringsList;

    public PregameCommandReceiver(){
        PregameCommandRegister register = new PregameCommandRegister();

        pregameCommands = register.getPregameCommands();

        pregameCommandStringsList =  register.getPregameCommands()
                .stream()
                .map(PregameCommand::getName)
                .collect(Collectors.toList());

    }

    @Override
    public Message getCommand(MessageEvent<TextMessageContent> event) {
        final String command = event.getMessage().getText().split(" ")[0].toLowerCase();

        // first check if the command exists,
        // if exists, then run the command, if no, do nothing
        if (!pregameCommandStringsList.contains(command)) {
            log.info(command + " isn't registered");
            return null;
        } else {
            Optional<PregameCommand> commandOptional = pregameCommands.stream()
                    .filter(cmd -> cmd.getName().equalsIgnoreCase(command))
                    .findAny();
            return commandOptional.isEmpty() ? new TextMessage("Command not support")
                    : commandOptional.get().onSlashCommand(event);
        }
    }
}
