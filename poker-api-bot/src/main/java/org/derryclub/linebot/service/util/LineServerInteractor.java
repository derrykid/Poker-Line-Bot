package org.derryclub.linebot.service.util;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.gameConfig.Game;
import org.derryclub.linebot.poker.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;

/**
 * An utility class which sends request to Line server and get the desired data
 */
@Slf4j
public final class LineServerInteractor {

    private static final String token = "4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=";

    private static final LineMessagingClient client = LineMessagingClient.builder(token).build();

    private static final ThreadManager threadManager = ThreadManager.getManager();
    private static final ScheduledExecutorService scheduledExecutorService = threadManager.getScheduledExecutorService();

    public static void pushHoleCards(String userID, String holeCards) {

        final TextMessage textMessage = EmojiProcessor.process(holeCards);
        final PushMessage pushMessage = new PushMessage(userID, textMessage);

        try {
            BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
            log.info("Push hole cards {}", botApiResponse);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Push hole cards error occur: {}", e.getMessage());
        }
    }


    public static String getUserName(String userId) {

        final UserProfileResponse userProfileResponse;
        try {
            userProfileResponse = client.getProfile(userId).get();
            return userProfileResponse.getDisplayName();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Get user name fails: {}", e.getMessage());
        }
        return null;
    }

    public static void onUserAllIn(String groupId, List<Card> cards, Game.GameStage stage) throws InterruptedException {

        // the cards that are going to be dealt for community cards
        ArrayList<Card> dealtCards = new ArrayList<>(cards);


        // means it's preflop
        if (dealtCards.size() > 3) {
            List<List<Card>> parts = chopped(dealtCards, 3);

            Runnable threeCardDealtRunnable = () -> {
                StringBuilder cardTextMessage = new StringBuilder();
                parts.get(0).forEach(card -> cardTextMessage.append(card.toString()));
                PushMessage push3CardsMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(push3CardsMessage).get();
                    log.info("Sent: {}", botApiResponse);
                    Thread.sleep(500);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            };

            // the [c, c, c] [c, c] part 2, first card
            Runnable secondFirstCardDealtRunnable = () -> {
                StringBuilder cardTextMessage = new StringBuilder();
                Card card = parts.get(1).get(0);
                cardTextMessage.append(card.toString());
                PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    Thread.sleep(500);
                    log.info("Sent: {}", botApiResponse);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            };
            // the [c, c, c] [c, c] part 2, second card
            Runnable secondSecondCardDealtRunnable = () -> {
                StringBuilder cardTextMessage = new StringBuilder();
                Card card = parts.get(1).get(1);
                cardTextMessage.append(card.toString());
                PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    Thread.sleep(500);
                    log.info("Sent: {}", botApiResponse);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            };

            BlockingQueue<Runnable> queue = new ArrayBlockingQueue(1024);
            queue.put(threeCardDealtRunnable);
            queue.put(secondFirstCardDealtRunnable);
            queue.put(secondSecondCardDealtRunnable);

            scheduledExecutorService.execute(queue.take());
            scheduledExecutorService.execute(queue.take());
            scheduledExecutorService.execute(queue.take());
        } else {

            // it can be 2 cards or 1 card
            for (Card per : dealtCards) {
                StringBuilder cardTextMessage = new StringBuilder();
                cardTextMessage.append(per.toString());
                PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    Thread.sleep(500);
                    log.info("Sent: {}", botApiResponse);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            }

        }

    }

    private static <T> List<List<T>> chopped (List<T> list, int L) {
        List<List<T>> parts = new ArrayList<List<T>>();

        for (int i = 0; i < list.size(); i += L) {
            parts.add(list.subList(i, Math.min(list.size(), i + L)));
        }

        return parts;
    }
}
