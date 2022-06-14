package pokerhand;

import org.derryclub.linebot.poker.PokerHand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.PrebuildHandForTest;

import static org.assertj.core.api.Assertions.assertThat;

public class PokerHandComparisonTest {

    private PrebuildHandForTest util;

    @BeforeEach
    void initHand() {
        this.util = new PrebuildHandForTest();
    }

    @Test
    void FourOfAKind_is_stronger_than_Set() {
        PokerHand fourOfAKind = util.fourOfAKind;
        PokerHand set = util.set;

        assertThat(fourOfAKind.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(set.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void pair_is_weaker_than_set(){
        PokerHand pair = util.pair;
        PokerHand set = util.set;
        assertThat(pair.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isLessThan(set.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }


    @Test
    void set_is_stronger_than_two_pair(){
        PokerHand set = util.set;
        PokerHand twoPair = util.twoPair;

        assertThat(set.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(twoPair.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void FullHouse_is_stronger_than_straight(){
        PokerHand fullHouse = util.fullHouse;
        PokerHand straight = util.straight;
        assertThat(fullHouse.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(straight.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void wheel_straight_is_weaker_than_straight(){
        PokerHand wheel = util.wheelStraight;
        PokerHand straight = util.straight;

        assertThat(wheel.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isLessThan(straight.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void four_of_a_kind_is_stronger_than_fullHouse() {
        PokerHand fourOfAKind = util.fourOfAKind;
        PokerHand fullHouse = util.fullHouse;

        assertThat(fourOfAKind.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(fullHouse.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void straight_is_weaker_than_flush() {
        PokerHand straight = util.straight;
        PokerHand flush = util.flush;

        assertThat(straight.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isLessThan(flush.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void royal_flush_is_stronger_than_wheel_flush(){
        PokerHand royalFlush = util.royalFlush;
        PokerHand wheelFlush = util.wheelFlush;

        assertThat(royalFlush.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(wheelFlush.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void royal_flush_is_stronger_than_set(){

        PokerHand royalFlush = util.royalFlush;
        PokerHand set = util.set;

        assertThat(royalFlush.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(set.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }

    @Test
    void two_pair_is_stronger_than_a_pair(){

        PokerHand twoPair = util.twoPair;
        PokerHand pair = util.pair;

        assertThat(twoPair.getHandAnalyzer().getClassification().getClassificationRank().getValue())
                .isGreaterThan(pair.getHandAnalyzer().getClassification().getClassificationRank().getValue());
    }


}
