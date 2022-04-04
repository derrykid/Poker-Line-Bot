package Controller;

import Service.Game;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import poker.Deck;
import poker.FivePokerHand;

import java.util.HashMap;
import java.util.Map;

public class Deal {

    private static Map<String, Game> gameMap = new HashMap<>();
    private static StringBuilder pokerHand = new StringBuilder();

    public static Map<String, Game> getGameMap() {
        return gameMap;
    }

    public static String deal(MessageEvent<TextMessageContent> event) throws IllegalAccessException {

       /*
       * check if the game exist,
       * if do load the game, else create a new game
       * */
        String groupID = event.getSource().getSenderId();
        Game game = gameMap.get(groupID);

        // means it's already in game state
        if (game != null) {
           /*
           * if enter this blocks, it means user has its card deal already
           * */
            Deck deck = game.getDeck();

            // TODO add more players
//            int totalPlayerNumber = 1;

            if (game.getGameState() == Game.GAME_PUBLIC_STATE){
                for (int i = 0; i < 3; i++) {
                    String publicCard = FivePokerHand.getCard(deck);
                    appendCard(publicCard);
                }
                game.setGameState(Game.GAME_TURN_STATE);
                return pokerHand.toString();
            } else if (game.getGameState() == Game.GAME_TURN_STATE) {
                String turnCard = FivePokerHand.getCard(deck);
                appendCard(turnCard);
                game.setGameState(Game.GAME_RIVER_STATE);
                return pokerHand.toString();
            } else if (game.getGameState() == Game.GAME_RIVER_STATE) {
                String riverCard = FivePokerHand.getCard(deck);
                appendCard(riverCard);

                /*
                * game over state
                * send the request to poker API, calculate the pots
                * finally destroy the game object
                * */
                game.setGameState(Game.GAME_OVER);
                return pokerHand.toString();
            } else {
                return "Game.deal() - if statement. Should not reach here!";
            }
        } else {
            /*
            * create new game and return the card
            * */
            game = new Game(groupID, Deck.newShuffledSingleDeck());
            gameMap.put(groupID, game);
            String startHand = FivePokerHand.getStartHand(game.getDeck());
            // once deal the cards, move to public state
            game.setGameState(Game.GAME_PUBLIC_STATE);
            return startHand;
        }
    }

    private static StringBuilder appendCard(String cards) {
        return pokerHand.append(cards);
    }


    public static boolean isExist(MessageEvent<TextMessageContent> event) {
        return gameMap.get(event.getSource().getSenderId()) != null;
    }

    public static String proceed(MessageEvent<TextMessageContent> event) throws IllegalAccessException {
        return deal(event);
    }
}
