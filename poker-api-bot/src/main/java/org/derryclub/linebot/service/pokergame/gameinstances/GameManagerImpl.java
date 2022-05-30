package org.derryclub.linebot.service.pokergame.gameinstances;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.Data;
import org.derryclub.linebot.gameConfig.Game;

import java.util.HashMap;
import java.util.Map;

@Data
public final class GameManagerImpl implements GameManager {

    private static GameManager instance;

    private final Map<String, Game> gameMap;

    private GameManagerImpl() {
        gameMap = new HashMap<>();
    }

    public void createGame(String groupId) {
        gameMap.put(groupId, Game.newGame());
    }

    public static GameManager getManager() {
        if (instance == null) {
            instance = new GameManagerImpl();
        }
        return instance;
    }

    @Override
    public int getOnGoingGames() {
        return gameMap.size();
    }

    @Override
    public boolean isAddingPlayerStage(MessageEvent<TextMessageContent> event) {
        return isGameExist(event) &&
                getGame(event.getSource().getSenderId())
                        .getGameStage() == Game.GameStage.GAME_ADDING_PLAYER;
    }

    public Game getGame(String groupId) {
        return gameMap.get(groupId);
    }

    @Override
    public boolean isGameExist(MessageEvent<TextMessageContent> event) {
        return gameMap.containsKey(event.getSource().getSenderId());
    }

    @Override
    public void gameFinished(String groupId) {
        gameMap.remove(groupId);
    }
}
