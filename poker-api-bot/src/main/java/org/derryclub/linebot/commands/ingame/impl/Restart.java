package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;

/**
 *  restart the existing game
 */
public final class Restart extends GameCommandAdapter {

    public Restart(String name, String description) {
        super("restart", "Restart this game");
    }

    // todo implement this command
    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return new TextMessage("Game restarting...");
    }
}
