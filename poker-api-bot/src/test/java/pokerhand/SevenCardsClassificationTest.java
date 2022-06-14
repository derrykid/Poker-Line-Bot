package pokerhand;

import org.derryclub.linebot.poker.PokerHand;
import org.derryclub.linebot.poker.analyzer.Classification;
import org.derryclub.linebot.poker.analyzer.ClassificationRank;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Deck;
import org.derryclub.linebot.poker.card.Rank;
import org.derryclub.linebot.poker.card.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SevenCardsClassificationTest {

    @DisplayName("High card classification")
    @Test
    void HighCard_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.HIGH_CARD.getName());
    }

    @DisplayName("Pair  classification")
    @Test
    void Pair_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.PAIR.getName());
    }

    @DisplayName("Two pair classification")
    @Test
    void TwoPair_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.TWO_PAIR.getName());
    }

    @DisplayName("Set classification")
    @Test
    void Set_Classification_Test(){

        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.THREE, Suit.SPADE));

        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.SET.getName());
    }

    @DisplayName("Straight")
    @Test
    void Straight_Classification_Test(){

        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));
        handBuilder.addCard(new Card(Rank.JACK, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.STRAIGHT.getName());
    }

    @DisplayName("Wheel straight")
    @Test
    void WheelStraight_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.TWO, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.WHEEL.getName());
    }

    @DisplayName("Flush classification")
    @Test
    void Flush_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.TEN, Suit.CLUB));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.FLUSH.getName());
    }

    @DisplayName("Full House classification")
    @Test
    void FullHouse_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.THREE, Suit.SPADE));

        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.HEART));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.FULL_HOUSE.getName());
    }

    @DisplayName("Four of a kind Classification")
    @Test
    void FourOfAKind_Classification_Test(){

        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.ACE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.ACE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.ACE, Suit.DIAMOND));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.FOUR_OF_A_KIND.getName());
    }

    @DisplayName("Wheel flush classification")
    @Test
    void WheelFlushStraight_Classification_Test(){
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.TWO, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.STRAIGHT_FLUSH_WHEEL.getName());
    }

    @DisplayName("Flush Straight classification")
    @Test
    void FlushStraight_Classification_Test(){

        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.TWO, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SIX, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.STRAIGHT_FLUSH.getName());
    }

    @DisplayName("Royal flush classification")
    @Test
    void RoyalFlushStraight_Classification_Test(){

        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.KING, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.QUEEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.JACK, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.TEN, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));

        PokerHand hand = handBuilder.build();
        Classification classification = hand.getHandAnalyzer().getClassification();
        assertThat(classification.toString()).isSameAs(ClassificationRank.ROYAL_FLUSH.getName());
    }

}
