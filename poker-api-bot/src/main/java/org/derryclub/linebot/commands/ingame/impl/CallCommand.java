package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;

public final class CallCommand extends GameCommandAdapter {

    public CallCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return null;
    }
}
