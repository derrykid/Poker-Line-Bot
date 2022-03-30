package poker;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Card implements Comparable<Card> {

    private final Rank rank;
    private final Suit suit;

    private static final Map<String, Card> CARD_CACHE = initCache();

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return this.rank;
    }
    public Suit getSuit() {
        return this.suit;
    }

    private static Map<String, Card> initCache() {
        Map<String, Card> cardCache = new HashMap<>();

        for (final Rank perRank: Rank.values()) {
            for (final Suit perSuit: Suit.values()) {
                cardCache.put(cardKey(perRank, perSuit), new Card(perRank, perSuit));
            }
        }
        return cardCache;
    }

    private static String cardKey(Rank rank, Suit suit) {
        return rank + " of " + suit;
    }


    public static Card getCard(Rank rank, Suit suit) {
        Card card = CARD_CACHE.get(cardKey(rank, suit));
        if (card != null) {
            return card;
        }
        throw new RuntimeException("Invalid card!" + rank + " of " + suit);
    }

    @Override
    public int compareTo(Card o) {
        int comparison = Integer.compare(this.rank.getRankValue(), o.rank.getRankValue());
        return comparison != 0 ? comparison : Integer.compare(this.suit.getSuitValue(), o.suit.getSuitValue());
    }

    @Override
    public String toString() {
        return String.format("%s of %s", this.rank, this.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }
}
