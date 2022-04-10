package Processor;

import Constant.GameCommand;
import Game.*;
import Poker.PokerHand;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import Card.*;

import java.util.*;

public class GameController {

    private static Set<String> gameCommands = new HashSet<>(GameCommand.getGameCommandList());

    /*
     * Map<GroupID, Set<Player>>
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
     * Map<GroupID,  Community Cards>
     * This map stores the dealt cards that correspond to the group
     * */
    private static HashMap<String, Set<Card>> communityCardsMap = new HashMap<>();

    public static Set<Card> getCommunityCard(String groupID) {
        return communityCardsMap.get(groupID);
    }

    private static HashMap<String, HashSet<Player>> tablePos = new HashMap<>();

    public static void create(String groupID) {
        Game game = new Game(Deck.newShuffledSingleDeck());
        game.setGameState(Game.GAME_ADDING_PLAYER);
        gameMap.put(groupID, game);
        playersInTheGroup.put(groupID, new HashSet<>());
    }

    public static Message handle(MessageEvent<TextMessageContent> event) throws Throwable {
        /*
         * handle game command
         * */
        String userText = event.getMessage().getText().split(" ")[0];

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
                Set<Card> communityCards = new HashSet<Card>();
                communityCardsMap.put(groupID, communityCards);
                /*
                 * get hole cards : push msg to user
                 * */
                HashSet<Player> playerPosList = TablePosition.initPositionSetter(participantsInGroup);
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
                } else {
                    return new TextMessage("Left game event error");
                }
            }
            return null;
        }

        /*
         * This part handles preflop, flop, turn, river
         * */

        // possibly make it a TreeSet that is accessible to improve the program
        HashSet<Player> playerPositionList = tablePos.get(groupID);
        ArrayList<Player> playerSortedList = new ArrayList<>(playerPositionList);
        playerSortedList.sort(Comparator.comparingInt((Player p) -> p.getPosition()));
        int playerNumber = playerSortedList.size();

        /*
         * what players say should proceed the game?
         * */
        switch (gameState) {
            case Game.GAME_PREFLOP:
                // TODO betting event
                /*
                 * TODO need players all say check
                 * */
                if (true) {
                    String message = "null";
                    if (userText.equalsIgnoreCase("check")) {
                        message = gamePreflop(playerPositionList, deck, userText, groupID, game);
                        game.setGameState(Game.GAME_FLOP);
                        return EmojiProcesser.process(message);
                    } else {
                        return null;
                    }
                }
                break;
            case Game.GAME_FLOP:
                // TODO betting event
                if (userText.equalsIgnoreCase("check")) {
                    String flopMessage = dealTurnAndRiverCards(playerNumber, deck, userText, groupID, game);
                    game.setGameState(Game.GAME_TURN_STATE);
                    return EmojiProcesser.process(flopMessage);
                }
                return null;
            case Game.GAME_TURN_STATE:
                // TODO betting event
                if (userText.equalsIgnoreCase("check")) {
                    String turnMessage = dealTurnAndRiverCards(playerNumber, deck, userText, groupID, game);
                    game.setGameState(Game.GAME_RIVER_STATE);
                    return EmojiProcesser.process(turnMessage);
                }
                return null;
            case Game.GAME_RIVER_STATE:
                //TODO betting event
                /*
                 * Map<Player, cards>
                 * This map ranks from the strongest hand to weakest
                 * */
//                Map<Player, String> playerCardMap = PokerAPIProcessor.process(playerSortedList, groupID);
                Set<Card> communityCards = communityCardsMap.get(groupID);
                Set<Player> winnerOrderedSet = PokerAPIProcessor.getWinner(playerSortedList, communityCards);
                String cardRankMsg = PokerAPIProcessor.cardRankMsg(winnerOrderedSet);
                String message = "Game done!";
                game.setGameState(Game.GAME_OVER);
                return new TextMessage(message + "\n"+ winnerOrderedSet + "\n" + cardRankMsg);
            case Game.GAME_OVER:
                /*
                 * may destroy the game in river state cuz winner is decided
                 * */
                gameMap.remove(groupID);
                return new TextMessage("Welcome to game over state!");
            default:
                return new TextMessage("Error occurs! Please report me!");
        }
        return null;
    }

    private static String gamePreflop(HashSet<Player> playerPositionList, Deck deck, String userText, String groupID, Game game) throws IllegalAccessException {
        Set<Card> communityCards = communityCardsMap.get(groupID);
        StringBuilder flopCards = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            Card card = Deal.getCard(deck);
            communityCards.add(card);
            flopCards.append(card);
        }
        return flopCards.toString();
    }

    private static String dealTurnAndRiverCards(int playerNumber, Deck deck, String userText, String groupID, Game game) throws IllegalAccessException {
        Set<Card> communityCards = communityCardsMap.get(groupID);

        Card card = Deal.getCard(deck);

        communityCards.add(card);

        StringBuilder communityCardStringBuilder = new StringBuilder();

        for (Card per : communityCards) {
            communityCardStringBuilder.append(per);
        }
        return communityCardStringBuilder.toString();
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
                String userName = per.getUserName();
                switch (per.getPosition()) {
                    case 0:
                        positionBuilder.append("小盲: " + userName + "\n");
                        break;
                    case 1:
                        positionBuilder.append("大盲: " + userName + "\n");
                        break;
                    case 2:
                        positionBuilder.append("+1: " + userName + "\n");
                        break;
                    case 3:
                        positionBuilder.append("+2: " + userName + "\n");
                        break;
                    case 4:
                        positionBuilder.append("+3: " + userName + "\n");
                        break;
                    case 5:
                        positionBuilder.append("+4: " + userName + "\n");
                        break;
                    case 6:
                        positionBuilder.append("+5: " + userName + "\n");
                        break;
                    case 7:
                        positionBuilder.append("+6: " + userName + "\n");
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
         * */

        for (Player per : participants) {
            /*
             * 1. Deal cards
             * The list is already sorted, so we can deal card directly
             * */
            StringBuilder cardsBuilder = new StringBuilder();
            for (int i = 0; i < 2; i++) {
                // fixme Ac Td pay attention to see if it will occur errors
                /*
                 * use Player to save cards, when in river state, call the cards and combine it with communitycards
                 *
                 * */
                Card card = Deal.getCard(deck);
                // add the card to Player variable
                per.addPlayerCards(card);
                cardsBuilder.append(card);
            }
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
        return playersInTheGroup.get(event.getSource().getSenderId()).add(new Player(userID, LineAPIClient.getUserName(userID)));
    }

    private static boolean removePlayer(MessageEvent<TextMessageContent> event) {
        String userID = event.getSource().getUserId();
        String groupID = event.getSource().getSenderId();
        Set<Player> playerSet = playersInTheGroup.get(groupID);

        /*
         * this can be improved by Optional
         * */
        Player playerToRemove = null;
        for (Player per : playerSet) {
            if (per.getUserID().equals(userID)) {
                playerToRemove = per;
            }
        }

        return playerSet.remove(playerToRemove);
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
