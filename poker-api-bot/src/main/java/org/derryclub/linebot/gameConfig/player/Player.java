package org.derryclub.linebot.gameConfig.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.chip.Chip;
import org.derryclub.linebot.gameConfig.chip.ChipImpl;
import org.derryclub.linebot.gameConfig.position.TableConfig;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.poker.analyzer.Classification;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

@Getter
@EqualsAndHashCode
@Slf4j
public class Player implements Comparable<Player> {

    private final String userId;
    private final String userName;
    private PlayerStatus playerStatue;
    private final Chip chip;
    private int chipOnTheTable;

    private final Set<Card> playerCards;
    private TableConfig position;
    private Classification handClassification;

    public static final Predicate<Player> theOneLeftPredicate = player -> {
        Player.PlayerStatus status = player.getPlayerStatue();
        return status != Player.PlayerStatus.FOLD ? true : false;
    };

    @Override
    public int compareTo(@NotNull Player o) {
        return this.position.compareTo(o.position);
    }


    @AllArgsConstructor
    public enum PlayerStatus {
        ALIVE(0), CHECK(1), FOLD(2);
        public final int value;

        public String toString() {
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

    public void bet(int moneyBet) {
        this.chipOnTheTable = this.chipOnTheTable + moneyBet;
    }

    public void clearChipOnTheTable() {
        this.chipOnTheTable = 0;
    }

    /**
     * Set player status to ALIVE,
     * it's a 'ready' state, which player can make move. It's not fold hand, nor check.
     */
    public void ready() {
        this.playerStatue = PlayerStatus.ALIVE;
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


    public void setPosition(int position) {
        switch (position) {
            case 0:
                this.position = TableConfig.SMALL_BLIND;
                break;
            case 1:
                this.position = TableConfig.BIG_BLIND;
                break;
            case 2:
                this.position = TableConfig.BIG_BLIND_PLUS_1;
                break;
            case 3:
                this.position = TableConfig.BIG_BLIND_PLUS_2;
                break;
            case 4:
                this.position = TableConfig.BIG_BLIND_PLUS_3;
                break;
            case 5:
                this.position = TableConfig.BIG_BLIND_PLUS_4;
                break;
            case 6:
                this.position = TableConfig.BIG_BLIND_PLUS_5;
                break;
            case 7:
                this.position = TableConfig.BIG_BLIND_PLUS_6;
                break;
            default:
                log.warn("Set position error occurs, shouldn't reach default case");
        }
    }


}
