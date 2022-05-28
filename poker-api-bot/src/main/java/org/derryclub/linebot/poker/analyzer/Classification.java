package org.derryclub.linebot.poker.analyzer;

import org.derryclub.linebot.poker.card.Card;

import java.util.Collections;
import java.util.SortedSet;

public class Classification {

    private final ClassificationRank classificationRank;
    private final SortedSet<Card> classifiedCards;

    Classification(ClassificationRank classificationRank, SortedSet<Card> cards) {
        this.classificationRank = classificationRank;
        this.classifiedCards = Collections.unmodifiableSortedSet(cards);
    }

    public SortedSet<Card> getClassifiedCards() {
        return this.classifiedCards;
    }

    public ClassificationRank getClassificationRank() {
        return this.classificationRank;
    }

    public String toString() {
        return this.classificationRank.toString();
    }
}
