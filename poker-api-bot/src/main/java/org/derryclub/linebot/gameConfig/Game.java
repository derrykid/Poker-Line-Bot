package org.derryclub.linebot.gameConfig;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.derryclub.linebot.gameConfig.blind.Blind;
import org.derryclub.linebot.poker.card.Deck;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;

@Getter
@EqualsAndHashCode
public class Game {

    private final Deck deck;
    private GameStage gameStage;
    private final int smallBlind = Blind.SMALL_BLIND.value;
    @Setter
    private int whoseTurnToMove;

    public enum GameStage {
        GAME_ADDING_PLAYER(0),
        GAME_PREFLOP(1),
        GAME_FLOP(2),
        GAME_TURN_STATE(3),
        GAME_RIVER_STATE(4),
        GAME_OVER(-1);
        public final int value;
        GameStage(int value) {
            this.value = value;
        }
    }

    private Game(Deck deck) {
        this.deck = deck;
        this.gameStage = GameStage.GAME_ADDING_PLAYER;
        this.whoseTurnToMove = 0;
    }

    public static Game newGame(){
        return new Game(Deck.newShuffledSingleDeck());
    }

    public Deck getDeck() {
        return deck;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }


}