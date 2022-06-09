package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;

public final class PregameSystemCommand extends PregameCommandAdapter {
    public PregameSystemCommand() {
        super("system", "application information");
    }

    /**
     *  Prints out application status:
     *  ongoing games, groupId, userId who invokes this command
     *
     *  It will throw null pointer exception if there's no game existing
     */
    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {

        return new TextMessage("gameMap Size:" + GameManagerImpl.getManager().getOnGoingGames() + "\n"
                + "Avail system cores" + Runtime.getRuntime().availableProcessors() + "\n"
                + "cards in the deck remains: " + GameManagerImpl.getManager()
                .getGame(event.getSource().getSenderId()).getDeck().size() + "\n"
                + "Group ID" + event.getSource().getSenderId() + "\n"
                + "User ID" + event.getSource().getUserId() + "\n");

    }
}
