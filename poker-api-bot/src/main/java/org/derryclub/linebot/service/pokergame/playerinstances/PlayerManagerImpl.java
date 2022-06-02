package org.derryclub.linebot.service.pokergame.playerinstances;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import lombok.Data;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.util.LineServerInteractor;

import java.lang.reflect.InaccessibleObjectException;
import java.util.*;

@Data
public final class PlayerManagerImpl implements PlayerManager {

    private static PlayerManagerImpl instance;
    private final Map<String, Set<Player>> gamePlayers;
    private final Map<String, Player> playerInfoMap;

    private PlayerManagerImpl() {
        gamePlayers = new HashMap<>();
        playerInfoMap = new HashMap<>();
    }

    public static PlayerManager getManager() {
        if (instance == null) {
            instance = new PlayerManagerImpl();
        }
        return instance;
    }

    @Override
    public void createPlayer(String groupId, String userId) {

        Set<Player> playersWhoWantsToPlayThisGame = new HashSet<>();

        Optional<Player> isPlayedBefore = Optional.ofNullable(playerInfoMap.get(userId));

        if (isPlayedBefore.isPresent()) {
            playersWhoWantsToPlayThisGame.add(playerInfoMap.get(userId));
        } else {
            Player player = new Player(userId, LineServerInteractor.getUserName(userId));
            playerInfoMap.put(userId, player);
            playersWhoWantsToPlayThisGame.add(player);
        }
        gamePlayers.put(groupId, playersWhoWantsToPlayThisGame);
    }

    @Override
    public boolean plusOneCommandAddPlayer(MessageEvent<TextMessageContent> event) {

        boolean isPlusOneCmd = event.getMessage().getText().equalsIgnoreCase("+1");

        if (isPlusOneCmd) {
            Source source = event.getSource();
            String groupId = source.getSenderId();
            String userId = source.getUserId();

            Set<Player> players = gamePlayers.get(groupId);

            Optional<Player> isPlayedBefore = Optional.ofNullable(playerInfoMap.get(userId));

            if (isPlayedBefore.isPresent()) {
                return players.add(playerInfoMap.get(userId));
            }

            Player newParticipant = new Player(userId, LineServerInteractor.getUserName(userId));
            playerInfoMap.put(userId, newParticipant);

            return players.add(newParticipant);
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

    /**
     * Set all players to ready state (or ALIVE state, to be specific)
     */
    public static void setBackStatus(String groupId) {
        instance.gamePlayers.get(groupId).stream()
                .filter(p -> p.getPlayerStatue() != Player.PlayerStatus.FOLD)
                .forEach(Player::ready);
    }

    public static Player nextPlayerToPlay(String groupId, int whoseTurn) {
        return instance.gamePlayers.get(groupId).stream()
                .filter(p -> p.getPosition().getValue() == whoseTurn)
                .findAny().orElseThrow(RuntimeException::new);
    }
}
