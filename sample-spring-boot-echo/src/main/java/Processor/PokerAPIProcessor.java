package Processor;


/*
 * This class sends the dealt cards to the Poker API and gets the winner
 * */

import Card.*;
import Game.Player;
import Poker.Analyzer.Classification;
import Poker.PokerHand;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class PokerAPIProcessor {

    /*
     * While use this method, players are in river state and decided to show hands
     * */
    // TODO modify the return type
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

    // deprecated, the request doesn't work
//    public static String getWinner(ArrayList<Player> playerSortedList, Set<Card> communityCards) throws URISyntaxException, IOException, InterruptedException {
//
//        // get the poker api uri
//        String apiAddressRoot = Constant.POKER_API.getUri();
//        // form the request uri
//        StringBuilder requestURI = new StringBuilder();
//
//
//        StringBuilder dealtCardBuilder = GameController.getDealtCard(groupID);
//        int playerNo = playerSortedList.size();
//
//        // separate each card with a comma
//        StringBuilder communityCardStringBuilder = communityCardProcessor(dealtCardBuilder, playerNo);
//        StringBuilder holeCardsStringBuilder = holeCardProcessor(dealtCardBuilder, playerNo);
//
//        // this is the request
//        requestURI.append(apiAddressRoot).append(communityCardStringBuilder).append(holeCardsStringBuilder);
//
//        // TODO decide the response type
//        String value = request(requestURI);
//
//        return value;
//    }
    private static String request(StringBuilder requestURI) throws URISyntaxException, IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder().uri(new URI(requestURI.toString())).GET().build();
//        HttpClient client = HttpClient.newBuilder().build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        // TODO modify the return type to map
//        return response.body();
        return null;
    }

    private static StringBuilder holeCardProcessor(StringBuilder dealtCardBuilder, int playerNo) {
        ArrayList<StringBuilder> aCardisAddedIndividually = new ArrayList<>();
        for (int i = 0; i < playerNo * 4; i++) {
            int startInt = i;
            int endInt = i + 1;
            String aCard = dealtCardBuilder.substring(startInt, endInt + 1);
            aCardisAddedIndividually.add(new StringBuilder(aCard));
            i++;
        }

        StringBuilder playerCardRequest = new StringBuilder();
        for (int i = 0; i < playerNo * 2; i++) {
            StringBuilder card1 = aCardisAddedIndividually.get(i);
            StringBuilder card2 = aCardisAddedIndividually.get(i + 1);
            playerCardRequest.append("&pc[]=").append(card1).append(',').append(card2);
            i++;
        }

        String requestModify = playerCardRequest.toString().replaceAll("T|t", "10");
        return new StringBuilder(requestModify);
    }

    private static StringBuilder communityCardProcessor(StringBuilder dealtCardBuilder, int playerNo) {
        StringBuilder communityCards = new StringBuilder(dealtCardBuilder.substring(playerNo * 4));
        char[] communityCardsChar = communityCards.toString().toCharArray();
        StringBuilder communityCardsSeparateWithComma = new StringBuilder();
        for (int i = 0; i < 4 * 2; i++) {
            communityCardsSeparateWithComma.append(communityCardsChar[i]).append(communityCardsChar[i + 1]).append(',');
            i++;
        }
        String modifyIfThereT = communityCardsSeparateWithComma.toString().replaceAll("T|t", "10");
        return new StringBuilder(modifyIfThereT);
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
