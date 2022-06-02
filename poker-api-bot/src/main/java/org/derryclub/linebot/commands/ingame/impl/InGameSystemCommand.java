package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gameinstances.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;

public final class InGameSystemCommand extends GameCommandAdapter {

    public InGameSystemCommand() {
        super("system", "Get system information");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        String msg;
        String groupId = event.getSource().getSenderId();
        msg = "On-going games:" + GameManagerImpl.getManager().getOnGoingGames() + "\n"
                + "cards in the deck remains: " + GameManagerImpl.getManager().getGame(groupId).getDeck().size() + "\n"
                + "The player numbers: " + PlayerManagerImpl.getManager().getPlayers(groupId).size() + "\n"
                + "Cards dealt" + CommunityCardManager.getManager().getCommunityCardsMap().get(groupId) + "\n"
                + "Group ID" + groupId + "\n"
                + "User ID" + event.getSource().getUserId() + "\n"
        ;
        return new TextMessage(msg);
    }
}
