package org.derryclub.linebot.game.chip;

public class ChipImpl implements Chip {
    private static final int INIT_CHIP = 1000;
    private int chip;

    public ChipImpl() {
        this.chip = INIT_CHIP;
    }

    @Override
    public int getChip() {
        return chip;
    }

    @Override
    public void bet(int chip) {
        this.chip -= chip;
    }

    @Override
    public void gainChip(int chip) {
        this.chip += chip;
    }

    @Override
    public String toString(){
        return String.valueOf(chip);
    }
}
