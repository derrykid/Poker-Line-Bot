package org.derryclub.linebot.processor;


/*
 * This class sends the dealt cards to the Poker API and gets the winner
 * */

import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.game.player.Player;
import org.derryclub.linebot.poker.analyzer.Classification;
import org.derryclub.linebot.poker.PokerHand;

import java.util.*;

public class PokerAPIProcessor {

    /*
     * While use this method, players are in river state and decided to show hands
     * */
    public static SortedSet<Player> getGameResult(Set<Player> players, List<Card> communityCards) {
        /*
         * composite Player hole cards with community cards,
         * use it to create 7 cards poker hand, sort it, and analyze the hand classification
         * compare each 7 card hand using comparator, get a set that from strongest to the weakest
         * */
        // TODO maybe can use stream to do this
        SortedSet<Player> playerHandRank = new TreeSet<>(Comparator.comparingInt(o -> -o.getHandClassification().getClassificationRank().getValue()));

        for (Player player : players) {
            Set<Card> playerCardsWithCommunityCards = new TreeSet<>();
            Set<Card> playerCards = player.getPlayerCards();
            playerCardsWithCommunityCards.addAll(playerCards);
            playerCardsWithCommunityCards.addAll(communityCards);

            PokerHand.Builder handBuilder = new PokerHand.Builder();
            for (Card card : playerCardsWithCommunityCards) {
                handBuilder.addCard(card);
            }
            PokerHand hand = handBuilder.build();

            // use for debug
            Classification classification = hand.getHandAnalyzer().getClassification();
            Set<Card> player7Cards = hand.getHandAnalyzer().getCards();

            player.setHandClassification(classification);
            System.out.println(player.getUserName());
            // 5 cc with player hole cards
            System.out.println("This is 7 cards:" + player7Cards);
            System.out.println("This is classification " + classification);
            playerHandRank.add(player);
        }
        return playerHandRank;
    }

    public static String cardRankMsg(SortedSet<Player> playerRanking) {
        StringBuilder revealCardRanking = new StringBuilder();


        for (Player player : playerRanking){
            Set<Card> playerCards = player.getPlayerCards();
            // winner is ranking higher
            revealCardRanking.append("最大牌型玩家是")
                    .append(player.getUserName())
                    .append(" 底牌是: ").append(playerCards)
                    .append(" \n")
                    .append("組成牌型")
                    .append(player.getHandClassification())
                    .append(" \n");
            System.out.println(revealCardRanking);
        }

        return revealCardRanking.toString();
    }
}
