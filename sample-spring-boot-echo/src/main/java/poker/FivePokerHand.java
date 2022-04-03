package poker;

import java.util.*;

public class FivePokerHand {

    private static final int POKER_HAND_SIZE = 5;

    public static String getStartHand(Deck deck) throws IllegalAccessException {

        // player1 start hand
        Set<Optional<Card>> player1 = new HashSet<>();

        // draw 2 cards, use Optional<Card> method to catch null if occurs
        player1.add(deck.deal());
        player1.add(deck.deal());

        StringBuilder player1Hand = new StringBuilder();

        // 1. first try out
        // use string builder to apped 2 card (toString)  to it
        // return the String
        for (Optional<Card> card: player1){
            player1Hand.append(card.toString() + "\n");
        }

        // 2. Return other object later to work on instead of string, e.g. JSON, Map, etc.

        return player1Hand.toString();
    }

//    public static class Builder {
//
//        private SortedSet<Card> cards;
//
//        public Builder() {
//            this.cards = new TreeSet<>();
//        }
//
//        public Builder addCard(Optional<Card> card) throws IllegalAccessException {
//            this.cards.add(card.orElseThrow(IllegalAccessException::new));
//            return this;
//        }
//
//        public FivePokerHand build() {
//            if (this.cards.size() != POKER_HAND_SIZE) {
//                throw new RuntimeException("Invalid hand size for hand " + this.cards.toString());
//            }
//            return new FivePokerHand(this);
//        }
//
//    }
}
