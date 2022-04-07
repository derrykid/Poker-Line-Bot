package Processor;

import Constant.GameCommand;
import Game.*;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import poker.Deal;
import poker.Deck;

import java.util.*;

public class GameController {

    private static Set<String> gameCommands = new HashSet<>(GameCommand.getGameCommandList());

    /*
     * Map<GroupID, Map<userID, Player<userID>>>
     *  */
    private static HashMap<String, Set<Player>> playersInTheGroup = new HashMap<>();

    public static Set<Player> getPlayersInTheGroup(String groupID) {
        return playersInTheGroup.get(groupID);
    }

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

    public String getDealtCards(String groupID) {
        return dealtCards.get(groupID).getDealtCards().toString();
    }

    private static HashMap<String, HashSet<Player>> tablePos = new HashMap<>();

    public static void create(String groupID) {
        Game game = new Game(Deck.newShuffledSingleDeck());
        game.setGameState(Game.GAME_ADDING_PLAYER);
        gameMap.put(groupID, game);
        playersInTheGroup.put(groupID, new HashSet<>());
    }

    public static Message handle(MessageEvent<TextMessageContent> event) throws IllegalAccessException {
        /*
        * handle game command
        * */
        String userText = event.getMessage().getText();

        if (gameCommands.contains(userText)) {
            Message message = GameCommandProcessor.handle(event);
            return message;
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

            Set<Player> participantsInGroup = playersInTheGroup.get(groupID);

            // if user use /end command, see if there's at least 2 players
            // then start the game
            if (participantsInGroup.size() >= 2 && userText.equals("/end")) {
                game.setGameState(Game.GAME_PREFLOP);
                DealtCardProcessor dealtCardProcessor = new DealtCardProcessor(deck);
                dealtCards.put(groupID, dealtCardProcessor);
                /*
                 * get hole cards : push msg to user
                 * */
                HashSet<Player> playerPosList = TablePosition.position(participantsInGroup);
                tablePos.put(groupID, playerPosList);
                // push message to user
                dealtHoleCards(groupID, playerPosList, deck);
                String positionMessage = positionMessage(game, playerPosList);
                TextMessage message = new TextMessage("遊戲開始！已將牌私訊發給玩家" + "\n" + positionMessage);
                return message;
            }

            /*
             * add player, filter out the same player +1
             * cuz I'm using Set, it'll filter out repeated values
             * */

            if (userText.equalsIgnoreCase("+1")) {
                if (addPlayer(event)) {
                    return new TextMessage("Welcome joined!");
                }
            }

            /*
             * the player leave the game
             * */
            if (userText.equalsIgnoreCase("-1")) {
                // if the player is in the set
                if (removePlayer(event)) {
                    return new TextMessage("You left the game");
                }
            }
            return null;
        }

        /*
         * This part handles preflop, flop, turn, river
         * */

        // possibly make it a TreeSet that is accessible to improve the program
        HashSet<Player> playerPositionList = tablePos.get(groupID);
        ArrayList<Player> playerList = new ArrayList<>(playerPositionList);
        playerList.sort(Comparator.comparingInt((Player p) -> p.getPosition()));

        /*
         * what players say should proceed the game?
         * */
        switch (gameState) {
            case Game.GAME_PREFLOP:
                // TODO betting event
                /*
                * if players all say check
                * */
                String message = gamePreflop(playerPositionList, deck, event, groupID, game);
                return new TextMessage(message);
            case Game.GAME_FLOP:
                // TODO betting event
//                gameFlopAndTurnAndRiver(playerPositionList, deck);
                return new TextMessage("I'm in flop swtich statement!");
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

        return null;
    }

    private static String gamePreflop(HashSet<Player> playerPositionList, Deck deck, MessageEvent<TextMessageContent> event, String groupID, Game game) throws IllegalAccessException {
        /*
        * test out dealt 3 cards
        * */
        StringBuilder flopCards = new StringBuilder();
        for (int i = 0; i < 3; i++){
            flopCards.append(Deal.getCard(deck));
        }

        DealtCardProcessor dealtCardProcessor = dealtCards.get(groupID);
        dealtCardProcessor.append(flopCards);

        game.setGameState(Game.GAME_FLOP);

        return flopCards.toString();
    }


    private static String positionMessage(Game game, Set<Player> playerPosList) {
        ArrayList<Player> playerList = new ArrayList<>(playerPosList);
        playerList.sort(Comparator.comparingInt((Player p) -> p.getPosition()));

        // check if it's in the preflop phrase
        if (game.getGameState() == Game.GAME_PREFLOP) {

            StringBuilder positionBuilder = new StringBuilder();
            /*
            * Loop through each user and get their userName
            * append it to the stringBuilder and get the position
            * */
            for (Player per : playerList) {
                String userName = LineAPIClient.getUserName(per.getUserID());
                switch (per.getPosition()) {
                    case 0:
                        positionBuilder.append("小盲: " + userName + "\n" );
                        break;
                    case 1:
                        positionBuilder.append("大盲: " + userName + "\n" );
                        break;
                    case 2:
                        positionBuilder.append("+1: " + userName + "\n" );
                        break;
                    case 3:
                        positionBuilder.append("+2: " + userName + "\n" );
                        break;
                    case 4:
                        positionBuilder.append("+3: " + userName + "\n" );
                        break;
                    case 5:
                        positionBuilder.append("+4: " + userName + "\n" );
                        break;
                    case 6:
                        positionBuilder.append("+5: " + userName + "\n" );
                        break;
                    case 7:
                        positionBuilder.append("+6: " + userName + "\n" );
                        break;
                    default:
                        return "GameController.positionMessage - seems wrong in the statement";
                }
            }
            return positionBuilder.toString();
        }

        return "GameController.positionMessage() bugs found, please report to developer";
    }

    private static void dealtHoleCards(String groupID, HashSet<Player> participants, Deck deck) throws IllegalAccessException {
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
            LineAPIClient.pushHoleCards(per.getUserID(), cards);

        }

    }

    private static Boolean addPlayer(MessageEvent<TextMessageContent> event) {
        // this user wants to play, add to the playerMap
        String userID = event.getSource().getUserId();
        return playersInTheGroup.get(event.getSource().getSenderId()).add(new Player(userID));
    }

    private static boolean removePlayer(MessageEvent<TextMessageContent> event) {
        String userID = event.getSource().getUserId();

        return playersInTheGroup.get(event.getSource().getSenderId()).remove(userID);
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
