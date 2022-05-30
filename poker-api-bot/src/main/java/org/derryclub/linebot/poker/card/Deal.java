package org.derryclub.linebot.poker.card;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *  This is the API where client get the card from
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Deal {
    public static Card dealCard(Deck deck) {
        return deck.deal().orElseThrow(IllegalAccessError::new);
    }
}
