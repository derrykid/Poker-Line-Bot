package org.derryclub.linebot;

import lombok.Getter;
import org.derryclub.linebot.commands.ingame.GameCommandReceiver;
import org.derryclub.linebot.commands.pregame.PregameCommandReceiver;

@Getter
public final class Bootstrap {

    private final PregameCommandReceiver pregameCommandReceiver;
    private final GameCommandReceiver gameCommandReceiver;

    private Bootstrap() {
        pregameCommandReceiver = new PregameCommandReceiver();
        gameCommandReceiver = new GameCommandReceiver();
    }

}
