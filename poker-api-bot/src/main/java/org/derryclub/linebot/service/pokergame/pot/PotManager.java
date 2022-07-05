package org.derryclub.linebot.service.pokergame.pot;

import org.derryclub.linebot.gameConfig.blind.Blind;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.gameConfig.position.TableConfig;
import org.derryclub.linebot.poker.analyzer.Hand;
import org.derryclub.linebot.service.pokergame.Manager;
import org.derryclub.linebot.service.pokergame.util.TablePosition;

import java.util.*;
import java.util.stream.Collectors;

public final class PotManager implements Manager {

    private static PotManager instance;

    /**
     *  Each player has its position, and its betting chip.
     *  Hence, if we want to know how much money betting on the table,
     *  get all player's betting chip is enough
     *  This map is equivalent to {@link TablePosition} map
     */
    private final Map<String, TreeSet<Player>> potMap;

    private PotManager() {
        potMap = TablePosition.getTablePositionMap();
    }

    public static PotManager getManager() {
        if (instance == null) {
            instance = new PotManager();
        }
        return instance;
    }

    /**
     *  It gets the total pot on the table bets by the players
     * @param groupId is used as the key to find the Player set
     * @return the total number bet by the players
     */
    public int getPotOnTheTable(String groupId) {
        return potMap.get(groupId).stream().mapToInt(Player::getChipOnTheTable).sum();
    }

    /**
     * Get the entirety of the map pot map in which key is groupId, value is player set
     * @return Map
     */
    public Map<String, TreeSet<Player>> getPotMap() {
        return this.potMap;
    }

    /**
     * Small blind and big blind players are forced bet players
     * @param players TreeSet is sorted, hence the first 2 players are forced bet players
     */
    public static void forcedBet(TreeSet<Player> players) {
        Set<Player> forcedBetPlayers = players
                .stream()
                .filter(p -> p.getPosition().value < 2)
                .collect(Collectors.toSet());

        for (Player player : forcedBetPlayers) {
            if (player.getPosition() == TableConfig.SMALL_BLIND) {
                player.bet(Blind.SMALL_BLIND.value);
            }
            if (player.getPosition() == TableConfig.BIG_BLIND) {
                player.bet(Blind.SMALL_BLIND.value * 2);
            }
        }
    }

    /**
     * Get the biggest bet placed by the player
     */
    public int getBiggestBetOnTable(String groupId) {
        OptionalInt biggestBet =  instance.getPotMap().get(groupId).stream()
                .mapToInt(Player::getChipOnTheTable)
                .max();

        return biggestBet.isEmpty() ? 0 : biggestBet.getAsInt();
    }

    public static int potDistribute(String groupId, SortedMap<Hand, Player> playerRanking) {

        int pot = getManager().getPotOnTheTable(groupId);

        Optional<Player> player = playerRanking.values().stream().findFirst();
        if (player.isPresent()) {
            player.get().getChip().gainChip(pot);
            return pot;
        }
        return 0;
    }
}
