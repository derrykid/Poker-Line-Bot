package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;

public final class Help extends PregameCommandAdapter {

    private final StringBuilder allPregameCommands = new StringBuilder();

    public Help() {
        super("help", "show all commands available pregame");
        allPregameCommands
                .append("/help").append("\n")
                .append("/start").append("\n")
                .append("/system").append("\n");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return new TextMessage(allPregameCommands.toString());
    }
}
