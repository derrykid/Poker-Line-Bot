package org.derryclub.linebot.poker.analyzer;

public enum ClassificationRank {

    HIGH_CARD(1, "高牌"),
    PAIR(2, "一對"),
    TWO_PAIR(3, "兩對"),
    SET(4, "三條"),
    WHEEL(5, "順子A 2 3 4 5"),
    STRAIGHT(6, "順子"),
    FLUSH(7, "同花"),
    FULL_HOUSE(8, "葫蘆"),
    FOUR_OF_A_KIND(9, "鐵支"),
    STRAIGHT_FLUSH_WHEEL(10, "同花順 A 2 3 4 5"),
    STRAIGHT_FLUSH(11, "同花順"),
    ROYAL_FLUSH(12, "皇家同花順");


    private final int value;
    private final String name;

    ClassificationRank(int value, String name) {
        this.value = value;
        this.name = name;
    }
    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

}
