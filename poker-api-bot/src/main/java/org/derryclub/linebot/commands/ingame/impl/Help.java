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

public final class Help extends GameCommandAdapter {

//
    /*
            StringBuilder text = new StringBuilder();
            for (GameCommandAdapter per : EnumSet.allOf(GameCommandAdapter.class)) {
                text.append('/').append(per.toString().toLowerCase()).append(": ").append(per.getDescription()).append("\n");
            }
            return new TextMessage(text.toString());
    *
    * */

    private final String allInGameCommands;
    private final TextMessage helpMessage;

    public Help() {
        super("help", "List out all available in-game commands");
        List<GameCommand> gameCommands = GameCommandRegister.getGameCommands();
        allInGameCommands = gameCommands.stream()
                .map(cmd -> cmd.getClass().getSimpleName())
                .reduce("", (a, b) -> (a + b + "\n"));

        helpMessage = new TextMessage(allInGameCommands);
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return helpMessage;
    }
}
