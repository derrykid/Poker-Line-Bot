package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.poker.card.Deal;
import org.derryclub.linebot.poker.card.Deck;

public final class DrawCardCommand extends PregameCommandAdapter {

    public DrawCardCommand() {
        super("抽牌", "隨機抽一張牌");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return new TextMessage(Deal.dealCard(Deck.newShuffledSingleDeck()).toString());
    }
}
