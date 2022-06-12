package org.derryclub.linebot.service.pokergame.gamecontrol;

import lombok.Getter;
import org.derryclub.linebot.service.pokergame.gamemanage.CommunityCardManager;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManager;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManager;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;

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
