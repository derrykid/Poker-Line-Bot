package org.derryclub.linebot.poker;

import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.analyzer.CardHandAnalyzer;
import org.derryclub.linebot.poker.analyzer.Hand;
import org.derryclub.linebot.poker.analyzer.HandAnalyzer;

import java.util.SortedSet;
import java.util.TreeSet;

/*
 * This is the API entry point. The sorting algorithm is in Analyzer package
 * */
public class PokerHand implements Hand {
    private final CardHandAnalyzer handAnalyzer;

    private static final int POKER_HAND_SIZE = 7;

    public PokerHand(Builder builder) { this.handAnalyzer = new CardHandAnalyzer(builder.cards); }

    @Override
    public HandAnalyzer getHandAnalyzer() {
        return this.handAnalyzer;
    }

    public static class Builder {

        private final SortedSet<Card> cards;

        public Builder() {
            this.cards = new TreeSet<>();
        }

        public Builder addCard(Card card) {
            this.cards.add(card);
            return this;
        }

        public PokerHand build() {
            if (this.cards.size() != POKER_HAND_SIZE) {
                throw new RuntimeException("Invalid hand size for hand " + this.cards);
            }
            return new PokerHand(this);
        }

    }
}
