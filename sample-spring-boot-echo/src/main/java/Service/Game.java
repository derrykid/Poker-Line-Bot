package Service;

import lombok.Value;
import poker.Deck;

public class Game {

    // groupID as gameID
    private String gameId;
    private Deck deck;
    private int gameState;

    // later implementation
    private int numberOfPlayer;

    public static final int GAME_PREHAND_STATE = 0;
    public static final int GAME_PUBLIC_STATE = 1;
    public static final int GAME_TURN_STATE = 2;
    public static final int GAME_RIVER_STATE = 3;
    public static final int GAME_OVER = 4;

    public Game(String gameId, Deck deck) {
        this.gameId = gameId;
        this.deck = deck;
        this.gameState = GAME_PREHAND_STATE;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
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
        if (gameId != null ? !gameId.equals(game.gameId) : game.gameId != null) return false;
        return deck != null ? deck.equals(game.deck) : game.deck == null;
    }

    @Override
    public int hashCode() {
        int result = gameId != null ? gameId.hashCode() : 0;
        result = 31 * result + (deck != null ? deck.hashCode() : 0);
        result = 31 * result + gameState;
        return result;
    }
}
