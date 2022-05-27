package org.derryclub.linebot.commands;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * All command implementations have to
 * 1. unique name
 * 2. return own status e.g. in game, or pregame
 *
 */
public interface Command {
    enum GameStatus{
        IN_GAME, PRE_GAME;
    }

    String getName();
    String getDescription();
    Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event);
    GameStatus getStatus();

}
