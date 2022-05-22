package game;

import card.Card;
import poker.analyzer.Classification;

import java.util.Set;
import java.util.TreeSet;

public class Player {

    public static final int ALIVE = 0;
    public static final int CHECK = 1;
    public static final int FOLD = 2;


    private String userID;
    /*
     * position starts with 0
     * 0 - small blind
     * 1 - big blind, etc
     * */
    private int position;
    private int chip;
    private Set<Card> playerCards;
    private Classification handClassification;
    private String userName;
    private int playerStatue;
    private int chipOnTheTable;

    public int getChipOnTheTable() {
        return this.chipOnTheTable;
    }

    private void addChipOnTheTable(int moneyBet) {
        this.chipOnTheTable = this.chipOnTheTable + moneyBet;
    }

    public void clearChipOnTheTable() {
        this.chipOnTheTable = 0;
    }

    public void setCheck() {
        this.playerStatue = CHECK;
    }

    public void foldHand() {
        this.playerStatue = FOLD;
    }

    public int getPlayerStatue() {
        return this.playerStatue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (position != player.position) return false;
        if (chip != player.chip) return false;
        if (userID != null ? !userID.equals(player.userID) : player.userID != null) return false;
        if (playerCards != null ? !playerCards.equals(player.playerCards) : player.playerCards != null) return false;
        if (handClassification != null ? !handClassification.equals(player.handClassification) : player.handClassification != null)
            return false;
        return userName != null ? userName.equals(player.userName) : player.userName == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + position;
        result = 31 * result + chip;
        result = 31 * result + (playerCards != null ? playerCards.hashCode() : 0);
        result = 31 * result + (handClassification != null ? handClassification.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setHandClassification(Classification classification) {
        this.handClassification = classification;
    }

    public Classification getHandClassification() {
        return this.handClassification;
    }

    public void addPlayerCards(Card card) {
        playerCards.add(card);
    }

    public Set<Card> getPlayerCards() {
        return this.playerCards;
    }

    @Override
    public String toString() {
        return "Player{" +
                "userID='" + userID + '\'' +
                ", position=" + position +
                ", chip=" + chip +
                ", playerCards=" + playerCards +
                '}';
    }

    public Player(String userID, String userName) {
        this.userID = userID;
        this.playerCards = new TreeSet<>();
        this.userName = userName;
        this.playerStatue = ALIVE;
        this.chip = 1000;
        this.chipOnTheTable = 0;
    }

    public String getUserID() {
        return userID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChip() {
        return chip;
    }

    public void setChip(int chip) {
        this.chip = chip;
    }

    public void bet(int chip) {
        addChipOnTheTable(chip);
        this.chip = this.chip - chip;
    }

    public void addChip(int pot){
        this.chip = this.chip + pot;
    }

}
