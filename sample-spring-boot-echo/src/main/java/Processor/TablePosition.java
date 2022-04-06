package Processor;

import Game.Player;

import java.util.*;

public class TablePosition {
    public static List<Player> position(Map<String, Player> participants) {

        List<Player> playerList = new ArrayList<Player>(participants.values());
        Collections.shuffle(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setPosition(i);
        }

        playerList.sort(Comparator.comparingInt(Player::getPosition));

        return playerList;
    }

}
