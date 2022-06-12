package org.derryclub.linebot.service.pokergame.card;

import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Deal;
import org.derryclub.linebot.poker.card.Deck;
import org.derryclub.linebot.service.util.LineServerInteractor;

import java.util.List;
import java.util.Set;

public final class DealCards {

    public static void dealHoleCards(Set<Player> players, Deck deck) {
        /*
         * 1. Deal cards to participants: push message
         * */
        for (Player per : players) {
            /*
             * 1. Deal cards
             * The list is already sorted, so we can deal card directly
             * */
            per.initCardSet();
            StringBuilder cardsBuilder = new StringBuilder();
            for (int i = 0; i < 2; i++) {
                /*
                 * use Player to save cards, when in river state, call the cards and combine it with communitycards
                 *
                 * */
                Card card = Deal.dealCard(deck);
                // add the card to Player variable
                per.addPlayerCards(card);
                cardsBuilder.append(card);
            }
            /*
             * push message to push the cards to each player
             * */
            String cards = cardsBuilder.toString();
            LineServerInteractor.pushHoleCards(per.getUserId(), cards);
        }

    }

    /**
     * This method is exclusive to preflop to deal 3 cards.
     *
     * @return a String of 3 cards
     */
    public static String deal3Cards(Deck deck, List<Card> communityCards) {
        StringBuilder cards = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            communityCards.add(Deal.dealCard(deck));
        }
        communityCards.forEach(cards::append);
        return cards.toString();
    }

    /**
     * Use to deal card
     *
     * @param deck           the deck of card of the table
     * @param communityCards keep a record of all cards that dealt
     * @return the card map to a string
     */
    public static String dealCard(Deck deck, List<Card> communityCards) {
        StringBuilder cards = new StringBuilder();

        communityCards.add(Deal.dealCard(deck));

        communityCards.forEach(cards::append);

        return cards.toString();
    }
}
