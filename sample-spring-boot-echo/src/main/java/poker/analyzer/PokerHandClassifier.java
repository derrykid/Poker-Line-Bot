package poker.analyzer;

import java.util.*;
import java.util.stream.Collectors;

import card.*;

public class PokerHandClassifier implements HandClassifier {

    private final RankGroup rankGroup;
    private final SuitGroup suitGroup;
    private final SortedSet<Card> cards;

    PokerHandClassifier(RankGroup rankGroup, SuitGroup suitGroup, SortedSet<Card> cards) {
        this.rankGroup = rankGroup;
        this.suitGroup = suitGroup;
        this.cards = cards;
    }

    @Override
    public Classification classify() {
        Classification result = detectImp();
        validateCards(result.getClassifiedCards());
        return result;
    }

    private SortedSet<Card> calculateHighCard() {
        return new TreeSet<>(this.cards.stream().limit(5).collect(Collectors.toSet()));
    }

    private Classification isPair() {
        if (this.rankGroup.getPairCount() == 1) {
            Iterator<Map.Entry<Rank, List<Card>>> rankGroup = this.rankGroup.iterator();
            SortedSet<Card> cards = new TreeSet<>();
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            return new Classification(ClassificationRank.PAIR, cards);
        }
        return new Classification(ClassificationRank.HIGH_CARD, calculateHighCard());
    }

    private Classification detectTwoPair() {
        if (this.rankGroup.getPairCount() == 2) {
            Iterator<Map.Entry<Rank, List<Card>>> rankGroup = this.rankGroup.iterator();
            SortedSet<Card> cards = new TreeSet<>();
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            return new Classification(ClassificationRank.TWO_PAIR, cards);
        }
        return isPair();
    }

    private Classification isSet() {
        // 3 3 3 7 9 J K
        if (this.rankGroup.getSetCount() == 1) {
            Iterator<Map.Entry<Rank, List<Card>>> rankGroup = this.rankGroup.iterator();
            SortedSet<Card> cards = new TreeSet<>();
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            cards.addAll(rankGroup.next().getValue());
            return new Classification(ClassificationRank.SET, cards);
        }
        return detectTwoPair();
    }

    private Classification detectNormalStraight() {
        Set<Rank> cardRanks = this.rankGroup.getRankMap().keySet();

        if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_TEN_TO_ACE)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_TEN_TO_ACE));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_NINE_TO_KING)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_NINE_TO_KING));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_EIGHT_TO_QUEEN)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_EIGHT_TO_QUEEN));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_SEVEN_TO_JACK)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_SEVEN_TO_JACK));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_SIX_TO_TEN)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_SIX_TO_TEN));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_FIVE_TO_NINE)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_FIVE_TO_NINE));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_FOUR_TO_EIGHT)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_FOUR_TO_EIGHT));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_THREE_TO_SEVEN)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_THREE_TO_SEVEN));
        } else if (cardRanks.containsAll(PokerHandUtils.STRAIGHT_TWO_TO_SIX)) {
            return new Classification(ClassificationRank.STRAIGHT, calculateStraight(PokerHandUtils.STRAIGHT_TWO_TO_SIX));
        }
        return isSet();
    }

    private SortedSet<Card> calculateStraight(List<Rank> ranks) {
        SortedSet<Card> results = new TreeSet<>();
        Map<Rank, List<Card>> rankGroup = this.rankGroup.getRankMap();
        for (Rank per: ranks) {
            List<Card> values = rankGroup.get(per);
            if (values != null ){
                results.add(values.iterator().next());
            }
        }
        return results;
    }

    private Classification detectWheel() {
        final List<Rank> wheelRank = Arrays.asList(Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE);
        Set<Rank> handRanks = new TreeSet<>(this.rankGroup.getRankMap().keySet());
        handRanks.retainAll(wheelRank);
        if (handRanks.containsAll(wheelRank)) {
            SortedSet<Card> cards = new TreeSet<>();
            for (Map.Entry<Rank, List<Card>> entry: this.rankGroup.getRankMap().entrySet()) {
                if (wheelRank.contains(entry.getKey())) {
                    cards.add(entry.getValue().iterator().next());
                }
            }
            if (cards.size() != 5) {
                throw new RuntimeException("Something went wrong!");
            }
            return new Classification(ClassificationRank.WHEEL, cards);
        }
        return detectNormalStraight();
    }

    private Classification detectFlush() {
        Map<Suit, List<Card>> suitGroup = this.suitGroup.getSuitMap();
        for (final Map.Entry<Suit, List<Card>> per: suitGroup.entrySet()) {
            if (per.getValue().size() == 5) {
                return new Classification(ClassificationRank.FLUSH, new TreeSet<>(per.getValue()));
            }
        }
        return detectWheel();
    }


    private Classification detectFullHouse() {
        if (this.rankGroup.getSetCount() == 2 || (this.rankGroup.getSetCount() == 1 && this.rankGroup.getPairCount() >= 1)) {
            Iterator<Map.Entry<Rank, List<Card>>> handRankIterator = this.rankGroup.iterator();
            SortedSet<Card> cards = new TreeSet<>();
            cards.addAll(handRankIterator.next().getValue());
            cards.addAll(extraFullHousePair(handRankIterator));
            return new Classification(ClassificationRank.FULL_HOUSE, cards);
        }
        return detectFlush();
    }

    private static Collection<Card> extraFullHousePair(final Iterator<Map.Entry<Rank, List<Card>>> handIterator){
        List<Card> fullHousePair = new ArrayList<>();
        List<Card> pairOrSet = handIterator.next().getValue();

//        AAA '888 1'
        if (pairOrSet.size() == 3) {
            Iterator<Card> remainingCardsIterator = pairOrSet.iterator();
            fullHousePair.add(remainingCardsIterator.next());
            fullHousePair.add(remainingCardsIterator.next());
        } else if (pairOrSet.size() == 2) {
            fullHousePair.addAll(pairOrSet);
        } else {
            throw new RuntimeException("Should not reach here");
        }
        return fullHousePair;
    }

    private Card extraQuadKicker(Iterator<Map.Entry<Rank, List<Card>>> rankGroup) {
        if (!rankGroup.hasNext()) {
            throw new RuntimeException("No kicker to extract");
        }
        SortedSet<Card> remainingCards = new TreeSet<>();
        rankGroup.forEachRemaining(rankListEntry -> remainingCards.addAll(rankListEntry.getValue()));
        return remainingCards.last();
    }

    private Classification detectFourOfAKind() {
        if (this.rankGroup.getQuadCount() == 1) {
            Iterator<Map.Entry<Rank, List<Card>>> rankGroup = this.rankGroup.iterator();
            SortedSet<Card> cards = new TreeSet<>();
            cards.addAll(rankGroup.next().getValue());
            cards.add(extraQuadKicker(rankGroup));
            return new Classification(ClassificationRank.FOUR_OF_A_KIND, cards);
        }
        return detectFullHouse();
    }

    private Classification detectStraightFlush() {
        Map<Suit, List<Card>> suitGroup = this.suitGroup.getSuitMap();

        // there are 4 <K, V> pair : <Spade, List<Card>>, Heart..., diamond,,,, club,,,
        for (Map.Entry<Suit, List<Card>> entry : suitGroup.entrySet()) {
            // go through every suit, if there's one suit have 5 cards:
            if (entry.getValue().size() == 5) {
                Card[] cardArray = entry.getValue().toArray(new Card[entry.getValue().size()]);
                for (int i = 0; i < cardArray.length - 1; i ++) {
                    if (cardArray[i].getRank().getRankValue() != cardArray[i + 1].getRank().getRankValue() - 1) {
                        return detectFourOfAKind();
                    }
                }
                return new Classification(ClassificationRank.STRAIGHT_FLUSH, new TreeSet<>(entry.getValue()));
            }
        }
        return detectFourOfAKind();
    }

    private Classification detectStraightFlushWheel() {
        List<Card> handCards = new ArrayList<>(this.cards);
        if (handCards.containsAll(PokerHandUtils.STRAIGHT_WHEEL_SPADES)) {
            handCards.retainAll(PokerHandUtils.STRAIGHT_WHEEL_SPADES);
            return new Classification(ClassificationRank.STRAIGHT_FLUSH_WHEEL, new TreeSet<>(handCards));
        } else if (handCards.containsAll(PokerHandUtils.STRAIGHT_WHEEL_HEARTS)) {
            handCards.retainAll(PokerHandUtils.STRAIGHT_WHEEL_HEARTS);
            return new Classification(ClassificationRank.STRAIGHT_FLUSH_WHEEL, new TreeSet<>(handCards));
        } else if (handCards.containsAll(PokerHandUtils.STRAIGHT_WHEEL_CLUBS)) {
            handCards.retainAll(PokerHandUtils.STRAIGHT_WHEEL_CLUBS);
            return new Classification(ClassificationRank.STRAIGHT_FLUSH_WHEEL, new TreeSet<>(handCards));
        } else if (handCards.containsAll(PokerHandUtils.STRAIGHT_WHEEL_DIAMONDS)) {
            handCards.retainAll(PokerHandUtils.STRAIGHT_WHEEL_DIAMONDS);
            return new Classification(ClassificationRank.STRAIGHT_FLUSH_WHEEL, new TreeSet<>(handCards));
        }
        return detectStraightFlush();
    }

    private Classification detectRoyalFlush() {
        final List<Card> handCards = new ArrayList<>(this.cards);
        if (handCards.containsAll(PokerHandUtils.ROYAL_FLUSH_SPADES)) {
            handCards.retainAll(PokerHandUtils.ROYAL_FLUSH_SPADES);
            return new Classification(ClassificationRank.ROYAL_FLUSH, new TreeSet<>(handCards));
        } else if (handCards.containsAll(PokerHandUtils.ROYAL_FLUSH_HEARTS)) {
            handCards.retainAll(PokerHandUtils.ROYAL_FLUSH_HEARTS);
            return new Classification(ClassificationRank.ROYAL_FLUSH, new TreeSet<>(handCards));
        } else if (handCards.containsAll(PokerHandUtils.ROYAL_FLUSH_CLUBS)) {
            handCards.retainAll(PokerHandUtils.ROYAL_FLUSH_CLUBS);
            return new Classification(ClassificationRank.ROYAL_FLUSH, new TreeSet<>(handCards));
        } else if (handCards.containsAll(PokerHandUtils.ROYAL_FLUSH_DIAMONDS)) {
            handCards.retainAll(PokerHandUtils.ROYAL_FLUSH_DIAMONDS);
            return new Classification(ClassificationRank.ROYAL_FLUSH, new TreeSet<>(handCards));
        }
        return detectStraightFlushWheel();
    }

    private Classification detectImp() {
        return detectRoyalFlush();
    }

    private static void validateCards(final SortedSet<Card> cards) {
        if (cards.size() != 5) {
            throw new RuntimeException("Invalid cards: " + cards);
        }
    }


}
