package org.derryclub.linebot.service.pokergame.gameinstances;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.service.pokergame.Manager;

public interface GameManager extends Manager {
    void createGame(String groupId);
    Game getGame(String groupId);
    boolean isGameExist(MessageEvent<TextMessageContent> event);
    int getOnGoingGames();

    boolean isAddingPlayerStage(MessageEvent<TextMessageContent> event);

    void gameFinished(String groupId);

}
