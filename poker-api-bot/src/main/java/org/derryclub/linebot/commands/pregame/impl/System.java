package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.processor.GameController;

public final class System extends PregameCommandAdapter {
    public System() {
        super("system", "application information");
    }

    /**
     *  Prints out application status:
     *  ongoing games, groupId, userId who invokes this command
     */
    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {

        return new TextMessage("gameMap Size:" + GameController.getOngoingGame() + "\n"
//                + "cards in the deck remains: " + GameController.getGameMap().get(event.getSource().getSenderId()).getDeck().size() + "\n"
//                + "The player numbers: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()).size() + "\n"
                + "The players: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()) + "\n"
                + "Group ID" + event.getSource().getSenderId() + "\n"
                + "User ID" + event.getSource().getUserId() + "\n");

    }
}
