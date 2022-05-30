package org.derryclub.linebot.service.pokergame.util;

import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.poker.analyzer.Classification;
import org.derryclub.linebot.poker.PokerHand;
import org.derryclub.linebot.service.pokergame.gameinstances.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;

import java.util.*;

public final class GameResultUtilClass {

    /**
     * User will only invoke this method while the game is in river state
     * @param groupId works as the key to the player map
     * @return SortedSet. The sorted ranking from the strongest hand to the weakest
     */
    public static SortedSet<Player> getGameResult(String groupId) {

        Set<Player> players = PlayerManagerImpl.getManager().getPlayers(groupId);

        List<Card> communityCards = CommunityCardManager.getManager()
                .getCommunityCardsMap()
                .get(groupId);
        /*
          combine Player hole cards with community cards,
          use it to create 7 cards poker hand, sort it, and analyze the hand classification
          compare each 7 card hand using comparator, get a set that from strongest to the weakest
         */
        SortedSet<Player> playerHandRank = new TreeSet<>(Comparator.comparingInt(
                o -> -o.getHandClassification().getClassificationRank().getValue()));

        for (Player player : players) {
            // combine to 7 cards
            Set<Card> playerCardsWithCommunityCards = new TreeSet<>();
            Set<Card> playerCards = player.getPlayerCards();
            playerCardsWithCommunityCards.addAll(playerCards);
            playerCardsWithCommunityCards.addAll(communityCards);

            // create a PokerHand of 7 cards
            PokerHand.Builder handBuilder = new PokerHand.Builder();
            for (Card card : playerCardsWithCommunityCards) {
                handBuilder.addCard(card);
            }
            PokerHand hand = handBuilder.build();
            Classification classification = hand.getHandAnalyzer().getClassification();
            player.setHandClassification(classification);

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
