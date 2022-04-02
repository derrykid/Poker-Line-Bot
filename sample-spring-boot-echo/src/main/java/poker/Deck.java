package poker;

import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

// this class gives user a starting hand
public class Deck {

    private final Stack<Card> deckCards;

    private Deck(boolean shouldShuffle) {
        this.deckCards = initDeck(shouldShuffle);
    }

    private Stack<Card> initDeck(boolean shouldShuffle) {
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
        return new Deck(true);
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
