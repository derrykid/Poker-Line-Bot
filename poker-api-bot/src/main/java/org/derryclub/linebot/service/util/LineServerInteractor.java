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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An utility class which sends request to Line server and get the desired data
 */
@Slf4j
public final class LineServerInteractor {

    private static LineServerInteractor instance;

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

    public static void onUserAllIn(String groupId, List<Card> cards, Game.GameStage stage) {

        ArrayList<Card> dealtCards = new ArrayList<>(cards);

        StringBuilder cardBuilder = new StringBuilder();
        ArrayList<Card> flopCards = new ArrayList<>();

        switch (stage) {
            // means 5 cards
            case GAME_PREFLOP:
                for (int i = 0; i < 3; i++) {
                    Card card = dealtCards.get(i);
                    flopCards.add(card);
                    cardBuilder.append(card.toString());
                }
                PushMessage push3CardsMessage = new PushMessage(groupId, EmojiProcessor.process(cardBuilder.toString()));
                try {
                    BotApiResponse botApiResponse = client.pushMessage(push3CardsMessage).get();
                    log.info("Sent: {}", botApiResponse);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("All in error: {}", e.getMessage());
                }
                dealtCards.removeAll(flopCards);
                for (Card per : dealtCards) {

                    scheduledExecutorService.scheduleAtFixedRate(
                    () -> {
                        String remainCard = per.toString();
                        PushMessage pushRemainCard = new PushMessage(groupId, EmojiProcessor.process(remainCard));
                        try {
                            BotApiResponse botApiResponse = client.pushMessage(pushRemainCard).get();
                            log.info("Sent: {}", botApiResponse);
                        } catch (InterruptedException | ExecutionException e) {
                            log.error("Push individual card error with time delay");
                            throw new RuntimeException(e);
                        }
                    }, 0, 500, TimeUnit.MILLISECONDS);

                }
                break;

            case GAME_FLOP:
            case GAME_TURN_STATE:

                for (Card per : dealtCards) {

                    scheduledExecutorService.scheduleAtFixedRate(
                            () -> {
                                String remainCard = per.toString();
                                PushMessage pushRemainCard = new PushMessage(groupId, EmojiProcessor.process(remainCard));
                                try {
                                    BotApiResponse botApiResponse = client.pushMessage(pushRemainCard).get();
                                    log.info("Sent: {}", botApiResponse);
                                } catch (InterruptedException | ExecutionException e) {
                                    log.error("Push individual card error with time delay");
                                    throw new RuntimeException(e);
                                }
                            }, 0, 500, TimeUnit.MILLISECONDS);

                }
                break;

            default:
                log.debug("Should not reach here in LineServerInteractor#onUserAllIn");
        }


        PushMessage message = new PushMessage(groupId, new TextMessage("Hi"));

        try {
            BotApiResponse botApiResponse = client.pushMessage(message).get();
            log.info("Sent: {}", botApiResponse);
        } catch (InterruptedException | ExecutionException e) {
            log.error("All in error: {}", e.getMessage());
        }
    }
}
