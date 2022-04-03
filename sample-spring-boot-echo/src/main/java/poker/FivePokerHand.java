package poker;

import java.util.*;

public class FivePokerHand {

    private static ArrayList<Set<Card>> playerHands = new ArrayList<>();

    public static String getStartHand(Deck deck) throws IllegalAccessException {

        // player1 start hand
        Set<Card> player1 = new HashSet<>();

        // draw 2 cards, use Optional<Card> method to catch null if occurs
        player1.add(deck.deal().orElseThrow(IllegalAccessException::new));
        player1.add(deck.deal().orElseThrow(IllegalAccessException::new));

        StringBuilder player1Hand = new StringBuilder();

        // 1. first try out
        // use string builder to apped 2 card (toString)  to it
        // return the String
        player1.stream().forEach(e -> player1Hand.append(e.toString()));

        // 2. Return other object later to work on instead of string, e.g. JSON, Map, etc.

        return player1Hand.toString();
    }

    public static String getCard(Deck deck) throws IllegalAccessException {
        Card card = deck.deal().orElseThrow(IllegalAccessError::new);
        return card.toString();
    }

}
