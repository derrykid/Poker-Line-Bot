package processor;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import java.util.concurrent.ExecutionException;

public class LineAPIClient {

    private static String token ="4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=";

    private static final LineMessagingClient client = LineMessagingClient .builder(token).build();
    public static void pushHoleCards(String userID, String holeCards) {



            final TextMessage textMessage = EmojiProcesser.process(holeCards);
            final PushMessage pushMessage = new PushMessage(userID, textMessage);

        try {
            BotApiResponse botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Push message error occurs");
            return;
        }
    }

    public static String getUserName(String userID) {
        final LineMessagingClient client = LineMessagingClient
                .builder(token)
                .build();

        final UserProfileResponse userProfileResponse;
        try {
            userProfileResponse = client.getProfile(userID).get();
            return userProfileResponse.getDisplayName();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "LineAPIClient.getUserName()- Can't get user Name, please ensure you add the Message Bot!";
        }
    }
}
