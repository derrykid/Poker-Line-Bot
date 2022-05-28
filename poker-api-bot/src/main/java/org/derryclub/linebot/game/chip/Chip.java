package org.derryclub.linebot.game.chip;

public interface Chip {
    int getChip();
    void bet(int chip);
    void gainChip(int chip);
}
