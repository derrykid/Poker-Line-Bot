package poker.analyzer;

import card.Card;

import java.util.SortedSet;

public interface HandAnalyzer {
    SortedSet<Card> getCards();

    Classification getClassification();

    RankGroup getRankGroup();

    SuitGroup getSuitGroup();
}
