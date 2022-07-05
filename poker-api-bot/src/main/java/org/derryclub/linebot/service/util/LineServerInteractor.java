package org.derryclub.linebot.service.util;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.service.pokergame.gamecontrol.GameControlSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * An utility class which sends request to Line server and get the desired data
 */
@Slf4j
public final class LineServerInteractor {

    private static final String token = "4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=";

    private static final LineMessagingClient client = LineMessagingClient.builder(token).build();

    private static final ThreadManager threadManager = ThreadManager.getManager();
    private static final ScheduledExecutorService scheduledExecutorService = threadManager.getScheduledExecutorService();
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

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

    public static void onUserAllIn(String groupId, List<Card> cards) {

        // the cards that are going to be dealt for community cards
        ArrayList<Card> dealtCards = new ArrayList<>(cards);


        // means it's preflop
        // I use 3 tasks to push all cards
        // FIXME simplify this mess
        if (dealtCards.size() > 3) {

            service.schedule(() -> {
                StringBuilder cardTextMessage = new StringBuilder();

                for (int i = 0; i < 3; i++) {
                    cardTextMessage.append(dealtCards.get(i).toString());
                }
                PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    log.info("Sent: {}", botApiResponse);
                } catch (ExecutionException | InterruptedException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            }, 500, TimeUnit.MILLISECONDS);

            service.schedule(() -> {

                StringBuilder cardTextMessage = new StringBuilder();
                cardTextMessage.append(dealtCards.get(3).toString());
                PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    log.info("Sent: {}", botApiResponse);
                } catch (ExecutionException | InterruptedException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            }, 500, TimeUnit.MILLISECONDS);

            service.schedule(() -> {

                StringBuilder cardTextMessage = new StringBuilder();
                cardTextMessage.append(dealtCards.get(4).toString());
                PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    log.info("Sent: {}", botApiResponse);
                } catch (ExecutionException | InterruptedException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            }, 500, TimeUnit.MILLISECONDS);

            // winner message
            Message msg = GameControlSystem.gameProceed(groupId);
            service.schedule(() -> {
                PushMessage pushMessage = new PushMessage(groupId, msg);
                try {
                    BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                    log.info("Sent: {}", botApiResponse);
                } catch (ExecutionException | InterruptedException e) {
                    log.error("All in error: {}", e.getMessage());
                }
            }, 500, TimeUnit.MILLISECONDS);


        } else {
            int size = dealtCards.size();

            if (size == 2) {
                service.schedule(() -> {

                    StringBuilder cardTextMessage = new StringBuilder();
                    cardTextMessage.append(dealtCards.get(0).toString());
                    PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                    try {
                        BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                        log.info("Sent: {}", botApiResponse);
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("All in error: {}", e.getMessage());
                    }
                }, 500, TimeUnit.MILLISECONDS);
                service.schedule(() -> {

                    StringBuilder cardTextMessage = new StringBuilder();
                    cardTextMessage.append(dealtCards.get(1).toString());
                    PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                    try {
                        BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                        log.info("Sent: {}", botApiResponse);
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("All in error: {}", e.getMessage());
                    }
                }, 500, TimeUnit.MILLISECONDS);

            } else {
                service.schedule(() -> {

                    StringBuilder cardTextMessage = new StringBuilder();
                    cardTextMessage.append(dealtCards.get(0).toString());
                    PushMessage pushMessage = new PushMessage(groupId, EmojiProcessor.process(cardTextMessage.toString()));
                    try {
                        BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                        log.info("Sent: {}", botApiResponse);
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("All in error: {}", e.getMessage());
                    }
                }, 500, TimeUnit.MILLISECONDS);

                // winner message
                Message msg = GameControlSystem.gameProceed(groupId);
                service.schedule(() -> {
                    PushMessage pushMessage = new PushMessage(groupId, msg);
                    try {
                        BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
                        log.info("Sent: {}", botApiResponse);
                    } catch (ExecutionException | InterruptedException e) {
                        log.error("All in error: {}", e.getMessage());
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }


        }

    }

    private static void pollCard(List<?> cards) {
    }


    private static <T> List<List<T>> chopped (List<T> list, int L) {
        List<List<T>> parts = new ArrayList<List<T>>();

        for (int i = 0; i < list.size(); i += L) {
            parts.add(list.subList(i, Math.min(list.size(), i + L)));
        }

        return parts;
    }
}
