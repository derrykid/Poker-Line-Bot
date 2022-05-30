package org.derryclub.linebot.gameConfig.position;

public enum TableConfig {

    // player position number
    SMALL_BLIND(0, "小盲"),
    BIG_BLIND(1, "大盲"),
    BIG_BLIND_PLUS_1(2, "大盲+1"),
    BIG_BLIND_PLUS_2(3, "大盲+2"),
    BIG_BLIND_PLUS_3(4, "大盲+3"),
    BIG_BLIND_PLUS_4(5, "大盲+4"),
    BIG_BLIND_PLUS_5(6, "大盲+5"),
    BIG_BLIND_PLUS_6(7, "大盲+6");


    public final int value;
    public final String positionName;

    TableConfig(int value, String name) {
        this.value = value;
        this.positionName = name;
    }

    public int getValue(){
        return this.value;
    }

    public String getPositionName() {
        return this.positionName;
    }

}
