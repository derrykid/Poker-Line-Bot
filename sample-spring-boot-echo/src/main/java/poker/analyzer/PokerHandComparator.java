package poker.analyzer;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import card.*;

public class PokerHandComparator implements Comparator<Hand> {

    @Override
    public int compare(Hand hand, Hand otherHand) {
        final int classificationComparison = compareHandClassifications(hand, otherHand);

        if (classificationComparison != 0) {
            return classificationComparison;
        }

        final int tiebreaker;
        switch (hand.getHandAnalyzer().getClassification().getClassificationRank()) {
            case ROYAL_FLUSH:
                tiebreaker = compareRoyalFlushHands(hand, otherHand);
                break;
            case STRAIGHT_FLUSH:
                tiebreaker = compareStraightFlushHands(hand, otherHand);
                break;
            case STRAIGHT_FLUSH_WHEEL:
                tiebreaker = compareStraightFlushWheelHands(hand, otherHand);
                break;
            case FOUR_OF_A_KIND:
                tiebreaker = compareQuadsHands(hand, otherHand);
                break;
            case FULL_HOUSE:
                tiebreaker = compareFullHouse(hand, otherHand);
                break;
            case FLUSH:
                tiebreaker = compareFlushHands(hand, otherHand);
                break;
            case STRAIGHT:
                tiebreaker = compareStraightHands(hand, otherHand);
                break;
            case WHEEL:
                tiebreaker = compareWheelHands(hand, otherHand);
                break;
            case SET:
                tiebreaker = compareSetHands(hand, otherHand);
                break;
            case TWO_PAIR:
                tiebreaker = compareTwoPairHands(hand, otherHand);
                break;
            case PAIR:
                tiebreaker = comparePairHands(hand, otherHand);
                break;
            case HIGH_CARD:
                tiebreaker = compareHighCardHands(hand, otherHand);
                break;
            default:
                throw new RuntimeException("Should not reach end of switch statement!");
        }
        return tiebreaker;
    }

    private static int compareRoyalFlushHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.ROYAL_FLUSH);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.ROYAL_FLUSH);

        return PokerHandUtils.TIE;
    }

    private static int compareStraightFlushHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.STRAIGHT_FLUSH);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.STRAIGHT_FLUSH);

        return Integer.compare(hand.getHandAnalyzer().getCards().last().getRank().getRankValue(), otherHand.getHandAnalyzer().getCards().last().getRank().getRankValue());
    }

    private static int compareStraightFlushWheelHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.STRAIGHT_FLUSH_WHEEL);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.STRAIGHT_FLUSH_WHEEL);
        return PokerHandUtils.TIE;
    }

    private static int compareQuadsHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.FOUR_OF_A_KIND);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.FOUR_OF_A_KIND);

        Iterator<Map.Entry<Rank, List<Card>>> handIterator = hand.getHandAnalyzer().getRankGroup().iterator();
        Iterator<Map.Entry<Rank, List<Card>>> otherHandIterator = otherHand.getHandAnalyzer().getRankGroup().iterator();

        int quadsCompare = compareRanks(handIterator, otherHandIterator);

        if (quadsCompare != 0) {
            return quadsCompare;
        }
        return iterateAndCompareHighCard(handIterator, otherHandIterator);
    }

    private static int compareRanks(Iterator<Map.Entry<Rank, List<Card>>> handIterator, Iterator<Map.Entry<Rank, List<Card>>> otherHandIterator) {
        return Integer.compare(handIterator.next().getKey().getRankValue(), otherHandIterator.next().getKey().getRankValue());
    }

    private static int iterateAndCompareHighCard(Iterator<Map.Entry<Rank, List<Card>>> handIterator,
                                                 Iterator<Map.Entry<Rank, List<Card>>> otherHandIterator) {

        while (handIterator.hasNext() && otherHandIterator.hasNext()) {
            int rankComparison = compareRanks(handIterator, otherHandIterator);
            if (rankComparison != 0) {
                return rankComparison;
            }
        }
        return PokerHandUtils.TIE;
    }

    private static int compareFullHouse(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.FULL_HOUSE);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.FULL_HOUSE);

        Iterator<Map.Entry<Rank, List<Card>>> handIterator = hand.getHandAnalyzer().getRankGroup().iterator();
        Iterator<Map.Entry<Rank, List<Card>>> otherHandIterator = otherHand.getHandAnalyzer().getRankGroup().iterator();

        int setCompare = compareRanks(handIterator, otherHandIterator);

        if (setCompare != 0) {
            return setCompare;
        }

        int pairCompare = compareRanks(handIterator, otherHandIterator);

        if (pairCompare != 0) {
            return pairCompare;
        }

        throw new RuntimeException("Should not reach here in 5 cards poker game");
    }

    private static int compareFlushHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.FLUSH);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.FLUSH);
        return iterateAndCompareHighCard(hand.getHandAnalyzer().getRankGroup().iterator(),
                otherHand.getHandAnalyzer().getRankGroup().iterator());
    }

    private static int compareStraightHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.STRAIGHT);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.STRAIGHT);

        return Integer.compare(hand.getHandAnalyzer().getCards().last().getRank().getRankValue(),
                otherHand.getHandAnalyzer().getCards().last().getRank().getRankValue());
    }

    private static int compareWheelHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.WHEEL);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.WHEEL);

        return PokerHandUtils.TIE;
    }

    private static int compareSetHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.SET);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.SET);

        int setCompare = compareRanks(hand.getHandAnalyzer().getRankGroup().iterator(),
                otherHand.getHandAnalyzer().getRankGroup().iterator());

        if (setCompare != 0) {
            return setCompare;
        }

        throw new RuntimeException("Should not reach here in 5 cards poker");
    }

    private static int compareTwoPairHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.TWO_PAIR);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.TWO_PAIR);

        Iterator<Map.Entry<Rank, List<Card>>> handIterator = hand.getHandAnalyzer().getRankGroup().iterator();
        Iterator<Map.Entry<Rank, List<Card>>> otherHandIterator = otherHand.getHandAnalyzer().getRankGroup().iterator();

        int highPairComparison = compareRanks(handIterator, otherHandIterator);
        if (highPairComparison != 0) {
            return highPairComparison;
        }

        int lowPairComparison = compareRanks(handIterator, otherHandIterator);
        if (lowPairComparison != 0 ) {
            return  lowPairComparison;
        }

        return iterateAndCompareHighCard(handIterator, otherHandIterator);
    }

    private static int comparePairHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.PAIR);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.PAIR);

        Iterator<Map.Entry<Rank, List<Card>>> handIterator = hand.getHandAnalyzer().getRankGroup().iterator();
        Iterator<Map.Entry<Rank, List<Card>>> otherHandIterator = otherHand.getHandAnalyzer().getRankGroup().iterator();

        int highPairComparison = compareRanks(handIterator, otherHandIterator);
        if (highPairComparison != 0) {
            return highPairComparison;
        }
        return iterateAndCompareHighCard(handIterator, otherHandIterator);
    }

    private static int compareHighCardHands(Hand hand, Hand otherHand) {
        PokerHandUtils.checkHandClassification(hand, ClassificationRank.HIGH_CARD);
        PokerHandUtils.checkHandClassification(otherHand, ClassificationRank.HIGH_CARD);

        return iterateAndCompareHighCard(hand.getHandAnalyzer().getRankGroup().iterator(), otherHand.getHandAnalyzer().getRankGroup().iterator());
    }


    public static int compareHandClassifications(Hand hand, Hand otherHand) {
        return Integer.compare(hand.getHandAnalyzer().getClassification().getClassificationRank().getValue(),
                otherHand.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }


}
