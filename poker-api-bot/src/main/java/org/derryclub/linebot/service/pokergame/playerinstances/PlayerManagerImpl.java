package org.derryclub.linebot.service.pokergame.playerinstances;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import lombok.Data;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.util.LineServerInteractor;

import java.lang.reflect.InaccessibleObjectException;
import java.util.*;

@Data
public final class PlayerManagerImpl implements PlayerManager {

    private static PlayerManagerImpl instance;
    private final Map<String, Set<Player>> gamePlayers;

    private PlayerManagerImpl() {
        gamePlayers = new HashMap<>();
    }

    public static PlayerManager getManager() {
        if (instance == null) {
            instance = new PlayerManagerImpl();
        }
        return instance;
    }

    @Override
    public void createPlayer(String groupId, String userId) {
        Set<Player> players = new HashSet<>();
        players.add(new Player(userId, LineServerInteractor.getUserName(userId)));
        gamePlayers.put(groupId, players);
    }

    @Override
    public void addPlayer(String groupId, String userId) {
        Set<Player> players = gamePlayers.get(groupId);

        Optional<Player> isExisted = players.stream()
                        .filter(player -> player.getUserId().equalsIgnoreCase(userId))
                                .findAny();
        if (isExisted.isEmpty()) {
            players.add(new Player(userId, LineServerInteractor.getUserName(userId)));
        }
    }

    @Override
    public boolean addPlayer(MessageEvent<TextMessageContent> event) {

        boolean isPlusOne = event.getMessage().getText().equalsIgnoreCase("+1");

        if (isPlusOne) {
            Source source = event.getSource();
            String groupId = source.getSenderId();
            String userId = source.getUserId();

            Set<Player> players = gamePlayers.get(groupId);

            Optional<Player> isExisted = players.stream()
                    .filter(player -> player.getUserId().equalsIgnoreCase(userId))
                    .findAny();

            if (isExisted.isEmpty()) {
                return players.add(new Player(userId, LineServerInteractor.getUserName(userId)));
            }
        }
        return false;
    }

    @Override
    public boolean removePlayer(String groupId, String userId) {
        Set<Player> players = gamePlayers.get(groupId);
        Optional<Player> toRemove = players.stream()
                .filter(player -> player.getUserId().equalsIgnoreCase(userId))
                .findAny();
        return toRemove.filter(players::remove).isPresent();
    }

    @Override
    public Set<Player> getPlayers(String groupId) {
        return gamePlayers.get(groupId);
    }

    @Override
    public Player getPlayer(String groupId, String userId) {
        Optional<Player> isExist = getPlayers(groupId)
                .stream()
                .filter(player -> player.getUserId().equals(userId))
                .findAny();

        return isExist.orElseThrow(InaccessibleObjectException::new);
    }

    public static void setBackStatus(String groupId) {
        instance.gamePlayers.get(groupId).stream()
                .filter(p -> p.getPlayerStatue() != Player.PlayerStatus.FOLD)
                .forEach(p -> p.check());
    }
}
