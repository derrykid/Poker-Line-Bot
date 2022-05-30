package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControlSystem;
import org.derryclub.linebot.service.pokergame.gamecontrol.Gaming;

public final class CheckCommand extends GameCommandAdapter implements Gaming {

    public CheckCommand() {
        super("check", "過牌");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return GameControlSystem.playerCheck(event);
    }
}
