package org.derryclub.linebot.service.pokergame.playermanage;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.pokergame.Manager;

import java.util.Set;

public interface PlayerManager extends Manager {

    boolean plusOneCommandAddPlayer(MessageEvent<TextMessageContent> event);

    boolean removePlayer(String groupId, String userId);

    Set<Player> getPlayers(String groupId);

    Player getPlayer(String groupId, String userId);

    void createPlayer(String groupId, String userId);
}
