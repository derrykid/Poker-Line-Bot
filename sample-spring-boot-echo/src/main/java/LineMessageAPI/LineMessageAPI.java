package LineMessageAPI;

import Controller.Deal;
import Service.EmojiProcesser;
import Service.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import poker.*;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
@LineMessageHandler
@Slf4j
public class LineMessageAPI {
    private Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> map;

    private class Test implements Cloneable {

    }

    @PostConstruct
    public void init() {
        map = Collections.synchronizedMap(new EnumMap<>(BotCommand.class));

        map.put(BotCommand.HELP,
                (event) -> new TextMessage("This is help API, your userID: " + event.getSource().getUserId())
        );
        map.put(BotCommand.DEAL,
                (event) -> {

            // TODO first deal and second time calling deal has different cards

                    // every event sent by user, same UserID will secure it's the same game
                    String cardDeal = Deal.deal(event);

                    // if it's river_state and cards are all dealt, call the poker API

                    return  EmojiProcesser.process(cardDeal);
                }
        );
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        // write event to log
        log.info("event: " + event);

        try {
            final String command = event.getMessage().getText().split(" ")[0];
            BotCommand botCommand = BotCommand.getBotCommand(command);

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

// TODO use group chat ID as the gameID
