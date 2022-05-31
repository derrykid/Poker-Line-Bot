package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.pokergame.gamecontrol.DealHoleCards;
import org.derryclub.linebot.service.pokergame.gameinstances.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;
import org.derryclub.linebot.service.pokergame.util.TablePosition;
import org.derryclub.linebot.service.pokergame.pot.PotManager;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public final class GameStartCommand extends GameCommandAdapter {

    public GameStartCommand() {
        super("end", "Use /end to end adding players and start the game");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {

        String groupId = event.getSource().getSenderId();
        Game game = GameManagerImpl.getManager().getGame(groupId);
        Set<Player> players = PlayerManagerImpl.getManager().getPlayers(groupId);

        if (!(players.size() >= 2)) {
            return new TextMessage("Must have at least 2 people");
        }
        // change game stage
        game.setGameStage(Game.GameStage.GAME_PREFLOP);

        // set up the community cards map
        CommunityCardManager.getManager()
                .getCommunityCardsMap().put(groupId, new ArrayList<>());

        // set table position
        TreeSet<Player> sortedSeat = TablePosition.initPositionSetter(groupId, players);

        // deal hole cards to players
        DealHoleCards.dealHoleCards(sortedSeat, game.getDeck());

        // small blind and big blind bets
        PotManager.forcedBet(sortedSeat);

        // messages of the player position with player name and its forced bet value
        String positionMessage = TablePosition.positionMessage(sortedSeat);

        return new TextMessage(positionMessage);
    }
}
