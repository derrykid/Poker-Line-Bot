package Processor;

import Constant.GameCommand;
import Game.*;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import Card.*;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class GameController {

    private static Set<String> gameCommands = new HashSet<>(GameCommand.getGameCommandList());

    /*
     * Map<GroupID, chip pool>
     * */
    // TODO initialise the value map
    private static HashMap<String, Map<Player, Integer>> potMap = new HashMap<>();

    public static Map getPotMap(String groupID) {
        return potMap.get(groupID);
    }


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
    private static HashMap<String, List<Card>> communityCardsMap = new HashMap<>();

    public static List<Card> getCommunityCard(String groupID) {
        return communityCardsMap.get(groupID);
    }

    private static HashMap<String, Set<Player>> tablePos = new HashMap<>();

    public static Game create(String groupID) {
        Game game = new Game(Deck.newShuffledSingleDeck());
        game.setSmallBlind(GameConstant.Blind.getValue());
        game.setGameState(Game.GAME_ADDING_PLAYER);
        gameMap.put(groupID, game);
        playersInTheGroup.put(groupID, new HashSet<>());
        return game;
    }

    public static Message handle(MessageEvent<TextMessageContent> event) throws Throwable {
        /*
         * handle game command
         * */
        String userText = event.getMessage().getText().split(" ")[0];

        int betChip = 0;

        if (userText.equalsIgnoreCase("/bet")) {
            betChip = Integer.parseInt(event.getMessage().getText().split(" ")[1]);
        }

        if (gameCommands.contains(userText)) {
            Message message = GameCommandProcessor.handle(event);
            return message;
        }

        String groupID = event.getSource().getSenderId();
        Game game = gameMap.get(event.getSource().getSenderId());
        Deck deck = game.getDeck();

        int gameState = game.getGameState();

        /*
         * this part initialises the game, and deal cards out
         * */
        if (gameState == Game.GAME_ADDING_PLAYER) {

            // it's hashset
            Set<Player> participantsInGroup = playersInTheGroup.get(groupID);

            // if user use /end command, see if there's at least 2 players
            // then start the game
            if (participantsInGroup.size() >= 2 && userText.equals("/end")) {
                game.setGameState(Game.GAME_PREFLOP);

                communityCardsMap.put(groupID, new ArrayList<>());
                /*
                 * get hole cards : push msg to user
                 * after method calls, it turns to treeset
                 * */
                Set<Player> playerPosSet = TablePosition.initPositionSetter(participantsInGroup);
                tablePos.put(groupID, playerPosSet);
                // push message to user
                dealtHoleCards(playerPosSet, deck);
                // initialise pot map
                potMap.put(groupID, new TreeMap<>(Comparator.comparingInt(Player::getPosition)));
                Map playerBetMap = potMap.get(groupID);
                for (Player player : playerPosSet){
                    playerBetMap.put(player, 0);
                }

                String positionMessage = positionMessage(game, playerPosSet);
                TextMessage message = new TextMessage(
                        "遊戲開始！已將牌私訊發給玩家" + "\n" + positionMessage
                                + "小盲:" + GameConstant.Blind.getValue() + "\n"
                                + "大盲:" + GameConstant.Blind.getValue() * 2 + "\n"
                );
                return message;
            }

            /*
             * add player, filter out the same player +1
             * cuz I'm using Set, it'll filter out repeated values
             * */

            if (userText.equalsIgnoreCase("+1")) {
                if (addPlayer(event)) {
                    // get all participants
                    StringBuilder name = new StringBuilder();
                    for (Player player : participantsInGroup) {
                        name.append(player.getUserName()).append("\n");
                    }
                    return new TextMessage("Welcome joined!" + "\n" + "玩家 \n" + name);
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

        Set<Player> playerSet = tablePos.get(groupID);

        List<Card> communityCards = communityCardsMap.get(groupID);

        Map<Player, Integer> playerBetMap = potMap.get(groupID);
        /*
         * what players say should proceed the game?
         * */
        switch (gameState) {
            case Game.GAME_PREFLOP:
                // players betting event
                if (userText.equalsIgnoreCase("/bet")) {
                    PotProcessor.handle(playerSet, Game.GAME_PREFLOP, betChip, playerBetMap, groupID);
                }
                // if all players call, etc, the game is proceed
                if (playerSet.stream().allMatch(player -> player.getPlayerStatue() != 0)) {
                    String message;
                    message = gamePreflop(deck, communityCards);
                    game.setGameState(Game.GAME_FLOP);
                    return EmojiProcesser.process(message);
                }
                break;
            case Game.GAME_FLOP:
                // TODO betting event
                if (userText.equalsIgnoreCase("check")) {
                    String flopMessage = dealTurnAndRiverCards(deck, communityCards);
                    game.setGameState(Game.GAME_TURN_STATE);
                    return EmojiProcesser.process(flopMessage);
                }
                return null;
            case Game.GAME_TURN_STATE:
                // TODO betting event
                if (userText.equalsIgnoreCase("check")) {
                    String turnMessage = dealTurnAndRiverCards(deck, communityCards);
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
                SortedSet<Player> playerRanking = PokerAPIProcessor.getGameResult(playerSet, communityCards);
                String cardRankMsg = PokerAPIProcessor.cardRankMsg(playerRanking);
                String message = "Game done!";
                game.setGameState(Game.GAME_OVER);
                return new TextMessage(message + "\n" + cardRankMsg);
            case Game.GAME_OVER:
                /*
                 * may destroy the game in river state cuz winner is decided
                 * */
                gameMap.remove(groupID);
                return null;
            default:
                return new TextMessage("Error occurs! Please report me!");
        }
        return null;
    }

    private static String gamePreflop(Deck deck, List<Card> communityCards) throws IllegalAccessException {
        StringBuilder flopCards = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            Card card = Deal.getCard(deck);
            communityCards.add(card);
            flopCards.append(card);
        }
        return flopCards.toString();
    }

    private static String dealTurnAndRiverCards(Deck deck, List<Card> communityCards) throws IllegalAccessException {
        StringBuilder communityCardStringBuilder = new StringBuilder();
        Card card = Deal.getCard(deck);
        communityCards.add(card);
        for (Card per : communityCards) {
            communityCardStringBuilder.append(per);
        }
        return communityCardStringBuilder.toString();
    }


    private static String positionMessage(Game game, Set<Player> playersSet) {

        // check if it's in the preflop phrase

        StringBuilder positionBuilder = new StringBuilder();
        /*
         * Loop through each user and get their userName
         * append it to the stringBuilder and get the position
         * */
        for (Player per : playersSet) {
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
                    return "GameController.positionMessage() bugs found, please report to developer";
            }
        }
        return positionBuilder.toString();
    }

    private static void dealtHoleCards(Set<Player> participants, Deck deck) throws IllegalAccessException {
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
