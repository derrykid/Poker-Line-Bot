package LineMessageAPI;

import Constant.BotCommand;
import Processor.BotCommandProcessor;
import Processor.FunctionThrowable;
import Processor.GameController;
import Processor.EmojiProcesser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
@LineMessageHandler
@Slf4j
public class LineMessageAPI {

    private Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> map = BotCommandProcessor.getCommandMap();

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        // write event to log
        log.info("event: " + event);

        final String command = event.getMessage().getText().split(" ")[0];
        BotCommand botCommand = BotCommand.getBotCommand(command);

        FunctionThrowable<MessageEvent<TextMessageContent>, Message> action = map.get(botCommand);

        return action.apply(event);

        // TODO finish the refactor
        /*
         * check if it's in game, if so, accept check commands, etc
         * */
        try {
            if (GameController.getGameMap().get(groupID) != null && event.getMessage().getText().equalsIgnoreCase("check")) {
                // TODO game logic stuff
                /*
                 * for now, whenever the use sends a text, it will proceed to next state
                 * TODO check what user says and determine the event
                 * */
                String cardDeal = GameController.proceed(event);
                return EmojiProcesser.process(cardDeal);
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return new TextMessage("Game proceed login error occurs");
        }

        try {

            if (botCommand == null) {
                //handleMismatchEvent(event);
                return null;
            }
            FunctionThrowable<MessageEvent<TextMessageContent>, Message> action = map.get(botCommand);

            // previous code
            return action.apply(event);

        } catch (Exception e) {
            e.printStackTrace();
            return new TextMessage("Handle text message error occurs");
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(LineMessageAPI.class, args);
    }

}
