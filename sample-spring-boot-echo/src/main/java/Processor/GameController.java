package Processor;

import Constant.BotCommand;
import Game.*;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import poker.Deal;
import poker.Deck;

import java.util.*;

public class GameController {

    /*
     * Map<GroupID, Map<userID, Player<userID>>>
     *  */
    private static HashMap<String, HashMap<String, Player>> playersInTheGroup = new HashMap<>();

    /*
     * Map<GroupID, Game>
     * The Game holds Deck, game status
     * */
    private static Map<String, Game> gameMap = new HashMap<>();

    /*
     * Map<GroupID, dealt cards>
     * This map stores the dealt cards that correspond to the group
     * */
    private static HashMap<String, DealtCardProcessor> dealtCards = new HashMap<>();

    private static HashMap<String, List<Player>> tablePos = new HashMap<>();

    public static void create(String groupID) {
        Game game = new Game(Deck.newShuffledSingleDeck());
        game.setGameState(Game.GAME_ADDING_PLAYER);
        gameMap.put(groupID, game);
        playersInTheGroup.put(groupID, new HashMap<>());
    }

    public static Message handle(MessageEvent<TextMessageContent> event) throws IllegalAccessException {
        /*
         * Even if in game state, user still can input some commands
         * */
        String text = event.getMessage().getText();
        if (text.equalsIgnoreCase("haha")) {
            return BotCommandProcessor.handle(event);
        }


        String groupID = event.getSource().getSenderId();
        String userID = event.getSource().getUserId();
        Game game = gameMap.get(event.getSource().getSenderId());
        Deck deck = game.getDeck();

        int gameState = game.getGameState();

        /*
         * this part initialises the game, and deal cards out
         * */
        if (gameState == Game.GAME_ADDING_PLAYER) {

            HashMap<String, Player> participantsInGroup = playersInTheGroup.get(groupID);
            String userText = event.getMessage().getText();

            // if user use /end command, see if there's at least 2 players
            // then start the game
            if (participantsInGroup.size() >= 2 && userText.equals("/end")) {
                game.setGameState(Game.GAME_PREFLOP);
                DealtCardProcessor dealtCardProcessor = new DealtCardProcessor(deck);
                dealtCards.put(groupID, dealtCardProcessor);
                /*
                 * get hole cards : push msg to user
                 * */
                List<Player> playerPosList = TablePosition.position(participantsInGroup);
                tablePos.put(groupID, playerPosList);
                // push message to user
                dealtHoleCards(groupID, playerPosList, deck);
                // TODO report position
                String apiMessage = apiMessage(game, playerPosList);
                TextMessage message = new TextMessage("遊戲開始！已將牌私訊發給玩家" + "\n" + apiMessage);
                return message;
            }

            /*
             * add player, filter out the same player +1
             * */
            // TODO logic error, it can add a lot of times
            if (playersInTheGroup.containsKey(userID) && userText.equalsIgnoreCase("+1")) {
                // TODO this message can be reomoved later on
                return new TextMessage("You were added!!!");
            } else if (userText.equalsIgnoreCase("+1")){
                addPlayer(event);
                return new TextMessage("welcome!");
            } else {
                return null;
            }
        }

        /*
         * This part handles preflop, flop, turn, river
         * */
        List<Player> playerPositionList = tablePos.get(groupID);

        /*
         * what players say should proceed the game?
         * */
        switch (gameState) {
            case Game.GAME_PREFLOP:
                // TODO betting event
                String message = apiMessage(game, playerPositionList);
//                gamePreflop(playerPositionList, deck);
                return new TextMessage(message);
            case Game.GAME_FLOP:
                // TODO betting event
//                gameFlopAndTurnAndRiver(playerPositionList, deck);
                break;
            case Game.GAME_TURN_STATE:
                // TODO betting event
//                gameFlopAndTurnAndRiver(playerPositionList, deck);
                break;
            case Game.GAME_RIVER_STATE:
                //TODO betting event
//                gameFlopAndTurnAndRiver(playerPositionList, deck);
                break;
            default:
                return new TextMessage("Error occurs! Please report me!");
        }

        // TODO finish the card dealt operations
        return null;
    }

    private static String apiMessage(Game game, List<Player> playerPosList) {
        StringBuilder messageBuilder = null;
        if (game.getGameState() == Game.GAME_PREFLOP) {
            messageBuilder = new StringBuilder();
            int i = 1;
            for (Player per : playerPosList) {
                messageBuilder.append(per.getUserID()).append(" 位置" + i + "\n");
                i++;
            }
        }
        return messageBuilder.toString();
    }

    private static void dealtHoleCards(String groupID, List<Player> participants, Deck deck) throws IllegalAccessException {
        /*
         * 1. Deal cards to participants: push message
         * 2. add the dealt ones to DealtCardProcessor StringBuilder
         * */

        for (Player per : participants) {
            /*
             * 1. Deal cards
             * The list is already sorted, so we can deal card directly
             * */
            StringBuilder cardsBuilder = new StringBuilder();
            for (int i = 0; i < 2; i++) {
                cardsBuilder.append(Deal.getCard(deck));
            }
            /*
             * 2. add dealt cards
             * add dealt card to the dealt card processor
             * the StringBuilder variable will store the cards dealt
             * */
            DealtCardProcessor dealtCardProcessor = dealtCards.get(groupID);
            dealtCardProcessor.append(cardsBuilder);

            /*
             * push message to push the cards to each player
             * */
            String cards = cardsBuilder.toString();
            pushCustomMessage.pushHoleCards(per.getUserID(), cards);

        }

    }

    private static void addPlayer(MessageEvent<TextMessageContent> event) {
        String userText = event.getMessage().getText();
        if (userText.equalsIgnoreCase("+1")) {
            // this user wants to play, add to the playerMap
            String userID = event.getSource().getUserId();
            playersInTheGroup.get(event.getSource().getSenderId()).put(userID, new Player(userID));
        }
    }

    public static String deal(Deck deck) throws IllegalAccessException {
        /*
         * This method only focus on deal the card. To players, and on board
         * */
        String card = Deal.getCard(deck);
        return card;
    }


    public static Map<String, Game> getGameMap() {
        return gameMap;
    }

    public static int getOngoingGame() {
        return gameMap.size();
    }

    public static boolean isGameExist(MessageEvent<TextMessageContent> event) {
        return gameMap.get(event.getSource().getSenderId()) != null;
    }


}
