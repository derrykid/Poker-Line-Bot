package org.derryclub.linebot.poker.analyzer;

import org.derryclub.linebot.poker.card.Card;

import java.util.SortedSet;

public interface HandAnalyzer {
    SortedSet<Card> getCards();

    Classification getClassification();

    RankGroup getRankGroup();

    SuitGroup getSuitGroup();
}
