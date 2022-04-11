package Game;

public enum GameConstant {
    Blind(10);

    private final int value;

    GameConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
