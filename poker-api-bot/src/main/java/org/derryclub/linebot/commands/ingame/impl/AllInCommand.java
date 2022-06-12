package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControlSystem;

public final class AllInCommand extends GameCommandAdapter {

    public AllInCommand() {
        super("all in", "All in all chips");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return GameControlSystem.playerAllIn(event);
    }
}
