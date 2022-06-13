package org.derryclub.linebot.service.pokergame.util;

import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.poker.PokerHand;
import org.derryclub.linebot.poker.analyzer.Classification;
import org.derryclub.linebot.poker.analyzer.Hand;
import org.derryclub.linebot.poker.analyzer.PokerHandComparator;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.service.pokergame.gamemanage.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;

import java.util.*;
import java.util.stream.Collectors;

public final class GameResultUtilClass {

    private static final Comparator<Hand> comparator = new PokerHandComparator();

    /**
     * User will only invoke this method while the game is in river state
     *
     * @param groupId works as the key to the player map
     * @return SortedSet. The sorted ranking from the strongest hand to the weakest
     */
    public static SortedMap<Hand, Player> getGameResult(String groupId) {

        // only alive players
        Set<Player> players = PlayerManagerImpl.getManager().getPlayers(groupId).stream()
                .filter(player -> player.getPlayerStatue() != Player.PlayerStatus.FOLD)
                .collect(Collectors.toSet());

        List<Card> communityCards = CommunityCardManager.getManager()
                .getCommunityCardsMap()
                .get(groupId);
        /*
          combine Player hole cards with community cards,
          use it to create 7 cards poker hand, sort it, and analyze the hand classification
          compare each 7 card hand using comparator, get a set that from strongest to the weakest
         */
        TreeMap<Hand, Player> pokerHandRankingMap = new TreeMap<>(comparator);

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

            pokerHandRankingMap.put(hand, player);
        }


        CommunityCardManager.getManager().clearCommunityCard(groupId);

        return pokerHandRankingMap;
    }

    public static String cardRankMsg(SortedMap<Hand, Player> playerRanking) {

        StringBuilder revealCardRanking = new StringBuilder();

        Optional<Player> winnerOptional = playerRanking.values().stream().findFirst();

        if (winnerOptional.isPresent()) {
            Player winner = winnerOptional.get();
            revealCardRanking.append("贏家是").append(winner.getUserName())
                    .append("底牌是: ").append(winnerCardSuitConverter(winner))
                    .append(" \n")
                    .append("組成牌型:").append(winner.getHandClassification())
                    .append(" \n");

            return revealCardRanking.toString();
        }
        return "Tie";
    }

    private static String winnerCardSuitConverter(Player player) {
        String playerCardsString = player.getPlayerCards().stream().map(Card::toString)
                .collect(Collectors.joining());

        StringBuilder playerCard = new StringBuilder(playerCardsString);
        switch (playerCard.charAt(1)) {
            case 's':
                playerCard.replace(1, 2, "桃黑");
                break;
            case 'h':
                playerCard.replace(1, 2, "心紅");
                break;
            case 'c':
                playerCard.replace(1, 2, "花梅");
                break;
            case 'd':
                playerCard.replace(1, 2, "塊方");
                break;
        }
        switch (playerCard.charAt(4)) {
            case 's':
                playerCard.replace(4, 5, "桃黑");
                break;
            case 'h':
                playerCard.replace(4, 5, "心紅");
                break;
            case 'c':
                playerCard.replace(4, 5, "花梅");
                break;
            case 'd':
                playerCard.replace(4, 5, "塊方");
                break;
        }

        return playerCard.reverse().toString();
    }
}
