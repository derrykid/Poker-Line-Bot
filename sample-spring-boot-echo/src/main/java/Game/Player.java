package Game;

import Card.Card;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private String userID;
    /*
    * position starts with 0
    * 0 - small blind
    * 1 - big blind, etc
    * */
    private int position;
    private int chip;
    private StringBuilder holeCards;
    private Set<Card> playerCards;

    public void addPlayerCards(Card card){
        playerCards.add(card);
    }

    public Set<Card> getPlayerCards() {
        return this.playerCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (position != player.position) return false;
        if (chip != player.chip) return false;
        if (userID != null ? !userID.equals(player.userID) : player.userID != null) return false;
        if (holeCards != null ? !holeCards.equals(player.holeCards) : player.holeCards != null) return false;
        return playerCards != null ? playerCards.equals(player.playerCards) : player.playerCards == null;
    }

    @Override
    public String toString() {
        return "Player{" +
                "userID='" + userID + '\'' +
                ", position=" + position +
                ", chip=" + chip +
                ", holeCards=" + holeCards +
                ", playerCards=" + playerCards +
                '}';
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + position;
        result = 31 * result + chip;
        result = 31 * result + (holeCards != null ? holeCards.hashCode() : 0);
        result = 31 * result + (playerCards != null ? playerCards.hashCode() : 0);
        return result;
    }

    public Player(String userID) {
        this.userID = userID;
        this.playerCards = new HashSet<>();
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

}
