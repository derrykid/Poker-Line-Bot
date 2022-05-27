package org.derryclub.linebot.commands.ingame;

import com.linecorp.bot.model.message.TextMessage;
import org.derryclub.linebot.commands.CommandReceiver;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import java.util.*;
import java.util.stream.Collectors;

public final class GameCommandReceiver implements CommandReceiver {

    private final List<String> gameCommandStringsList = GameCommandRegister
            .getGameCommands()
            .stream()
            .map(GameCommand::getName)
            .collect(Collectors.toList());

    private final List<GameCommand> gameCommands = GameCommandRegister.getGameCommands();

    @Override
    public Message handle(MessageEvent<TextMessageContent> event) {
        final String command = event.getMessage().getText().split(" ")[0]
                .substring(1).toLowerCase();

        // first check if the command exists,
        // if exists, then run the command, if no, do nothing
        if (!gameCommandStringsList.contains(command)) {
            return null;
        } else {
            Optional<GameCommand> commandOptional = gameCommands.stream()
                    .filter(cmd -> cmd.getName().equalsIgnoreCase(command))
                    .findAny();
            return commandOptional.isEmpty() ? new TextMessage("Command not support")
                    : commandOptional.get().onSlashCommand(event);
        }
    }
}
