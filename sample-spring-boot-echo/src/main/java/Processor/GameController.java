package Processor;

import Game.*;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import poker.Deal;
import poker.Deck;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameController {

    /*
     * Map<GroupID, PlayerList>
     *  */
    private static Map<String, ArrayList<Player>> playersInTheGroup = new HashMap<>();

    /*
    * Map<GroupID, Game>
    * The Game holds Deck, game status
    * */
    private static Map<String, Game> gameMap = new HashMap<>();

    private static StringBuilder boardCards = new StringBuilder();

    public static void create(String groupID) {
        Game game = new Game(Deck.newShuffledSingleDeck());
        game.setGameState(Game.GAME_ADDING_PLAYER);
        gameMap.put(groupID, game);
        playersInTheGroup.put(groupID, new ArrayList<>());
    }

    public static TextMessage handle(MessageEvent<TextMessageContent> event) {
        // TODO player add event
        String groupID = event.getSource().getSenderId();
        Game game = gameMap.get(event.getSource().getSenderId());
        int gameState = game.getGameState();

        if (gameState == Game.GAME_ADDING_PLAYER){

            ArrayList<Player> players = playersInTheGroup.get(groupID);
            String userText = event.getMessage().getText();
            // if user use /end command, see if there's at least 2 players
            if (players.size() >= 2 && userText.equals("/end")) {
                game.setGameState(Game.GAME_PREFLOP);
                // TODO players get hole cards
                return new TextMessage("遊戲開始！");
            }

            // TODO how to get the player? I only have their userID
            if (players.contains()) {

            } else {
                addPlayer(event);
            }
            return null;
        }

        switch (gameState){
            default:
                return new TextMessage("Error occurs! Please report me!");
        }


        // after players added, play the game
        // TODO a game on going, check proceed depends on the state

        return null;
    }

    private static void addPlayer(MessageEvent<TextMessageContent> event) {
        String userText = event.getMessage().getText();
        if (userText.equalsIgnoreCase("+1")) {
            // this user wants to play, add to the playerMap
            String userID = event.getSource().getUserId();
            playersInTheGroup.get(event.getSource().getSenderId()).add(new Player(userID));
        }
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

            if (game.getGameState() == Game.GAME_FLOP){
                for (int i = 0; i < 3; i++) {
                    String publicCard = Deal.getCard(deck);
                    appendCard(publicCard);
                }
                game.setGameState(Game.GAME_TURN_STATE);
                return boardCards.toString();
            } else if (game.getGameState() == Game.GAME_TURN_STATE) {
                String turnCard = Deal.getCard(deck);
                appendCard(turnCard);
                game.setGameState(Game.GAME_RIVER_STATE);
                return boardCards.toString();
            } else if (game.getGameState() == Game.GAME_RIVER_STATE) {
                String riverCard = Deal.getCard(deck);
                appendCard(riverCard);

                /*
                * game over state
                * send the request to poker API, calculate the pots
                * finally destroy the game object
                * */
                game.setGameState(Game.GAME_OVER);
                return boardCards.toString();
            } else {
                return "Game.deal() - if statement. Should not reach here!";
            }
        } else {
            /*
            * create new game and return the card
            * */
            game = new Game(Deck.newShuffledSingleDeck());
            gameMap.put(groupID, game);
            String startHand = Deal.getStartHand(game.getDeck());
            // once deal the cards, move to public state
            game.setGameState(Game.GAME_FLOP);
            return startHand;
        }
    }

    private static StringBuilder appendCard(String cards) {
        return boardCards.append(cards);
    }

    public static Map<String, Game> getGameMap() {
        return gameMap;
    }

    public static int getOngoingGame(){
        return gameMap.size();
    }

    public static boolean isGameExist(MessageEvent<TextMessageContent> event) {
        return gameMap.get(event.getSource().getSenderId()) != null;
    }

    public static String proceed(MessageEvent<TextMessageContent> event) throws IllegalAccessException {
        return deal(event);
    }

}
