package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommand;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.commands.pregame.PregameCommandRegister;

import java.util.List;

public final class PregameHelp extends PregameCommandAdapter {

    private String allPregameCommands;
    private TextMessage cacheMessage;

    public PregameHelp() {
        super("help", "列出遊戲開始前可用的指令");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        if (allPregameCommands == null) {
        List<PregameCommand> pregameCommandList = PregameCommandRegister.getPregameCommands();

        allPregameCommands = pregameCommandList
                .stream()
                .map(command ->
                        "/" + command.getName() + ": " + command.getDescription() + "\n")
                .reduce("", (a, b) -> a + b );
        cacheMessage = new TextMessage(allPregameCommands);
        }
        return cacheMessage;
    }
}
