package Poker.Analyzer;

import Card.*;
import Poker.Analyzer.*;
import java.util.Collections;
import java.util.SortedSet;

public class CardHandAnalyzer implements HandAnalyzer {

    private final SortedSet<Card> cards;
    private final Classification handClassification;
    private final RankGroup rankGroup;
    private final SuitGroup suitGroup;

    public CardHandAnalyzer(SortedSet<Card> cards) {
        this.cards = Collections.unmodifiableSortedSet(cards);
        this.rankGroup = new RankGroup(cards);
        this.suitGroup = new SuitGroup(cards);
        this.handClassification = PokerHandUtils.classifyPokerHand(getRankGroup(), getSuitGroup(), getCards());
    }

    @Override
    public SortedSet<Card> getCards() {
        return this.cards;
    }

    @Override
    public Classification getClassification() {
        return this.handClassification;
    }

    @Override
    public RankGroup getRankGroup() {
        return this.rankGroup;
    }

    @Override
    public SuitGroup getSuitGroup() {
        return this.suitGroup;
    }
}
