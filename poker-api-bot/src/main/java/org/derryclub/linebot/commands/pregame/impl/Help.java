package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.LineMessageAPI;
import org.derryclub.linebot.commands.pregame.PregameCommand;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.commands.pregame.PregameCommandRegister;

import java.util.List;

public final class Help extends PregameCommandAdapter {

    private final String allPregameCommands;

    public Help() {
        super("help", "show all commands available pregame");
//        List<PregameCommand> pregameCommands = LineMessageAPI.getPregameCommandReceiver()
//                .getPregameCommands();
//
//        allPregameCommands = pregameCommands.stream()
//                .map(cmd -> cmd.getClass().getSimpleName())
//                .reduce("", (a, b) -> (a + b + "\n"));
        allPregameCommands = "hi";
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return new TextMessage(allPregameCommands);
    }
}
