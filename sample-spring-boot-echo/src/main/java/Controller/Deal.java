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

    public static String deal(MessageEvent<TextMessageContent> event) throws IllegalAccessException {

       /*
       * check if the game exist,
       * if do load the game, else create a new game
       * */
        String userID = event.getSource().getUserId();
        Game game = gameMap.get(userID);

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
                return pokerHand.toString();
            } else {
                return "Game.deal() - if statement. Should not reach here!";
            }
        } else {
            /*
            * create new game and return the card
            * */
            game = new Game(userID, Deck.newShuffledSingleDeck());
            gameMap.put(userID, game);
            String startHand = FivePokerHand.getStartHand(game.getDeck());
            // once deal the cards, move to public state
            game.setGameState(Game.GAME_PUBLIC_STATE);
            return startHand;
        }
    }

    private static StringBuilder appendCard(String cards) {
        return pokerHand.append(cards);
    }

}
