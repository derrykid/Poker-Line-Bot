package org.derryclub.linebot.poker.card;

public enum Suit {

    SPADE(4, 's'),
    HEART(3, 'h'),
    DIAMOND(2, 'd'),
    CLUB(1, 'c');

    private final int suitValue;
   /*
   * suit initial for each
   * spade for s, etc.
   * */
    private final char suitInitial;

    Suit(int suitValue, char initial){
        this.suitValue = suitValue;
        this.suitInitial = initial;
    }

    public int getSuitValue(){
        return this.suitValue;
    }

    public char getSuitInitials(){
        return this.suitInitial;
    }

    @Override
    public String toString() {
        return String.valueOf(this.suitInitial);
    }
}
