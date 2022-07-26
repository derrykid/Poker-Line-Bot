package org.derryclub.linebot.service.pokergame.playermanage;

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
    private final Map<String, Player> playerCache;

    private PlayerManagerImpl() {
        gamePlayers = new HashMap<>();
        playerCache = new HashMap<>();
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

        Optional<Player> isPlayedBefore = Optional.ofNullable(playerCache.get(userId));

        if (isPlayedBefore.isPresent()) {
            playersWhoWantsToPlayThisGame.add(playerCache.get(userId));
        } else {
            Player player = new Player(userId, LineServerInteractor.getUserName(userId));
            playerCache.put(userId, player);
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

            Optional<Player> isPlayedBefore = Optional.ofNullable(playerCache.get(userId));

            if (isPlayedBefore.isPresent()) {
                // if 0 chip, can't play
                if (isPlayedBefore.get().getChip().getAvailableChip() <= 0) {
                    return false;
                }
                return players.add(playerCache.get(userId));
            }

            Player newParticipant = new Player(userId, LineServerInteractor.getUserName(userId));
            playerCache.put(userId, newParticipant);

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

    public static Player getWhoseTurn(String groupId, int whoseTurn) {
        int thePlayerWhoShouldMakeAMove = whoseTurn % instance.getPlayers(groupId).size();
        return instance.gamePlayers.get(groupId).stream()
                .filter(p -> p.getPosition().getValue() == thePlayerWhoShouldMakeAMove)
                .findAny().orElseThrow(InaccessibleObjectException::new);
    }

    public static Player nextPlayerToPlay(String groupId, int whoseTurn) {
        int nextOne = (whoseTurn + 1) % PlayerManagerImpl.getManager().getPlayers(groupId).size();
        return instance.gamePlayers.get(groupId).stream()
                .filter(p -> p.getPosition().getValue() == nextOne)
                .findAny().orElseThrow(InaccessibleObjectException::new);
    }
}
