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
import org.derryclub.linebot.processor.GameController;

@LineMessageHandler
@Slf4j
public class LineMessageAPI {

    // pregame cmd
    // ingame cmd
    private static final PregameCommandReceiver pregameCommandReceiver = new PregameCommandReceiver();
    private static final GameCommandReceiver gameCommandReceiver = new GameCommandReceiver();

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("event: " + event);

        /*
         * Check if the group is in game state,
         * if it's true, user input matters,
         * if it's false, listen to bot commands event
         * */
        if (GameController.isGameExist(event)) {
            Message gameMessage;
            try {
                gameMessage = GameController.handle(event);
            } catch (Throwable e) {
                e.printStackTrace();
                return new TextMessage("Main.Controller if statement, something went wrong");
            }
            return gameMessage;
        }
        /*
         * Process commands
         * */
        return pregameCommandReceiver.getCommand(event);
    }

}
