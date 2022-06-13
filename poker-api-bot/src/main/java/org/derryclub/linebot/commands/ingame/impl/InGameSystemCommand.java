package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamemanage.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;

public final class InGameSystemCommand extends GameCommandAdapter {

    public InGameSystemCommand() {
        super("system", "系統資訊");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        String msg;
        String groupId = event.getSource().getSenderId();
        msg = "On-going games:" + GameManagerImpl.getManager().getOnGoingGames() + "\n"
                + "cards in the deck remains: " + GameManagerImpl.getManager().getGame(groupId).getDeck().size() + "\n"
                + "The player numbers: " + PlayerManagerImpl.getManager().getPlayers(groupId).size() + "\n"
                + "Game clock" + GameManagerImpl.getManager().getGame(groupId).getWhoseTurnToMove() + "\n"
                + "Cards dealt" + CommunityCardManager.getManager().getCommunityCardsMap().get(groupId) + "\n"
                + "Chip: " + PlayerManagerImpl.getManager().getPlayer(groupId, event.getSource().getUserId()).getChip().getAvailableChip();
        return new TextMessage(msg);
    }
}
