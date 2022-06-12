package org.derryclub.linebot.service.pokergame.chipmanage;

import org.derryclub.linebot.gameConfig.blind.Blind;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.pokergame.pot.PotManager;

public final class ChipManagerImpl implements ChipManager {


    /**
     * Check if the player has bet a least the small blind value and its total bet equals to
     * or larger than the biggest bet on the table.
     * <br>
     * Check if his chip is enough to make the bet the player wants
     */
    public static boolean betValueValidator(String groupId, Player player, int playerBet) {

        boolean isLargerThanBlind = Math.floorDiv(playerBet, Blind.SMALL_BLIND.value) >= 1 &&
                playerBet >= 0;

        boolean isHavingEnoughToBet = playerBet <= player.getChip().getAvailableChip();

        @SuppressWarnings("OptionalGetWithoutIsPresent") boolean isEqualOrLargerThanTheBiggestBet = PotManager.getManager().getPotMap().get(groupId)
                .stream()
                .mapToInt(Player::getChipOnTheTable)
                .max().getAsInt() <= playerBet + player.getChipOnTheTable();

        return isLargerThanBlind && isHavingEnoughToBet && isEqualOrLargerThanTheBiggestBet;
    }

    public static boolean availChipIsGreaterThanBettingAmount(Player playerWhoCallsCmd, int playerBettingAmount) {
        return playerWhoCallsCmd.getChip().getAvailableChip() >= playerBettingAmount;
    }
}
