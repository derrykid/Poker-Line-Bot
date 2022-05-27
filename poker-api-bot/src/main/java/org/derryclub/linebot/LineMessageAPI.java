package org.derryclub.linebot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * This class handles all incoming events
 */
@LineMessageHandler
@Slf4j
public final class LineMessageAPI implements EventHandler {

    @Override
    @EventMapping
    public Message handleEvent(MessageEvent<TextMessageContent> event) {

        log.info("User event: {}", event);

        /*
         * Check if the group is in game state,
         * if it's true, user input matters,
         * if it's false, listen to bot commands event
         * */
//        if (GameController.isGameExist(event)) {
//            Message gameMessage;
//            try {
//                gameMessage = GameController.handle(event);
//            } catch (Throwable e) {
//                e.printStackTrace();
//                return new TextMessage("Main.Controller if statement, something went wrong");
//            }
//            return gameMessage;
//        }
//        /*
//         * Process commands
//         * */
//        return pregameCommandReceiver.getCommand(event);
        return null;
    }
}
