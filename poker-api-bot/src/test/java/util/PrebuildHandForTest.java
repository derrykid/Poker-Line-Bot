package util;

import org.derryclub.linebot.poker.PokerHand;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Rank;
import org.derryclub.linebot.poker.card.Suit;

public class PrebuildHandForTest {

    public final PokerHand highCard;
    public final PokerHand pair;
    public final PokerHand twoPair;
    public final PokerHand set;
    public final PokerHand wheelStraight;
    public final PokerHand straight;
    public final PokerHand flush;
    public final PokerHand fullHouse;
    public final PokerHand fourOfAKind;
    public final PokerHand wheelFlush;
    public final PokerHand flushStraight;
    public final PokerHand royalFlush;

    public PrebuildHandForTest(){
        this.highCard = highCardInit();
        this.pair = pairInit();
        this.twoPair = twoPairInit();
        this.set = setInit();
        this.wheelStraight = wheelStraightInit();
        this.straight = straightInit();
        this.flush = flushInit();
        this.fullHouse = fullHouseInit();
        this.fourOfAKind = fourOfAKindInit();
        this.wheelFlush = wheelFlushInit();
        this.flushStraight = flushStraightInit();
        this.royalFlush = royalFlushInit();
    }

    private PokerHand royalFlushInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.KING, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.QUEEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.JACK, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.TEN, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));

        return handBuilder.build();
    }

    private PokerHand flushStraightInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.TWO, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SIX, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand wheelFlushInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.TWO, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand fourOfAKindInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.ACE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.ACE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.ACE, Suit.DIAMOND));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand fullHouseInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.THREE, Suit.SPADE));

        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.HEART));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand flushInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.TEN, Suit.CLUB));

        return handBuilder.build();
    }

    private PokerHand straightInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));

        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));
        handBuilder.addCard(new Card(Rank.JACK, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand wheelStraightInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.ACE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.TWO, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand setInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));
        handBuilder.addCard(new Card(Rank.THREE, Suit.SPADE));

        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand twoPairInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand pairInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.THREE, Suit.HEART));

        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

    private PokerHand highCardInit() {
        PokerHand.Builder handBuilder = new PokerHand.Builder();
        handBuilder.addCard(new Card(Rank.THREE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FOUR, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.FIVE, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.SEVEN, Suit.CLUB));
        handBuilder.addCard(new Card(Rank.EIGHT, Suit.HEART));
        handBuilder.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder.addCard(new Card(Rank.TEN, Suit.HEART));

        return handBuilder.build();
    }

}
