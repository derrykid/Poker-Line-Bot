package com.example.bot.spring.echo;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.response.BotApiResponse;
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
import poker.Deck;
import poker.FivePokerHand;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@LineMessageHandler
public class LineMessageAPI {
    private final Logger log = LoggerFactory.getLogger(LineMessageAPI.class);
    private Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> map;


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

//    @EventMapping
//    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
//        // write event to log
//        log.info("event: " + event);
//
//        // echo bot
////        final String originalMessageText = event.getMessage().getText();
////        return new TextMessage(originalMessageText);
//
//        try {
//            final String command = event.getMessage().getText().split(" ")[0];
//            BotCommand botCommand = BotCommand.getBotCommand(command);
//
//            if (botCommand == null) {
//                //handleMismatchEvent(event);
//                return null;
//            }
//            FunctionThrowable<MessageEvent<TextMessageContent>, Message> action = map.get(botCommand);
//
//            // previous code
//            // return action.apply(event);
//
//            // emoji builder test
////            TextMessageContent.Emoji emo = TextMessageContent.Emoji.builder().productId("5ac21c4e031a6752fb806d5b").emojiId("009").build();
//
//
//            return null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new TextMessage("Handle text message error occurs");
//        }
//    }

    @EventMapping
    public BotApiResponse handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        final LineMessagingClient client = LineMessagingClient
                .builder("4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=")
                .build();

        final TextMessage textMessage = new TextMessage("hello");
        final ReplyMessage replyMessage = new ReplyMessage(
                event.getReplyToken(),
                textMessage);

        BotApiResponse botApiResponse = null;
        try {
            botApiResponse = client.replyMessage(replyMessage).get();
            return botApiResponse;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("This is bot API response: " + botApiResponse);
        return botApiResponse;
    }


    private void handleMismatchEvent(MessageEvent<TextMessageContent> event) {
        // TODO handle the mismatch event
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    public static void main(String[] args) {
        SpringApplication.run(LineMessageAPI.class, args);
    }
}
