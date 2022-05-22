package game;

import card.Deck;

public class Game {

    private final Deck deck;
    private int gameState;
    private int smallBlind;

    public static final int GAME_PREFLOP = 0;
    public static final int GAME_FLOP = 1;
    public static final int GAME_TURN_STATE = 2;
    public static final int GAME_RIVER_STATE = 3;
    public static final int GAME_OVER = 4;
    public static final int GAME_ADDING_PLAYER = 5;

    public Game(Deck deck) {
        this.deck = deck;
        this.gameState = GAME_PREFLOP;
    }

    public void setSmallBlind(int smallBlind){
        this.smallBlind = smallBlind;
    }

    public int getSmallBlind() {
        return this.smallBlind;
    }


    public Deck getDeck() {
        return deck;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (gameState != game.gameState) return false;
        return deck != null ? deck.equals(game.deck) : game.deck == null;
    }

    @Override
    public int hashCode() {
        int result = deck != null ? deck.hashCode() : 0;
        result = 31 * result + gameState;
        return result;
    }
}
