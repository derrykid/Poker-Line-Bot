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
        map.put(BotCommand.DEBUG, (event) -> {
            StringBuilder system = new StringBuilder();
            system.append("gameMap Size:" + Deal.getGameMapSize() + "\n"
                    + "my deck remains: " + Deal.getGameMap().get(event.getSource().getSenderId()).getDeck().size() + "\n"
            );
            return new TextMessage(system.toString());
        });
        map.put(BotCommand.RESTART, (event) -> {

           /*
           * if the game exist, remove it and run command DEAL
           * if not exist, run the command DEAL straight away
           * */
            if (Deal.isExist(event)){
                // delete the game and restart one
                // remove the reference
                Deal.getGameMap().remove(event.getSource().getSenderId());
                String cardDeal = Deal.deal(event);
                return EmojiProcesser.process(cardDeal);

            } else {
                /*
                * it's not exist, so create a new one
                * */

                String cardDeal = Deal.deal(event);
                return EmojiProcesser.process(cardDeal);
            }
        });
        map.put(BotCommand.DESTROY, (event) -> {
            Deal.getGameMap().remove(event.getSource().getSenderId());
            return new TextMessage("Game deleted");
        });
        map.put(BotCommand.DEAL,
                (event) -> {

            // TODO first deal and second time calling deal has different cards

                    // every event sent by user, same groupID will secure it's the same game
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

        String groupID = event.getSource().getSenderId();

        /*
        * check if it's in game, if so, accept check commands, etc
        * */
        try {
            if (Deal.getGameMap().get(groupID) != null && event.getMessage().getText().equalsIgnoreCase("check")) {
                // TODO game logic stuff
                /*
                * for now, whenever the use sends a text, it will proceed to next state
                * TODO check what user says and determine the event
                * */
                String cardDeal = Deal.proceed(event);
                return EmojiProcesser.process(cardDeal);
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return new TextMessage("Game proceed login error occurs");
        }

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
