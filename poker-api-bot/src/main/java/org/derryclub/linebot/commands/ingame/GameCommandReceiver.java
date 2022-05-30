package org.derryclub.linebot.commands.ingame;

import com.linecorp.bot.model.message.TextMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.commands.CommandReceiver;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import org.derryclub.linebot.commands.pregame.PregameCommandReceiver;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton instance, only accessable via getInstance()
 */
@Slf4j
@Getter
public final class GameCommandReceiver implements CommandReceiver {

    private static GameCommandReceiver instance;

    private final List<GameCommand> gameCommands;
    private final List<String> gameCommandStringsList;

    private GameCommandReceiver() {
        gameCommands = GameCommandRegister.getGameCommands();
        gameCommandStringsList = gameCommands
                        .stream()
                        .map(GameCommand::getName)
                        .collect(Collectors.toList());

    }

    public static GameCommandReceiver getInstance() {
        if (instance == null) {
            instance = new GameCommandReceiver();
        }
        return instance;
    }

    @Override
    public Message handle(MessageEvent<TextMessageContent> event) {

        // is it gaming command? or is it system command?
        final String command = event.getMessage().getText().split(" ")[0]
                .substring(1).toLowerCase();

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
