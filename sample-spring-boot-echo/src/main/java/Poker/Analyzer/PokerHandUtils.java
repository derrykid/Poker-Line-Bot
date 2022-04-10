package Poker.Analyzer;

import Card.*;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import static Card.Rank.*;

public enum PokerHandUtils {
    ; // no instance
    public static final int TIE = 0;

    public static final List<Card> ROYAL_FLUSH_SPADES = Arrays.asList(new Card(Rank.ACE, Suit.SPADE),
            new Card(Rank.KING, Suit.SPADE),
            new Card(Rank.QUEEN, Suit.SPADE),
            new Card(Rank.JACK, Suit.SPADE),
            new Card(Rank.TEN, Suit.SPADE));

    public static final List<Card> ROYAL_FLUSH_HEARTS = Arrays.asList(new Card(Rank.ACE, Suit.HEART),
            new Card(Rank.KING, Suit.HEART),
            new Card(Rank.QUEEN, Suit.HEART),
            new Card(Rank.JACK, Suit.HEART),
            new Card(Rank.TEN, Suit.HEART));

    public static final List<Card> ROYAL_FLUSH_CLUBS = Arrays.asList(new Card(Rank.ACE, Suit.CLUB),
            new Card(Rank.KING, Suit.CLUB),
            new Card(Rank.QUEEN, Suit.CLUB),
            new Card(Rank.JACK, Suit.CLUB),
            new Card(Rank.TEN, Suit.CLUB));

    public static final List<Card> ROYAL_FLUSH_DIAMONDS = Arrays.asList(new Card(Rank.ACE, Suit.DIAMOND),
            new Card(Rank.KING, Suit.DIAMOND),
            new Card(Rank.QUEEN, Suit.DIAMOND),
            new Card(Rank.JACK, Suit.DIAMOND),
            new Card(Rank.TEN, Suit.DIAMOND));

    public static final List<Card> STRAIGHT_WHEEL_SPADES = Arrays.asList(new Card(Rank.ACE, Suit.SPADE),
            new Card(Rank.TWO, Suit.SPADE),
            new Card(THREE, Suit.SPADE),
            new Card(FOUR, Suit.SPADE),
            new Card(FIVE, Suit.SPADE));

    public static final List<Card> STRAIGHT_WHEEL_HEARTS = Arrays.asList(new Card(Rank.ACE, Suit.HEART),
            new Card(Rank.TWO, Suit.HEART),
            new Card(THREE, Suit.HEART),
            new Card(FOUR, Suit.HEART),
            new Card(FIVE, Suit.HEART));

    public static final List<Card> STRAIGHT_WHEEL_CLUBS = Arrays.asList(new Card(Rank.ACE, Suit.CLUB),
            new Card(Rank.TWO, Suit.CLUB),
            new Card(THREE, Suit.CLUB),
            new Card(FOUR, Suit.CLUB),
            new Card(FIVE, Suit.CLUB));

    public static final List<Card> STRAIGHT_WHEEL_DIAMONDS = Arrays.asList(new Card(Rank.ACE, Suit.DIAMOND),
            new Card(Rank.TWO, Suit.DIAMOND),
            new Card(THREE, Suit.DIAMOND),
            new Card(FOUR, Suit.DIAMOND),
            new Card(FIVE, Suit.DIAMOND));

    public static final List<Rank> STRAIGHT_TWO_TO_SIX = Arrays.asList(TWO, THREE, FOUR, FIVE, SIX);

    public static final List<Rank> STRAIGHT_THREE_TO_SEVEN = Arrays.asList(THREE, FOUR, FIVE, SIX, SEVEN);

    public static final List<Rank> STRAIGHT_FOUR_TO_EIGHT = Arrays.asList(FOUR, FIVE, SIX, SEVEN, EIGHT);

    public static final List<Rank> STRAIGHT_FIVE_TO_NINE = Arrays.asList(FIVE, SIX, SEVEN, EIGHT, NINE);

    public static final List<Rank> STRAIGHT_SIX_TO_TEN = Arrays.asList(SIX, SEVEN, EIGHT, NINE, TEN);

    public static final List<Rank> STRAIGHT_SEVEN_TO_JACK = Arrays.asList(SEVEN, EIGHT, NINE, TEN, JACK);

    public static final List<Rank> STRAIGHT_EIGHT_TO_QUEEN = Arrays.asList(EIGHT, NINE, TEN, JACK, QUEEN);

    public static final List<Rank> STRAIGHT_NINE_TO_KING = Arrays.asList(NINE, TEN, JACK, QUEEN, KING);

    public static final List<Rank> STRAIGHT_TEN_TO_ACE = Arrays.asList(TEN, JACK, QUEEN, KING, ACE);

    // not review yet
    public static void checkHandClassification(Hand hand, ClassificationRank classificationRank){
        if (hand.getHandAnalyzer().getClassification().getClassificationRank() != classificationRank) {
            throw new RuntimeException("Hand: " + hand + " does not match expected classificationRank " + classificationRank);
        }
    }

    public static Classification classifyPokerHand(RankGroup rankGroup, SuitGroup suitGroup, SortedSet<Card> cards) {
        PokerHandClassifier handDetector = new PokerHandClassifier(rankGroup, suitGroup, cards);
        return handDetector.classify();
    }
}
