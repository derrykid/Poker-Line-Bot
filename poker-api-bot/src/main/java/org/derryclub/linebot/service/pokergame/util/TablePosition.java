package org.derryclub.linebot.service.pokergame.util;

import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.blind.Blind;
import org.derryclub.linebot.gameConfig.player.Player;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public final class TablePosition {


    private static final HashMap<String, TreeSet<Player>> tablePositionMap = new HashMap<>();

    public static TreeSet<Player> initPositionSetter(String groupId, Set<Player> participants) {

        List<Player> playerList = new ArrayList<>(participants);

        Collections.shuffle(playerList);

        IntStream.rangeClosed(0, playerList.size() - 1)
                .forEach(i -> playerList.get(i).setPosition(i));

        TreeSet<Player> playersSet = new TreeSet<>(playerList);

        tablePositionMap.put(groupId, playersSet);

        return playersSet;
    }

    public static Map<String, TreeSet<Player>> getTablePositionMap() {
        return tablePositionMap;
    }

    public static String positionMessage(TreeSet<Player> players) {

        StringBuilder positionBuilder = new StringBuilder("遊戲開始!" + "\n"
                + "牌已私訊發給玩家" + "\n"
                + "盲注 $" + Blind.SMALL_BLIND.value + "\n");

        for (Player player : players) {
            positionBuilder.append(player.getPosition().getPositionName()).append(": ").append(player.getUserName())
                    .append("已下注(").append(player.getChipOnTheTable()).append(")\n")
                    .append("(剩餘籌碼").append(player.getChip().getAvailableChip()).append(")\n");
        }
        return positionBuilder.toString();
    }

}
