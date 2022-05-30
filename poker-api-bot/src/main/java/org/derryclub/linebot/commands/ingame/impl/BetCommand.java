package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControlSystem;
import org.derryclub.linebot.service.pokergame.gamecontrol.Gaming;

public final class BetCommand extends GameCommandAdapter implements Gaming {

    public BetCommand() {
        super("bet", "下注");
    }

    /**
     * Only in charge of getting the betting value from user,
     * <p>
     * The {@link GameControlSystem} will determine based on the game stage and forward to
     * correspond handler
     * </p>
     *
     * @param event from Line user
     * @return TextMessage of betting result
     */
    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {

        final int bettingValue = Integer.parseInt(
                event.getMessage().getText().split(" ")[1]);

        Source source = event.getSource();
        String groupId = source.getSenderId();
        String userId = source.getUserId();

        return GameControlSystem.betEvent(groupId, userId, bettingValue);
    }
}
