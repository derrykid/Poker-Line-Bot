package processor;

import game.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TablePosition {
    public static Set<Player> initPositionSetter(Set<Player> participants) {
        /*
        * This class gives shuffle player position
        * */

        List<Player> playerList = new ArrayList<>(participants);
        Collections.shuffle(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setPosition(i);
        }
        Set<Player> playerSet = new TreeSet<>(Comparator.comparingInt(Player::getPosition));
        playerSet.addAll(playerList);

        return playerSet;
    }

}
