package Processor;

import Constant.BotCommand;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
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

        commandMap.put(BotCommand.HELP,
                (event) -> {
                    StringBuilder text = new StringBuilder();

                    for (BotCommand per : EnumSet.allOf(BotCommand.class)) {
                        text.append(per.toString().toLowerCase() + " \n");
                    }

                    return new TextMessage(text.toString());
                }
        );
        commandMap.put(BotCommand.DEBUG, (event) -> {
            StringBuilder system = new StringBuilder();
            system.append("gameMap Size:" + GameController.getOngoingGame() + "\n"
                    + "my deck remains: " + GameController.getGameMap().get(event.getSource().getSenderId()).getDeck().size() + "\n"
                    + "The player numbers: " + GameController.getPlayersInTheGroup(event.getSource().getSenderId()) + "\n"
            );
            return new TextMessage(system.toString());
        });
        commandMap.put(BotCommand.RESTART, (event) -> {
            // TODO restart the match
            return new TextMessage("Game restart");
        });

        commandMap.put(BotCommand.DESTROY, (event) -> {
            GameController.getGameMap().remove(event.getSource().getSenderId());
            return new TextMessage("Game deleted");
        });
        commandMap.put(BotCommand.START, (event) -> {
            /*
             * check if there's an existing game
             * if not, create a new game
             * if exist, says there's a game going
             *
             * */

            if (GameController.isGameExist(event)) {
                // game exist
                return new TextMessage("已經有遊戲在進行中了！  （如果沒有可以輸入 /destroy 重建一局）");
            } else {
                /*
                 * user participating until /end
                 * */
                GameController.create(event.getSource().getSenderId());
                // TODO the create game user should add to the set
                return new TextMessage("上限8人，要玩的請輸入 '+1' \n" + "輸入 '/end' 停止增加玩家");
            }

        });
        commandMap.put(BotCommand.DEAL,
                (event) -> new TextMessage("Deal commands awaits refactor")
        );

    }

    public static Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> getCommandMap() {
        return commandMap;
    }
}
