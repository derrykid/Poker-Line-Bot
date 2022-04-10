package Processor;

import Constant.BotCommand;
import Game.*;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class BotCommandProcessor {


    private static Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> commandMap;

    public BotCommandProcessor() {
        init();
    }

    public static Message handle(MessageEvent<TextMessageContent> event) {
        final String command = event.getMessage().getText().split(" ")[0];
        BotCommand botCommand = BotCommand.getBotCommand(command);

        if (botCommand == null) {
            return null;
        }

        FunctionThrowable<MessageEvent<TextMessageContent>, Message> action = commandMap.get(botCommand);

        try {
            return action.apply(event);
        } catch (Exception ex) {
            return new TextMessage("Unhandled commands");
        }
    }

    @PostConstruct
    private static void init() {

        commandMap = Collections.synchronizedMap(new EnumMap<>(BotCommand.class));

        commandMap.put(BotCommand.HELP, (event) -> {
                    StringBuilder text = new StringBuilder();
                    for (BotCommand per : EnumSet.allOf(BotCommand.class)) {
                        text.append('/').append(per.toString().toLowerCase()).append(": ").append(per.getDescription()).append("\n");
                    }
                    return new TextMessage(text.toString());
                }
        );
        commandMap.put(BotCommand.SYSTEM, (event) -> {
            String msg;

            msg = "gameMap Size:" + GameController.getOngoingGame() + "\n"
                    + "cards in the deck remains: " + GameController.getGameMap().get(event.getSource().getSenderId()).getDeck().size() + "\n"
                    + "The player numbers: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()).size() + "\n"
                    + "The player: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()) + "\n"
                    + "Group ID" + event.getSource().getSenderId() + "\n"
                    + "User ID" + event.getSource().getUserId() + "\n"
            ;

            return new TextMessage(msg);
        });
        commandMap.put(BotCommand.START, (event) -> {
            /*
             * If groupID & userID are equal, it's private chat.
             * */
            Source source = event.getSource();
            String groupID = source.getSenderId();
            String userID = source.getUserId();

            if (groupID.equals(userID)) {
                return new TextMessage("一個人不能玩！ 請到群組聊天室開始遊戲!");
            }

            /*
             * user participating until /end
             * */
            GameController.create(groupID);
            GameController.getPlayersInTheGroup(groupID).add(new Player(userID, LineAPIClient.getUserName(userID)));
            return new TextMessage("上限8人，要玩的請輸入 '+1' \n" + "輸入 '/end' 停止增加玩家");
        });

    }

    public static Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> getCommandMap() {
        return commandMap;
    }
}
