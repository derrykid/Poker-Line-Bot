package org.derryclub.linebot.service.pokergame.gamecontrol;

import lombok.Getter;
import org.derryclub.linebot.service.pokergame.gameinstances.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManager;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManager;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;

@Getter
public abstract class GameControl implements Gaming {
    /**
     *  Require a manager to handle game control
     */
    private final GameManager gameManagerImpl;
    private final PlayerManager playerManagerImpl;
    private final CommunityCardManager communityCardManager;

    public GameControl() {
        this.gameManagerImpl = GameManagerImpl.getManager();
        this.playerManagerImpl = PlayerManagerImpl.getManager();
        this.communityCardManager = CommunityCardManager.getManager();
    }

}
