package org.derryclub.linebot.game.position;

public enum TablePosition {

    // player position number
    SMALL_BLIND(0),
    BIG_BLIND(1),
    BIG_BLIND_PLUS_1(2),
    BIG_BLIND_PLUS_2(3),
    BIG_BLIND_PLUS_3(4),
    BIG_BLIND_PLUS_4(5),
    BIG_BLIND_PLUS_5(6),
    BIG_BLIND_PLUS_6(7),

    // blind value
    Blind(10);

    public final int value;

    TablePosition(int value) {
        this.value = value;
    }

}
