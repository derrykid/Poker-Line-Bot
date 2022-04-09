package Card;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

public class Deck {

    private final Stack<Card> deckCards;

    private Deck() {
        this.deckCards = initDeck();
    }

    private Stack<Card> initDeck() {
        Stack<Card> cardDeck = new Stack<>();
        for (Suit perSuit : Suit.values()) {
            for (Rank perRank : Rank.values()) {
                cardDeck.push(Card.getCard(perRank, perSuit));
            }
        }

        Collections.shuffle(cardDeck);

        return cardDeck;
    }

    public static Deck newShuffledSingleDeck() {
        return new Deck();
    }


    public int size() {
        return this.deckCards.size();
    }

    public boolean contains(Card card) {
        return this.deckCards.contains(card);
    }

    public Optional<Card> deal() {
        return this.deckCards.empty() ? Optional.empty() : Optional.of(this.deckCards.pop());
    }
}
