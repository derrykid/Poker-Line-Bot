package Processor;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import java.util.concurrent.ExecutionException;

public class LineAPIClient {
    private final static LineMessagingClient client = LineMessagingClient .builder("4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=").build();
    public static void pushHoleCards(String userID, String holeCards) {



            final TextMessage textMessage = EmojiProcesser.process(holeCards);
            final PushMessage pushMessage = new PushMessage(userID, textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Push message error occurs");
            return;
        }
    }

    public static String getUserName(String userID) {
        final LineMessagingClient client = LineMessagingClient
                .builder("N6UpY0AcuaoeOd4g3YYL3DNqXF8tzIGcaXZ4oAWF8Wa+S4tIwhbufl15UCkS+am82kxgM8rBnRyXwgwYhIY1hmu+kh8NCckUZNRImthycZFA7dv5Oljwns8e117Bon2rOfM+uyfe84vSjk+Y7tkBigdB04t89/1O/w1cDnyilFU=")
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
