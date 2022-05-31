package org.derryclub.linebot.service.pokergame.gamecontrol;

import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.blind.Blind;
import org.derryclub.linebot.gameConfig.position.TableConfig;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Deal;
import org.derryclub.linebot.poker.card.Deck;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.util.EmojiProcesser;
import org.derryclub.linebot.service.pokergame.util.GameResultUtilClass;
import org.derryclub.linebot.service.pokergame.gameinstances.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;
import org.derryclub.linebot.service.pokergame.pot.PotManager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public final class GameControlSystem extends GameControl {

//    public Message handle(MessageEvent<TextMessageContent> event) throws Throwable {
//
//        /*
//         * what players say should proceed the game?
//         * */
//        // todo not create new player objects, save the player
//        // fixme high card compare bugs
//        // fixme player won't be allow to check if they don't call on the bet
//        switch (gameState) {
//            case Game.GAME_PREFLOP:
//
//                // todo game proceed when players says extra 'check'
//                if (userCmd.equalsIgnoreCase("check")) {
//
//                    // if all players call, etc, the game is proceed
//                    if (playerSet.stream().allMatch(player -> player.getPlayerStatue() != 0)) {
//                        String message;
//                        message = dealCard(deck, communityCards);
//                        game.setGameStage(Game.GAME_FLOP);
//                        PotProcessor.resetGameClock(groupId);
//                        return EmojiProcesser.process(message);
//                    } else {
//                        String msg = PotProcessor.handle2PlayerCheck(playerSet, betChip, playerBetMap, groupId, playerOf);
//                        return new TextMessage(msg);
//                    }
//                }
//
//                // only 2 players
//                if (playerSet.size() == 2 && userCmd.equalsIgnoreCase("/bet")) {
//                    if (betChip <= 0) {
//                        return new TextMessage("You're betting wrong chip...");
//                    }
//                    String msg = PotProcessor.handPreFlop2Players(playerSet, betChip, playerBetMap, groupId, playerOf);
//                    if (msg == null) {
//                        return null;
//                    } else {
//                        return new TextMessage(msg);
//                    }
//                }
//
//                break;
//            case Game.GAME_FLOP:
//                // todo reset the check status
//
//                // todo game proceed when players says extra 'check'
//                if (userCmd.equalsIgnoreCase("check")) {
//
//                    // if all players call, etc, the game is proceed
//                    if (playerSet.stream().allMatch(player -> player.getPlayerStatue() != 0)) {
//                        String flopMessage = dealTurnAndRiverCards(deck, communityCards);
//                        game.setGameStage(Game.GAME_TURN_STATE);
//                        PotProcessor.resetGameClock(groupId);
//                        return EmojiProcesser.process(flopMessage);
//                    } else {
//                        String msg = PotProcessor.handle2PlayerCheck(playerSet, betChip, playerBetMap, groupId, playerOf);
//                        return new TextMessage(msg);
//                    }
//                }
//
//                // only 2 players
//                if (userCmd.equalsIgnoreCase("/bet")) {
//                    if (betChip <= 0) {
//                        return new TextMessage("You're betting wrong chip...");
//                    }
//                    String msg = PotProcessor.handPreFlop2Players(playerSet, betChip, playerBetMap, groupId, playerOf);
//                    if (msg == null) {
//                        return null;
//                    } else {
//                        return new TextMessage(msg);
//                    }
//                }
//                return null;
//            case Game.GAME_TURN_STATE:
//                // todo game proceed when players says extra 'check'
//                if (userCmd.equalsIgnoreCase("check")) {
//
//                    // if all players call, etc, the game is proceed
//                    if (playerSet.stream().allMatch(player -> player.getPlayerStatue() != 0)) {
//                        String turnMessage = dealTurnAndRiverCards(deck, communityCards);
//                        game.setGameStage(Game.GAME_RIVER_STATE);
//                        PotProcessor.resetGameClock(groupId);
//                        return EmojiProcesser.process(turnMessage);
//                    } else {
//                        String msg = PotProcessor.handle2PlayerCheck(playerSet, betChip, playerBetMap, groupId, playerOf);
//                        return new TextMessage(msg);
//                    }
//                }
//
//                if (userCmd.equalsIgnoreCase("/bet")) {
//                    if (betChip <= 0) {
//                        return new TextMessage("You're betting wrong chip...");
//                    }
//                    String msg = PotProcessor.handPreFlop2Players(playerSet, betChip, playerBetMap, groupId, playerOf);
//                    if (msg == null) {
//                        return null;
//                    } else {
//                        return new TextMessage(msg);
//                    }
//                }
//                return null;
//            case Game.GAME_RIVER_STATE:
//                SortedSet<Player> playerRanking = GameResultUtilClass.getGameResult(playerSet, communityCards);
//                int winPot = PotProcessor.potDistribute(playerRanking, groupId);
//                String gameRankingMessage = GameResultUtilClass.cardRankMsg(playerRanking);
//                String message = "Game done!";
//                game.setGameStage(Game.GAME_OVER);
//                return new TextMessage(message + "\n" + gameRankingMessage + "\n" + "winner chips: " + winPot);
//            case Game.GAME_OVER:
////                gameManagerImpl.getGameMap().remove(groupId);
//                return null;
//            default:
//                return new TextMessage("Error occurs! Please report me!");
//        }
//        return null;
//    }

    private static String dealCard(Deck deck, List<Card> communityCards) {
        StringBuilder cards = new StringBuilder();
        Card card = Deal.dealCard(deck);
        communityCards.add(card);
        cards.append(card);
        return cards.toString();
    }

    public static Message betEvent(String groupId, String userId, int bettingValue) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Game.GameStage gameStage = game.getGameStage();

        switch (gameStage) {
            case GAME_PREFLOP:
                return preflopBet(game, groupId, userId, bettingValue);
            case GAME_FLOP:
            case GAME_TURN_STATE:
            case GAME_RIVER_STATE:
                return playerBet(game, groupId, userId, bettingValue);
            case GAME_OVER:
                return new TextMessage("Game over!");
        }
        log.error("Shouldn't reach here error: {}", GameControlSystem.class);
        return null;
    }


    /**
     * See if it's the player's turn to bet, if so, make it
     */
    private static Message preflopBet(Game game, String groupId, String userId, int playerBet) {

        int whoseTurn = whoseTurnToMove(game, groupId);

        Player playerWhoWantsToBet = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        boolean isPlayerTurnAndBetEnough = (playerWhoWantsToBet.getPosition().value == whoseTurn)
                && isBetEnough(groupId, playerWhoWantsToBet, playerBet);

        if (isPlayerTurnAndBetEnough) {
            playerWhoWantsToBet.bet(playerBet);
            playerWhoWantsToBet.setPlayerStatue(Player.PlayerStatus.CHECK);
            game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
            return new TextMessage("You bet: " + playerBet);
        } else {
            return new TextMessage("Not bet enough!");
        }
    }

    private static Message playerBet(Game game, String groupId, String userId, int playerBet) {

        int whoseTurn = whoseTurnToMove(game, groupId);

        Player playerWhoWantsToBet = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        boolean isPlayerTurnAndBetEnough = (playerWhoWantsToBet.getPosition().value == whoseTurn)
                && isBetEnough(groupId, playerWhoWantsToBet, playerBet);

        // todo make sure player only bet what he has in the pocket
        if (isPlayerTurnAndBetEnough) {
            playerWhoWantsToBet.bet(playerBet);
            playerWhoWantsToBet.setPlayerStatue(Player.PlayerStatus.CHECK);
            game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
            return new TextMessage("You bet: " + playerBet);
        } else {
            return new TextMessage("Not bet enough!");
        }
    }

    public static Message playerCheck(MessageEvent<TextMessageContent> event) {

        String groupId = event.getSource().getSenderId();
        String userId = event.getSource().getUserId();
        Player playerWhoCallsCommand = PlayerManagerImpl.getManager().getPlayer(groupId, userId);
        Game game = GameManagerImpl.getManager().getGame(event.getSource().getSenderId());

        int whoseTurn = whoseTurnToMove(game, groupId);

        boolean isEligible = playerWhoCallsCommand.getPlayerStatue().value == whoseTurn;

        if (isEligible) {
            playerWhoCallsCommand.setPlayerStatue(Player.PlayerStatus.CHECK);
            game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
            return allCheckedOrFolded(groupId)
                    ? gameProceed(groupId)
                    : new TextMessage("You checked");
        }
        return new TextMessage("Not your turn");

    }

    public static Message playerFold(MessageEvent<TextMessageContent> event) {
        String groupId = event.getSource().getSenderId();
        String userId = event.getSource().getUserId();
        Player playerWhoFolds = PlayerManagerImpl.getManager().getPlayer(groupId, userId);
        playerWhoFolds.setPlayerStatue(Player.PlayerStatus.FOLD);
        return new TextMessage("You fold");
    }

    public static Message gameProceed(String groupId) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Deck deck = game.getDeck();
        List<Card> cards = CommunityCardManager.getManager()
                .getCommunityCardsMap().get(groupId);

        // todo make every player static to ready to make a move except dead status
        switch (game.getGameStage()) {
            case GAME_PREFLOP:
                String cardString = IntStream.range(0, 3)
                        .mapToObj(i -> dealCard(deck, cards))
                        .collect(Collectors.joining());
                game.setGameStage(Game.GameStage.GAME_FLOP);
                game.setWhoseTurnToMove(0);
                return EmojiProcesser.process(cardString);
            case GAME_FLOP:
                game.setGameStage(Game.GameStage.GAME_TURN_STATE);
                game.setWhoseTurnToMove(0);
                return EmojiProcesser.process(dealCard(deck, cards));
            case GAME_TURN_STATE:
                game.setGameStage(Game.GameStage.GAME_RIVER_STATE);
                game.setWhoseTurnToMove(0);
                return EmojiProcesser.process(dealCard(deck, cards));
            case GAME_RIVER_STATE:
                game.setGameStage(Game.GameStage.GAME_OVER);
                game.setWhoseTurnToMove(0);

                SortedSet<Player> playerRanking = GameResultUtilClass.getGameResult(groupId);

                int winnerPot = PotManager.potDistribute(groupId, playerRanking);

                String cardRankMsg = GameResultUtilClass.cardRankMsg(playerRanking);

                return new TextMessage("Game done!" + "\n" +
                        cardRankMsg + "\n" + "winner chips: " + winnerPot);

            case GAME_OVER:
                GameManagerImpl.getManager().gameFinished(groupId);
                break;
        }

        return null;
    }

    /**
     * if all players are checked or fold, return true
     * @param groupId - used to find the player set
     * @return true means all are checked or fold
     */
    public static boolean allCheckedOrFolded(String groupId) {
        return PlayerManagerImpl.getManager().getPlayers(groupId).stream()
                .allMatch(player -> player.getPlayerStatue().value != 0);
    }

    /**
     * Util method that used to check if the player has bet a least the small blind value and its total bet equals to
     * or larger than the biggest bet on the table.
     */
    private static boolean isBetEnough(String groupId, Player player, int playerBet) {

        boolean isLargerThanBlind = Math.floorDiv(playerBet, Blind.SMALL_BLIND.value) >= 1;

        boolean isEqualOrLargerThanTheBiggestBet = PotManager.getManager().getPotMap().get(groupId)
                .stream()
                .mapToInt(Player::getChipOnTheTable)
                .max().getAsInt() <= playerBet + player.getChipOnTheTable();

        return isLargerThanBlind && isEqualOrLargerThanTheBiggestBet;
    }

    /**
     * find out which player is the right player to move
     *
     * @param game    finds out the exact game the room is using
     * @param groupId is used to find the TreeSet of the participating players in the room
     * @return The int value of the enum class {@link TableConfig} the one who is capable of bet / check / fold
     */
    private static int whoseTurnToMove(Game game, String groupId) {
        return game.getWhoseTurnToMove() % PlayerManagerImpl.getManager()
                .getPlayers(groupId).size();
    }
}
