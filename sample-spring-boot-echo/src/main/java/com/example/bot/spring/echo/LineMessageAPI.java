package com.example.bot.spring.echo;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.core.env.Environment;
import poker.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@LineMessageHandler
@Slf4j
public class LineMessageAPI {
    private Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> map;

    private class Test implements Cloneable{

    }

    @PostConstruct
    public void init() {
        map = Collections.synchronizedMap(new EnumMap<>(BotCommand.class));
        map.put(BotCommand.HELP,
                (event) -> {
                    return new TextMessage("This is help API, your userID: " + event.getSource().getUserId());
                }
        );
        map.put(BotCommand.DEAL,
                (event) -> {

                    // create a deck of card
                    Deck deck = Deck.newShuffledSingleDeck();

                    // bot deliver start hand to user

                    final String startHand = FivePokerHand.getStartHand(deck);


                    return new TextMessage("This is your card \n" + startHand);
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
