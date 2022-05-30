package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControlSystem;
import org.derryclub.linebot.service.pokergame.gamecontrol.Gaming;

public final class FoldCommand extends GameCommandAdapter implements Gaming {

    public FoldCommand() {
        super("fold", "蓋牌");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return GameControlSystem.playerFold(event);
    }
}
