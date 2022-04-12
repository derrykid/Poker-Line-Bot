package Game;

public enum GameConstant {

    // player position number
    SmallBlind(0),
    BigBlind(1),
    BigBlindPlus1(2),
    BigBlindPlus2(3),
    BigBlindPlus3(4),
    BigBlindPlus4(5),
    BigBlindPlus5(6),
    BigBlindPlus6(7),

    // blind value
    Blind(10);

    private final int value;

    GameConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
