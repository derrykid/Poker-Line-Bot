package Processor;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class pushCustomMessage {
    public static void dealHoleCards(String playerID, String holeCards) {

        final LineMessagingClient client = LineMessagingClient
                .builder("N6UpY0AcuaoeOd4g3YYL3DNqXF8tzIGcaXZ4oAWF8Wa+S4tIwhbufl15UCkS+am82kxgM8rBnRyXwgwYhIY1hmu+kh8NCckUZNRImthycZFA7dv5Oljwns8e117Bon2rOfM+uyfe84vSjk+Y7tkBigdB04t89/1O/w1cDnyilFU=")
                .build();

        for (Object player: players){
            final TextMessage textMessage = new TextMessage("Try out push functionality! I don't need bear");
            final PushMessage pushMessage = new com.linecorp.bot.model.PushMessage(userID, textMessage);
        }




        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
