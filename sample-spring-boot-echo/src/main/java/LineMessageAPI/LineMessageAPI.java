package LineMessageAPI;

import Constant.BotCommand;
import Processor.BotCommandProcessor;
import Processor.GameCommandProcessor;
import Processor.GameController;
import com.linecorp.bot.model.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
@Slf4j
public class LineMessageAPI {

    static {
        new BotCommandProcessor();
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("event: " + event);



        /*
         * Check if the group is in game state,
         * if it's true, user input matters,
         * if it's false, listen to bot commands event
         * */
        if (GameController.isGameExist(event)) {
            Message gameMessage = null;
            try {
                gameMessage = GameController.handle(event);
            } catch (Throwable e) {
                e.printStackTrace();
                return new TextMessage("Mail.Controller if statement, something went wrong");
            }
            return gameMessage;
        }
        /*
         * Process commands
         * */
        Message message = BotCommandProcessor.handle(event);
        return message;
    }


    public static void main(String[] args) {
        SpringApplication.run(LineMessageAPI.class, args);
    }

}
