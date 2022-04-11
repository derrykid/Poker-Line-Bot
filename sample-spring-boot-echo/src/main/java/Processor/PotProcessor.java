package Processor;

import Game.*;

import java.util.*;

public class PotProcessor {

    // game procedure clock, value 0 means it's now small blind to bet
    // Map<GroupID, player to move>
    private static Map<String, Integer> potProcessor = new HashMap<>();


    public static void handle(Set<Player> playerSet, int GameStatus, int betChip, Map<Player, Integer> playerBetMap, String groupID) {

        Optional<Integer> order = Optional.ofNullable(potProcessor.get(groupID));
        // TODO betting logic


        // the base for every player move
        int blindValue = GameConstant.Blind.getValue();

        /*
        * playerBetMap<Player, chips>
        * */

        // pre flop: set small blind and big blind
        if (GameStatus == Game.GAME_PREFLOP){
            int i = 0;
            /*
            * this for loop get the S/B blind player betted at beginning
            * */
            for (Player player : playerBetMap.keySet()){
                // it means it's small blind
                if (i == 2) {
                    break;
                }
                if (player.getPosition() == 0){
                    playerBetMap.put(player, blindValue);
                }

                if (player.getPosition() == 1) {
                    playerBetMap.put(player, blindValue * 2);
                }
                i++;
            }
            // update to big blind + 1, if there's the guy, then proceed, if nope, end
            potProcessor.put(groupID, 2);
        }

    }
}
