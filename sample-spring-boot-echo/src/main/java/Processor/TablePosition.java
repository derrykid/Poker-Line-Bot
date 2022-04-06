package Processor;

import Game.Player;

import java.util.*;

public class TablePosition {
    public static HashSet<Player> position(Set<Player> participants) {

        List<Player> playerList = new ArrayList<>(participants);
        Collections.shuffle(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setPosition(i);
        }

        return new HashSet<Player>(playerList);
    }

}
