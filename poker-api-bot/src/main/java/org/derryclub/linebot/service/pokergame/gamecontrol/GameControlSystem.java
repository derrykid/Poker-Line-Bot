package org.derryclub.linebot.service.pokergame.gamecontrol;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.gameConfig.position.TableConfig;
import org.derryclub.linebot.poker.analyzer.Hand;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.card.Deck;
import org.derryclub.linebot.service.pokergame.card.DealCards;
import org.derryclub.linebot.service.pokergame.chipmanage.ChipManagerImpl;
import org.derryclub.linebot.service.pokergame.card.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;
import org.derryclub.linebot.service.pokergame.pot.PotManager;
import org.derryclub.linebot.service.pokergame.util.GameResultUtilClass;
import org.derryclub.linebot.service.util.EmojiProcessor;
import org.derryclub.linebot.service.util.Threads;

import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

@Slf4j
public final class GameControlSystem extends GameControl {

    private final ExecutorService executor = Threads.getExecutor();

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

        // check if it's the player's turn
        int whoseTurn = whoseTurnToMove(game, groupId);

        Player playerWhoCallsCmd = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        boolean playerTurn = playerWhoCallsCmd.getPosition().value == whoseTurn;
        if (!playerTurn) {
            String theOneWhoShouldMakeMove = PlayerManagerImpl
                    .getWhoseTurn(groupId, whoseTurn)
                    .getUserName();
            return new TextMessage("現在是輪到" + theOneWhoShouldMakeMove + "\n");
        }

        // check if bet value valid
        if (playerBettingAmount <= 0) { return new TextMessage("請輸入大於0的數字"); }

        int playerRemainingChip = playerWhoCallsCmd.getChip().getAvailableChip();

        boolean notHaveEnoughChip = playerRemainingChip > 0;

        if (!notHaveEnoughChip) { return new TextMessage("你剩" + playerRemainingChip); }

        boolean isBetAmountValid = ChipManagerImpl.availChipIsGreaterThanBettingAmount
                (playerWhoCallsCmd, playerBettingAmount);

        if (!isBetAmountValid) { return new TextMessage("你剩下:" + playerRemainingChip +"籌碼");}


        // while betting, there are 3 situations:
        // 1. bigger that the chip other players bet
        // 2. equal to the chip
        // 3. smaller to the chip, but it's players all available chip
        int biggestOnTable = PotManager.getManager().getBiggestBetOnTable(groupId);
        int playerToTalBet = playerWhoCallsCmd.getChipOnTheTable() + playerBettingAmount;

        // equal to the chip, it's call
        if (playerToTalBet == biggestOnTable) {
            return playerCall(groupId, userId);
        }

        // smaller to the chip, see if it's all the player's available chip
        if (playerToTalBet < biggestOnTable) {
            if (playerToTalBet >= playerWhoCallsCmd.getChip().getAvailableChip()) {
                return playerAllIn(groupId, userId);
            }
            int playerBet = playerWhoCallsCmd.getChipOnTheTable();
            return new TextMessage("下注的金額不夠多！至少要" + (biggestOnTable - playerBet));
        }

        // this is bigger than the other player chip
        playerWhoCallsCmd.bet(playerBettingAmount);
        playerWhoCallsCmd.check();
        game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);

        String nextPlayerName = PlayerManagerImpl.nextPlayerToPlay(groupId, whoseTurn)
                .getUserName();

        return new TextMessage("你下注：" + playerBettingAmount + "\n" +
                "你的總下注金額：" + playerWhoCallsCmd.getChipOnTheTable() + "\n" +
                "你籌碼剩下:" + playerWhoCallsCmd.getChip().getAvailableChip() + "\n" +
                "輪到" + nextPlayerName + "\n" + "請使用指令！ 可從主頁查詢或是 '/help'");
    }

    public static Message playerCall(String groupId, String userId) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Player playerWhoCallsCommand = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        int biggestOnTable = PotManager.getManager().getBiggestBetOnTable(groupId);
        int playerBet = playerWhoCallsCommand.getChipOnTheTable();
        int whoseTurn = whoseTurnToMove(game, groupId);


        boolean isPlayerTurn = playerWhoCallsCommand.getPosition().value == whoseTurn;

        int theCallAmount = biggestOnTable - playerBet;

        if (!isPlayerTurn) {
            String theOneWhoShouldMakeMove = PlayerManagerImpl
                    .getWhoseTurn(groupId, whoseTurn)
                    .getUserName();
            return new TextMessage("現在是輪到" + theOneWhoShouldMakeMove + "\n");
        }
        playerWhoCallsCommand.bet(theCallAmount);

        playerWhoCallsCommand.check();
        String nextPlayerName = PlayerManagerImpl.nextPlayerToPlay(groupId, whoseTurn)
                .getUserName();

        game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
        return allCheckedOrFoldedOrAllIn(groupId)
                ? gameProceed(groupId)
                : new TextMessage(playerWhoCallsCommand.getUserName() + "跟注" + theCallAmount + "\n"
                + "輪到" + nextPlayerName + "\n" + "請使用指令！ 可從主頁查詢或是'/help'");
    }

    // use when player places a bet that is bigger that he owns
    public static Message playerAllIn(String groupId, String userId) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Player playerWhoCallsCommand = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        int whoseTurn = whoseTurnToMove(game, groupId);

        boolean isPlayerTurn = playerWhoCallsCommand.getPosition().value == whoseTurn;

        if (!isPlayerTurn) {
            String theOneWhoShouldMakeMove = PlayerManagerImpl
                    .getWhoseTurn(groupId, whoseTurn)
                    .getUserName();
            return new TextMessage("現在是輪到" + theOneWhoShouldMakeMove + "\n");
        }
        playerWhoCallsCommand.bet(playerWhoCallsCommand.getChip().getAvailableChip());

        playerWhoCallsCommand.allIn();

        String nextPlayerName = PlayerManagerImpl.nextPlayerToPlay(groupId, whoseTurn)
                .getUserName();

        game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);

        return allCheckedOrFoldedOrAllIn(groupId)
                ? gameProceed(groupId)
                : new TextMessage(playerWhoCallsCommand.getUserName() + "All in!" +
                playerWhoCallsCommand.getChipOnTheTable() + "\n"
                + "輪到" + nextPlayerName + "\n" + "請使用指令！ 可從主頁查詢或是'/help'");
    }

    // by using all in cmd
    public static Message playerAllIn(MessageEvent<TextMessageContent> event) {

        String groupId = event.getSource().getSenderId();
        String userId = event.getSource().getUserId();

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Player playerWhoCallsCommand = PlayerManagerImpl.getManager().getPlayer(groupId, userId);

        int whoseTurn = whoseTurnToMove(game, groupId);

        boolean isPlayerTurn = playerWhoCallsCommand.getPosition().value == whoseTurn;

        if (!isPlayerTurn) {
            String theOneWhoShouldMakeMove = PlayerManagerImpl
                    .getWhoseTurn(groupId, whoseTurn)
                    .getUserName();
            return new TextMessage("現在是輪到" + theOneWhoShouldMakeMove + "\n");
        }
        playerWhoCallsCommand.bet(playerWhoCallsCommand.getChip().getAvailableChip());

        playerWhoCallsCommand.allIn();

        String nextPlayerName = PlayerManagerImpl.nextPlayerToPlay(groupId, whoseTurn)
                .getUserName();

        game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);

        return allCheckedOrFoldedOrAllIn(groupId)
                ? gameProceed(groupId)
                : new TextMessage(playerWhoCallsCommand.getUserName() + " All in! " +
                playerWhoCallsCommand.getChipOnTheTable() + "\n"
                + "輪到" + nextPlayerName + "\n" + "請使用指令！ 可從主頁查詢或是'/help'");
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
            String theOneWhoShouldMakeMove = PlayerManagerImpl
                    .getWhoseTurn(groupId, whoseTurn)
                    .getUserName();

            return new TextMessage("現在是輪到" + theOneWhoShouldMakeMove + "\n" +
                    "請使用指令！ 可從主頁查詢或是'/help'");
        }

        if (!isChipTheBiggestOnTheTable) {
            return new TextMessage("你至少要下注：" + (biggestOnTable - playerBet)
                    + "\n" + "或者你可以棄牌 '/fold'");
        }

        playerWhoCallsCommand.check();
        String nextPlayerName = PlayerManagerImpl.nextPlayerToPlay(groupId, whoseTurn)
                .getUserName();

        game.setWhoseTurnToMove(game.getWhoseTurnToMove() + 1);
        return allCheckedOrFoldedOrAllIn(groupId)
                ? gameProceed(groupId)
                : new TextMessage(playerWhoCallsCommand.getUserName() + "過牌!" + "\n"
                + "輪到" + nextPlayerName + "\n" + "請使用指令！ 可從主頁查詢或是'/help'");
    }

    public static Message playerFold(MessageEvent<TextMessageContent> event) {
        String groupId = event.getSource().getSenderId();
        String userId = event.getSource().getUserId();
        Player playerWhoFolds = PlayerManagerImpl.getManager().getPlayer(groupId, userId);
        playerWhoFolds.fold();


        // if only 1 player alive, game end
        boolean onlyOnePlayerLeft = (int) PlayerManagerImpl.getManager().getPlayers(groupId).stream()
                .filter(player -> player.getPlayerStatue() != Player.PlayerStatus.FOLD)
                .count() == 1;

        if (onlyOnePlayerLeft) {
            return gameFinishedByOnlyOneLeft(groupId);
        }

        return allCheckedOrFoldedOrAllIn(groupId)
                ? gameProceed(groupId)
                : new TextMessage(playerWhoFolds.getUserName() + "蓋牌");
    }

    private static Message gameFinishedByOnlyOneLeft(String groupId) {
        GameManagerImpl.getManager().getGame(groupId).setGameStage(Game.GameStage.GAME_OVER);
        GameManagerImpl.getManager().gameFinished(groupId);
        CommunityCardManager.getManager().clearCommunityCard(groupId);
        int winnerPot = PotManager.getManager().getPotOnTheTable(groupId);

        Optional<Player> optionalPlayer = PlayerManagerImpl.getManager().getPlayers(groupId)
                .stream()
                .filter(Player.theOneLeftPredicate)
                .findAny();

        if (optionalPlayer.isPresent()) {
            Player winner = optionalPlayer.get();
            winner.getChip().gainChip(winnerPot);

            PlayerManagerImpl.getManager().getPlayers(groupId)
                    .forEach(Player::clearChipOnTheTable);

            return new TextMessage("Game over!" + "\n" + "贏家獲得的籌碼: " + winnerPot);
        }
        log.error("Error occurred when players fold and only one left");
        return new TextMessage("出錯了！請回報給開發者");
    }

    public static Message gameProceed(String groupId) {

        Game game = GameManagerImpl.getManager().getGame(groupId);
        Deck deck = game.getDeck();
        List<Card> cards = CommunityCardManager.getManager()
                .getCommunityCardsMap().get(groupId);


        switch (game.getGameStage()) {
            case GAME_PREFLOP:
                game.setGameStage(Game.GameStage.GAME_FLOP);
                game.setWhoseTurnToMove(0);
                PlayerManagerImpl.setBackStatus(groupId);
                return EmojiProcessor.process(DealCards.deal3Cards(deck, cards));
            case GAME_FLOP:
                game.setGameStage(Game.GameStage.GAME_TURN_STATE);
                game.setWhoseTurnToMove(0);
                PlayerManagerImpl.setBackStatus(groupId);
                return EmojiProcessor.process(DealCards.dealCard(deck, cards));
            case GAME_TURN_STATE:
                game.setGameStage(Game.GameStage.GAME_RIVER_STATE);
                game.setWhoseTurnToMove(0);
                PlayerManagerImpl.setBackStatus(groupId);
                return EmojiProcessor.process(DealCards.dealCard(deck, cards));
            case GAME_RIVER_STATE:
                game.setGameStage(Game.GameStage.GAME_OVER);

                SortedMap<Hand, Player> playerRanking = GameResultUtilClass.getGameResult(groupId);

                log.info("Player ranking: {}",playerRanking);

                int winnerPot = PotManager.potDistribute(groupId, playerRanking);

                String cardRankMsg = GameResultUtilClass.cardRankMsg(playerRanking);

                PlayerManagerImpl.setBackStatus(groupId);

                GameManagerImpl.getManager().gameFinished(groupId);

                // clear the bet chip on table for next round
                PlayerManagerImpl.getManager().getPlayers(groupId)
                        .forEach(Player::clearChipOnTheTable);

                return new TextMessage("Game over!" + "\n" +
                        cardRankMsg + "\n" + "贏家獲得的籌碼: " + winnerPot);

        }
        return null;
    }

    /**
     * if all players are checked, all in, or fold, return true
     *
     * @param groupId - used to find the player set
     * @return true means all are checked or fold
     */
    private static boolean allCheckedOrFoldedOrAllIn(String groupId) {
        Predicate<Player> eitherCheckOrFoldOrAllIn = player ->
                ((player.getPlayerStatue() == Player.PlayerStatus.CHECK) ||
                        (player.getPlayerStatue() == Player.PlayerStatus.FOLD) ||
                        (player.getPlayerStatue() == Player.PlayerStatus.ALL_IN));

        return PlayerManagerImpl.getManager().getPlayers(groupId).stream()
                .allMatch(eitherCheckOrFoldOrAllIn);
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
