package Processor;

import Constant.BotCommand;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class BotCommandProcessor {

    private static Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> commandMap;

    public BotCommandProcessor() {

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
            system.append("gameMap Size:" + GameController.getGameMapSize() + "\n"
                    + "my deck remains: " + GameController.getGameMap().get(event.getSource().getSenderId()).getDeck().size() + "\n"
            );
            return new TextMessage(system.toString());
        });
        commandMap.put(BotCommand.RESTART, (event) -> {

            /*
             * if the game exist, remove it and run command DEAL
             * if not exist, run the command DEAL straight away
             * */
            if (GameController.isExist(event)) {
                // delete the game and restart one
                // remove the reference
                GameController.getGameMap().remove(event.getSource().getSenderId());
                String cardDeal = GameController.deal(event);
                return EmojiProcesser.process(cardDeal);

            } else {
                /*
                 * it's not exist, so create a new one
                 * */

                String cardDeal = GameController.deal(event);
                return EmojiProcesser.process(cardDeal);
            }
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

            String groupID = event.getSource().getSenderId();
            if (GameController.getGameMap().get(groupID) != null ) {
                // game exist
                return new TextMessage("已經有遊戲在進行中了！  （如果沒有可以輸入 /destroy 重建一局）");
            } else {
                /*
                 * use /end command to finish adding people
                 * */
                return new TextMessage("上限8人，要玩的請輸入 '+1'" + "  /end");
            }

        });
        commandMap.put(BotCommand.DEAL,
                (event) -> {

                    // TODO first deal and second time calling deal has different cards

                    // every event sent by user, same groupID will secure it's the same game
                    String cardDeal = GameController.deal(event);

                    // if it's river_state and cards are all dealt, call the poker API

                    return EmojiProcesser.process(cardDeal);
                }
        );
    }

    public static Map<BotCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> getCommandMap() {
        return commandMap;
    }
}
