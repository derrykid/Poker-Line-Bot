package Processor;

import Game.Player;

import java.util.*;

public class TablePosition {
    public static List<Player> position(HashMap<String, Player> participants) {

        List<Player> playerList = new ArrayList<>(participants.values());
        Collections.shuffle(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setPosition(i);
        }

        playerList.sort(Comparator.comparingInt(Player::getPosition));

        return playerList;
    }

}
