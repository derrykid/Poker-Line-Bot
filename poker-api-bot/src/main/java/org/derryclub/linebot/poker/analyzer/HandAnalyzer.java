package org.derryclub.linebot.poker.analyzer;

import org.derryclub.linebot.card.Card;

import java.util.SortedSet;

public interface HandAnalyzer {
    SortedSet<Card> getCards();

    Classification getClassification();

    RankGroup getRankGroup();

    SuitGroup getSuitGroup();
}
