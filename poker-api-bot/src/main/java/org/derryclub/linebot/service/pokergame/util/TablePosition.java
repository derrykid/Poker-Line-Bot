package org.derryclub.linebot.service.pokergame.util;

import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.player.Player;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public final class TablePosition {

    private static final HashMap<String, TreeSet<Player>> tablePositionMap = new HashMap<>();

    public static TreeSet<Player> initPositionSetter(String groupId, Set<Player> participants) {

        List<Player> playerList = new ArrayList<>(participants);

        Collections.shuffle(playerList);

        IntStream.rangeClosed(0, playerList.size())
                .forEach(i -> playerList.get(i).setPosition(i));

        TreeSet<Player> playersSet = new TreeSet<>(playerList);

        tablePositionMap.put(groupId, playersSet);

        return playersSet;
    }

    public static Map<String, TreeSet<Player>> getTablePositionMap() {
        return tablePositionMap;
    }

    public static String positionMessage(TreeSet<Player> players) {

        StringBuilder positionBuilder = new StringBuilder("遊戲開始！已將牌私訊發給玩家" + "\n");
        /*
         * Loop through each user and get their userName
         * append it to the stringBuilder and get the position
         * */
        for (Player player : players) {
            String userName = player.getUserName();
            switch (player.getPosition().value) {
                case 0:
                    positionBuilder.append("小盲: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 1:
                    positionBuilder.append("大盲: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 2:
                    positionBuilder.append("+1: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 3:
                    positionBuilder.append("+2: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 4:
                    positionBuilder.append("+3: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 5:
                    positionBuilder.append("+4: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 6:
                    positionBuilder.append("+5: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                case 7:
                    positionBuilder.append("+6: " + userName + "(" + player.getChipOnTheTable() + ")" + "\n");
                    break;
                default:
                    log.error("Should not be here: {}", TablePosition.class);
            }
        }
        return positionBuilder.toString();
    }

}
