package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.commands.ingame.GameCommand;
import org.derryclub.linebot.commands.ingame.GameCommandRegister;

import java.util.List;

public final class InGameHelp extends GameCommandAdapter {

    private String allInGameCommands;
    private TextMessage cacheMessage;

    public InGameHelp() {
        super("help", "List out all available in-game commands");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        if (allInGameCommands == null) {
            List<GameCommand> gameCommands = GameCommandRegister.getGameCommands();

            allInGameCommands = gameCommands
                    .stream()
                    .map(command ->
                            "/" + command.getName() + ": " + command.getDescription() + "\n")
                    .reduce("", (a, b) -> a + b);
            cacheMessage = new TextMessage(allInGameCommands);
        }
        return cacheMessage;
    }
}
