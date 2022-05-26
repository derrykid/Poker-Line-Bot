package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.processor.GameController;

public final class System extends GameCommandAdapter {

    public System() {
        super("system", "Get system information");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        String msg;
        msg = "gameMap Size:" + GameController.getOngoingGame() + "\n"
                + "cards in the deck remains: " + GameController.getGameMap().get(event.getSource().getSenderId()).getDeck().size() + "\n"
                + "The player numbers: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()).size() + "\n"
                + "The player: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()) + "\n"
                + "Group ID" + event.getSource().getSenderId() + "\n"
                + "User ID" + event.getSource().getUserId() + "\n"
        ;
        return new TextMessage(msg);
    }
}
