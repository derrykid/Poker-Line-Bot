package org.derryclub.linebot.gameConfig.chip;

public interface Chip {
    int getAvailableChip();
    void bet(int chip);
    void gainChip(int chip);
}
