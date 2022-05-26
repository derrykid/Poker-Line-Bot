package org.derryclub.linebot.card;

public enum Rank {

    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6",6),
    SEVEN("7",7),
    EIGHT("8",8),
    NINE("9",9),
    TEN("T", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    ACE("A", 14);

    // this is used for EmojiProcessor
    private final String rankValueString;
    private final int rankValue;

    Rank(final String rankValueString, final int rankValue){
        this.rankValueString = rankValueString;
        this.rankValue = rankValue;
    }

    public int getRankValue(){
        return this.rankValue;
    }

    @Override
    public String toString(){
        return this.rankValueString;
    }
}
