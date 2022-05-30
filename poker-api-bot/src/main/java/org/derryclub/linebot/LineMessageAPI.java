package org.derryclub.linebot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.commands.ingame.GameCommandReceiver;
import org.derryclub.linebot.commands.pregame.PregameCommandReceiver;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControl;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControlSystem;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;

/**
 * This class handles all incoming events
 * <br/>
 * Dispense command to the responsive handler
 */
@LineMessageHandler
@Slf4j
public class LineMessageAPI implements EventHandler {

    private final PregameCommandReceiver pregameCommandReceiver;
    private final GameCommandReceiver gameCommandReceiver;

    private LineMessageAPI() {
        pregameCommandReceiver = PregameCommandReceiver.getInstance();
        gameCommandReceiver = GameCommandReceiver.getInstance();
    }

    @Override
    @EventMapping
    public Message handleEvent(MessageEvent<TextMessageContent> event) {

        log.info("User event: {}", event);

        // If user text doesn't start with "/", it's not a command
        if (!event.getMessage().getText().startsWith("/")) {
            return null;
        }

        // Adding player is a special event, in which user can simply '+1' to enroll
        if (GameManagerImpl.getManager().isAddingPlayerStage(event)) {
            return PlayerManagerImpl.getManager().addPlayer(event)
                    ? new TextMessage("Welcome!")
                    : new TextMessage("要玩快++ 加入過得先等其他人。");
        }

        return GameManagerImpl.getManager().isGameExist(event)
               ? gameCommandReceiver.handle(event)
               : pregameCommandReceiver.handle(event);

    }
}
