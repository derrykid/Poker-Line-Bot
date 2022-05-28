package org.derryclub.linebot.game.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.derryclub.linebot.game.chip.Chip;
import org.derryclub.linebot.game.chip.ChipImpl;
import org.derryclub.linebot.game.position.TablePosition;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.analyzer.Classification;

import java.util.Set;
import java.util.TreeSet;

@Getter
@EqualsAndHashCode
public class Player {

    private final String userId;
    private final String userName;
    private PlayerStatus playerStatue;
    private Chip chip;
    private int chipOnTheTable;

    private final Set<Card> playerCards;
    private TablePosition position;
    private Classification handClassification;

    @AllArgsConstructor
    enum PlayerStatus {
        ALIVE(0), CHECK(1), FOLD(2);
        public final int value;
        public String toString(){
            return String.valueOf(value);
        }
    }

    public Player(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.playerStatue = PlayerStatus.ALIVE;
        this.chip = new ChipImpl();
        this.chipOnTheTable = 0;
        this.playerCards = new TreeSet<>();
    }

    public int getChipOnTheTable() {
        return this.chipOnTheTable;
    }

    private void addChipOnTheTable(int moneyBet) {
        this.chipOnTheTable = this.chipOnTheTable + moneyBet;
    }

    public void clearChipOnTheTable() {
        this.chipOnTheTable = 0;
    }

    public void check() {
        this.playerStatue = PlayerStatus.CHECK;
    }

    public void fold() {
        this.playerStatue = PlayerStatus.FOLD;
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
                "userID='" + userId + '\'' +
                ", position=" + position.value +
                ", chip=" + chip +
                ", playerCards=" + playerCards +
                '}';
    }


    public void setPosition(TablePosition position) {
        this.position = position;
    }


}
