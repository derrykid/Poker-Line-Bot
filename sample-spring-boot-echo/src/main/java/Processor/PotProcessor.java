package Processor;

import Game.*;

import java.util.*;

public class PotProcessor {

    // game procedure clock, value 0 means it's now small blind to bet
    // Map<GroupID, player to move>
    private static Map<String, Integer> gameClock = new HashMap<>();

    public static String handle(Set<Player> playerSet, int GameStatus, int betChip, Map<Player, Integer> playerBetMap, String groupID, Player playerOf) throws Exception {
        // the base for every player move
        int blindValue = GameConstant.Blind.getValue();

        int biggestBetOnTheTable = playerBetMap.values().stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);

        int thePlayerToMove = gameClock.get(groupID);

        if (GameStatus == Game.GAME_PREFLOP) {
            for (Player per : playerSet) {
                if (per.getPosition() == thePlayerToMove) {
                    if (Math.floorDiv(betChip, blindValue) >= 1 && betChip >= biggestBetOnTheTable) {

                        return "You bet: " + betChip;
                    } else {
                        return "You have to at least bet " + blindValue;
                    }
                }
            }
        }

        return null;
    }

    public static String handPreFlop(Set<Player> playerSet, int GameStatus, int betChip, Map<Player, Integer> playerBetMap, String groupID, Player playerOf) {
        // the base for every player move
        int blindValue = GameConstant.Blind.getValue();

        int biggestBetOnTheTable = playerBetMap.values().stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);

        int thePlayerToMove = gameClock.get(groupID);

        for (Player per : playerSet) {
            if (per.getPosition() == thePlayerToMove) {
                if (Math.floorDiv(betChip, blindValue) >= 1 && betChip >= biggestBetOnTheTable) {
                    per.bet(betChip);
                    gameClock.put(groupID, thePlayerToMove++);
                    return per.getUserName() + " 下注： " + betChip;
                } else {
                    return "你至少要下注 " + blindValue;
                }
            }
        }

        return null;
    }

    public static void setSmallAndBigBlind(Set<Player> playerPosSet, Map<Player, Integer> playerBetMap, String groupID) {
        int blindValue = GameConstant.Blind.getValue();

        /*
         * playerBetMap<Player, Int>
         * */
        for (Player player : playerPosSet) {
            if (player.getPosition() == 0) {
                player.bet(blindValue);
                playerBetMap.put(player, blindValue);
            }
            if (player.getPosition() == 1) {
                player.bet(blindValue * 2);
                playerBetMap.put(player, blindValue * 2);
            }
        }
        gameClock.put(groupID, GameConstant.BigBlindPlus1.getValue());
    }

    public static String handPreFlop2Players(Set<Player> playerSet, int betChip, Map<Player, Integer> playerBetMap, String groupID, Player playerOf) {

        int blindValue = GameConstant.Blind.getValue();

        int biggestBetOnTheTable = playerBetMap.values().stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);

        int totalBet = playerOf.getChipOnTheTable() + betChip;


        /*
         * IF it's not ur turn and you use /bet cmd
         *       out("not your turn!");
         * else
         *       you bet xxx
         * */
        int turn = gameClock.get(groupID);
        System.out.println("----turn value---------");
        System.out.println(turn);
        System.out.println("-------------");

        if (turn % 2 == GameConstant.SmallBlind.getValue()) {
            // small blind
            if (playerOf.getPosition() != GameConstant.SmallBlind.getValue()) {
                return "It's not your turn to bet!";
            } else {
                if (playerOf.getChip() < betChip) {
                    return "You don't have enough money to bet";
                }
                if (Math.floorDiv(betChip, blindValue) >= 1 && totalBet >= biggestBetOnTheTable) {
                    playerOf.bet(betChip);
                    playerBetMap.put(playerOf, totalBet);
                    gameClock.put(groupID, turn + 1);
                    System.out.println("-------------");
                    System.out.println("Small blind~~!!");
                    System.out.println("-------------");
                    return playerOf.getUserName() + " 總下注： " + totalBet;
                } else {
                    return "你至少要下注 " + blindValue;
                }
            }
        } else {
            // big blind
            if (playerOf.getPosition() != GameConstant.BigBlind.getValue()) {
                return "It's not your turn to bet!";
            } else {
                if (playerOf.getChip() < betChip) {
                    return "You don't have enough money to bet";
                }
                if (Math.floorDiv(betChip, blindValue) >= 1 && totalBet >= biggestBetOnTheTable) {
                    playerOf.bet(betChip);
                    playerBetMap.put(playerOf, totalBet);
                    gameClock.put(groupID, turn + 1);
                    System.out.println("-------------");
                    System.out.println("Big blind~~!!");
                    System.out.println("-------------");
                    return playerOf.getUserName() + " 總下注： " + totalBet;
                } else {
                    return "你至少要下注 " + blindValue;
                }
            }

        }


        // 2 players game, dont need clock
//        int thePlayerToMove = gameClock.get(groupID);
//            gameClock.put(groupID, thePlayerToMove++);


        // no reminder means, it's small blind turn
//        if (thePlayerToMove % 2 == 0) {
//            if (Math.floorDiv(betChip, blindValue) >= 1 && totalBet >= biggestBetOnTheTable) {
//                playerOf.bet(betChip);
//                // TODO reset gameClock
//                gameClock.put(groupID, thePlayerToMove++);
//                playerBetMap.put(playerOf, totalBet);
//                return playerOf.getUserName() + " 總下注： " + totalBet;
//            } else {
//                return "你至少要下注 " + blindValue;
//            }
//        } else {
//            if (Math.floorDiv(betChip, blindValue) >= 1 && totalBet >= biggestBetOnTheTable) {
//                playerOf.bet(betChip);
//                // TODO reset gameClock
//                gameClock.put(groupID, thePlayerToMove++);
//                playerBetMap.put(playerOf, totalBet);
//                return playerOf.getUserName() + " 總下注： " + totalBet;
//            } else {
//                return "你至少要下注 " + blindValue;
//            }
//        }
    }

    public static String handle2PlayerCheck(Set<Player> playerSet, int betChip, Map<Player, Integer> playerBetMap, String groupID, Player playerOf) {
        int turn = gameClock.get(groupID);
        if (turn % 2 == GameConstant.SmallBlind.getValue()) {
            if (playerOf.getPosition() != GameConstant.SmallBlind.getValue()) {
                return "Not your turn";
            } else {
                playerOf.setCheck();
                gameClock.put(groupID, turn + 1);
                return "You checked!";
            }
        } else {
            if (playerOf.getPosition() != GameConstant.BigBlind.getValue()) {
                return "Not your turn";
            } else {
                playerOf.setCheck();
                gameClock.put(groupID, turn + 1);
                return "You checked!";
            }
        }
    }

    public static int potDistribute(SortedSet<Player> playerRanking, String groupID) {

        Player winner = playerRanking.first();

        Integer pot = GameController.getPotPool(groupID);

        winner.addChip(pot);

        return winner.getChip();

    }
}
