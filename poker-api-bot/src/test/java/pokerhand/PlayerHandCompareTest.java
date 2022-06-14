package pokerhand;

import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.poker.PokerHand;
import org.derryclub.linebot.poker.analyzer.Hand;
import org.derryclub.linebot.poker.analyzer.PokerHandComparator;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Rank;
import org.derryclub.linebot.poker.card.Suit;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerHandCompareTest {

    private final Player player1 = new Player("1", "Player 1");
    private final Player player2 = new Player("2", "Player 2");

    private final Comparator<Hand> handComparator = new PokerHandComparator();

    @Test
    void two_pair_of_3_4_is_stronger_than_two_pair_of_2_3() {

        // player ranking map
        TreeMap<Hand, Player> playerTreeMap = new TreeMap<>(handComparator);

        // community cards
        Set<Card> communityCards = new TreeSet<>();
        communityCards.add(new Card(Rank.QUEEN, Suit.HEART));
        communityCards.add(new Card(Rank.FIVE, Suit.SPADE));
        communityCards.add(new Card(Rank.QUEEN, Suit.CLUB));
        communityCards.add(new Card(Rank.KING, Suit.HEART));
        communityCards.add(new Card(Rank.JACK, Suit.DIAMOND));


        PokerHand.Builder handBuilder1 = new PokerHand.Builder();
        for (Card each : communityCards) {
            handBuilder1.addCard(each);
        }
        handBuilder1.addCard(new Card(Rank.ACE, Suit.HEART));
        handBuilder1.addCard(new Card(Rank.FIVE, Suit.HEART));
        PokerHand player1hand = handBuilder1.build();
        player1.setHandClassification(player1hand.getHandAnalyzer().getClassification());


        PokerHand.Builder handBuilder2 = new PokerHand.Builder();
        for (Card each : communityCards) {
            handBuilder2.addCard(each);
        }
        handBuilder2.addCard(new Card(Rank.NINE, Suit.SPADE));
        handBuilder2.addCard(new Card(Rank.NINE, Suit.DIAMOND));
        PokerHand player2hand = handBuilder2.build();
        player2.setHandClassification(player2hand.getHandAnalyzer().getClassification());

        playerTreeMap.put(player1hand, player1);
        playerTreeMap.put(player2hand, player2);

        Map<Hand, Player> descendingMap = playerTreeMap.descendingMap();

        Optional<Player> optionalPlayer = descendingMap.values().stream().findFirst();

        assertThat(optionalPlayer).containsSame(player2);
    }
}
