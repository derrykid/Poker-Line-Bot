package Processor;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import java.util.concurrent.ExecutionException;

public class pushCustomMessage {
    public static void pushHoleCards(String userID, String holeCards) {

        final LineMessagingClient client = LineMessagingClient
                .builder("4lHdgSOo/+RQsdLDmS3R99+HclBAXUAVFcCcgfF9FIrDzNiVWyOkfho59nsDahfnrfnkLPeVjDUkLB5Q9nj6A8WVgxMZ3DGRtsRO+hqZO6qoXzLcIKWBKvJhxkPc3Y1ok9etjDBGn7Hm1gmSEthktgdB04t89/1O/w1cDnyilFU=")
                .build();

            final TextMessage textMessage = EmojiProcesser.process(holeCards);
            final TextMessage test = new TextMessage("Test" + "Ua417bc25582edd9fec30f8b179edfab1");
            final PushMessage pushMessage = new PushMessage("Ua417bc25582edd9fec30f8b179edfab1", test);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Push message error occurs");
        }
    }
}
