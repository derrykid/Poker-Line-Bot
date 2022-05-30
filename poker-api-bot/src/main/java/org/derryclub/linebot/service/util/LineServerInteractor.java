package org.derryclub.linebot.service.util;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.service.util.EmojiProcesser;

import java.util.concurrent.ExecutionException;

/**
 *  An utility class which sends request to Line server and get the desired data
 */
@Slf4j
public final class LineServerInteractor {

    private static final String token = "4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=";

    private static final LineMessagingClient client = LineMessagingClient.builder(token).build();

    public static void pushHoleCards(String userID, String holeCards) {

            final TextMessage textMessage = EmojiProcesser.process(holeCards);
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
}
