package Processor;


/*
 * This class sends the dealt cards to the Poker API and gets the winner
 * */

import Card.*;
import Constant.Constant;
import Game.Player;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PokerAPIProcessor {

    /*
     * While use this method, players are in turn state and decided to show hands
     * */
    // TODO modify the return type
    public static String process(ArrayList<Player> playerSortedList, Set<Card> communityCards) throws URISyntaxException, IOException, InterruptedException {
        // example request uri
        // https://api.pokerapi.dev/v1/winner/texas_holdem?cc=AC,KD,QH,JS,7C&pc[]=10S,8C&pc[]=3S,2C

        String apiAddressRoot = Constant.POKER_API.getUri();
        StringBuilder requestURI = new StringBuilder();


        StringBuilder dealtCardBuilder = GameController.getDealtCard(groupID);
        int playerNo = playerSortedList.size();

        // separate each card with a comma
        StringBuilder communityCardStringBuilder = communityCardProcessor(dealtCardBuilder, playerNo);
        StringBuilder holeCardsStringBuilder = holeCardProcessor(dealtCardBuilder, playerNo);

        // this is the request
        requestURI.append(apiAddressRoot).append(communityCardStringBuilder).append(holeCardsStringBuilder);

        // TODO decide the response type
        String value = request(requestURI);

        return value;
    }

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


}
// TODO save it to the map, next time access it
