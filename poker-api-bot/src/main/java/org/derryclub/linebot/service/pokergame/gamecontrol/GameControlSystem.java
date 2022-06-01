package org.derryclub.linebot.service.pokergame.gamecontrol;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.gameConfig.blind.Blind;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.gameConfig.position.TableConfig;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Deal;
import org.derryclub.linebot.poker.card.Deck;
import org.derryclub.linebot.service.pokergame.gameinstances.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;
import org.derryclub.linebot.service.pokergame.pot.PotManager;
import org.derryclub.linebot.service.pokergame.util.GameResultUtilClass;
import org.derryclub.linebot.service.util.EmojiProcesser;

import java.util.List;
import java.util.SortedSet;
import java.util.function.Predicate;

@Slf4j
public final class GameControlSystem extends GameControl {

    /**
     * Use to deal card
     * @param deck the deck of card of the table
     * @param communityCards keep a record of all cards that dealt
     * @return the card map to a string
     */
    private static String dealCard(Deck deck, List<Card> communityCards) {
        StringBuilder cards = new StringBuilder();

        communityCards.add(Deal.dealCard(deck));

        communityCards.stream().forEach(per -> cards.append(per));

        return cards.toString();
    }

    public static Message betEvent(String groupId, String userId, int bettingValue) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Game.GameStage gameStage = game.getGameStage();

        if (gameStage.equals(Game.GameStage.GAME_OVER)) {
            return new TextMessage("遊戲結束，可以使用 '/start' 重新開啟牌局");
        }
        return playerBet(game, groupId, userId, bettingValue);
    }

    /**
     * See if it's the player's turn to bet, if so, make it
     */
    private static Message playerBet(Game game, String groupId, String userId,
                                     int playerBettingAmount) {

        int whoseTurn = whoseTurnToMove(game, groupId);

        Player playerWhoWantsToBet = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        boolean isPlayerTurnAndBetEnough = (playerWhoWantsToBet.getPosition().value == whoseTurn)
                && betValueValidator(groupId, playerWhoWantsToBet, playerBettingAmount);

        if (isPlayerTurnAndBetEnough) {
            playerWhoWantsToBet.bet(playerBettingAmount);
            playerWhoWantsToBet.check();
            game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
            return new TextMessage("你下注：" + playerBettingAmount + "\n" +
                    "你的總下注金額：" + playerWhoWantsToBet.getChipOnTheTable());
        } else {
            return new TextMessage("下注的金額不夠多！");
        }
    }

    public static Message playerCheck(MessageEvent<TextMessageContent> event) {

        String groupId = event.getSource().getSenderId();
        String userId = event.getSource().getUserId();

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Player playerWhoCallsCommand = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        int biggestOnTable = PotManager.getManager().getBiggestBetOnTable(groupId);
        int playerBet = playerWhoCallsCommand.getChipOnTheTable();
        int whoseTurn = whoseTurnToMove(game, groupId);


        boolean isPlayerTurn = playerWhoCallsCommand.getPosition().value == whoseTurn;
        boolean isChipTheBiggestOnTheTable = playerBet >= biggestOnTable;

        if (!isPlayerTurn) {
            return new TextMessage("現在不是輪到你");
        }

        if (!isChipTheBiggestOnTheTable) {
            return new TextMessage("你至少要下注：" + (biggestOnTable - playerBet)
            + "\n" + "或者你可以棄牌 '/fold'");
        }

        playerWhoCallsCommand.check();
        game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
        return allCheckedOrFolded(groupId)
                ? gameProceed(groupId)
                : new TextMessage(playerWhoCallsCommand.getUserName() + "過牌！");

    }

    public static Message playerFold(MessageEvent<TextMessageContent> event) {
        String groupId = event.getSource().getSenderId();
        String userId = event.getSource().getUserId();
        Player playerWhoFolds = PlayerManagerImpl.getManager().getPlayer(groupId, userId);
        playerWhoFolds.fold();
        return new TextMessage(playerWhoFolds.getUserName() + "蓋牌");
    }

    public static Message gameProceed(String groupId) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Deck deck = game.getDeck();
        List<Card> cards = CommunityCardManager.getManager()
                .getCommunityCardsMap().get(groupId);

        switch (game.getGameStage()) {
            case GAME_PREFLOP:
                StringBuilder cardAboutToDeal = new StringBuilder();
                for (int i = 0; i < 3; i++) {
                    cardAboutToDeal.append(dealCard(deck, cards));
                }
                game.setGameStage(Game.GameStage.GAME_FLOP);
                game.setWhoseTurnToMove(0);
                PlayerManagerImpl.setBackStatus(groupId);
                return EmojiProcesser.process(cardAboutToDeal.toString());
            case GAME_FLOP:
                game.setGameStage(Game.GameStage.GAME_TURN_STATE);
                game.setWhoseTurnToMove(0);
                PlayerManagerImpl.setBackStatus(groupId);
                return EmojiProcesser.process(dealCard(deck, cards));
            case GAME_TURN_STATE:
                game.setGameStage(Game.GameStage.GAME_RIVER_STATE);
                game.setWhoseTurnToMove(0);
                PlayerManagerImpl.setBackStatus(groupId);
                return EmojiProcesser.process(dealCard(deck, cards));
            case GAME_RIVER_STATE:
                game.setGameStage(Game.GameStage.GAME_OVER);
                game.setWhoseTurnToMove(0);

                SortedSet<Player> playerRanking = GameResultUtilClass.getGameResult(groupId);

                int winnerPot = PotManager.potDistribute(groupId, playerRanking);

                String cardRankMsg = GameResultUtilClass.cardRankMsg(playerRanking);

                PlayerManagerImpl.setBackStatus(groupId);

                // remove the game
                GameManagerImpl.getManager().gameFinished(groupId);

                return new TextMessage("Game over!" + "\n" +
                        cardRankMsg + "\n" + "贏家獲得的籌碼: " + winnerPot);
        }
        return null;
    }

    /**
     * if all players are checked or fold, return true
     *
     * @param groupId - used to find the player set
     * @return true means all are checked or fold
     */
    public static boolean allCheckedOrFolded(String groupId) {
        Predicate<Player> eitherCheckOrFold = player ->
                ((player.getPlayerStatue() == Player.PlayerStatus.CHECK) ||
                        (player.getPlayerStatue() == Player.PlayerStatus.FOLD));

        return PlayerManagerImpl.getManager().getPlayers(groupId).stream()
                .allMatch(eitherCheckOrFold);
    }

    /**
     * Check if the player has bet a least the small blind value and its total bet equals to
     * or larger than the biggest bet on the table.
     * <br>
     * Check if his chip is enough to make the bet the player wants
     */
    private static boolean betValueValidator(String groupId, Player player, int playerBet) {

        boolean isLargerThanBlind = Math.floorDiv(playerBet, Blind.SMALL_BLIND.value) >= 1 &&
                playerBet >= 0;

        boolean isHavingEnoughToBet = playerBet <= player.getChip().getAvailableChip();

        boolean isEqualOrLargerThanTheBiggestBet = PotManager.getManager().getPotMap().get(groupId)
                .stream()
                .mapToInt(Player::getChipOnTheTable)
                .max().getAsInt() <= playerBet + player.getChipOnTheTable();

        return isLargerThanBlind && isHavingEnoughToBet && isEqualOrLargerThanTheBiggestBet;
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
