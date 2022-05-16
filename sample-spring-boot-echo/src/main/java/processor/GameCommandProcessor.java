package processor;

import constant.GameCommand;
import Game.Game;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class GameCommandProcessor {

    private static Map<GameCommand, FunctionThrowable<MessageEvent<TextMessageContent>, Message>> gameCommandMap;

    public GameCommandProcessor() {
        init();
    }

    public static Message handle(MessageEvent<TextMessageContent> event) {
        // TODO handle game commands
        final String command = event.getMessage().getText().split(" ")[0];
        GameCommand gameCommand = GameCommand.getGameCommand(command);

        if (gameCommand == null) {
            return null;
        }

        FunctionThrowable<MessageEvent<TextMessageContent>, Message> action = gameCommandMap.get(gameCommand);

        try {
            return action.apply(event);
        } catch (Exception ex) {
            return new TextMessage("Unhandled commands");
        }
    }

    @PostConstruct
    private static void init() {
        gameCommandMap = Collections.synchronizedMap(new EnumMap<>(GameCommand.class));

        gameCommandMap.put(GameCommand.HELP, (event) -> {
            StringBuilder text = new StringBuilder();
            for (GameCommand per : EnumSet.allOf(GameCommand.class)) {
                text.append('/').append(per.toString().toLowerCase()).append(": ").append(per.getDescription()).append("\n");
            }
            return new TextMessage(text.toString());
        });

        gameCommandMap.put(GameCommand.SYSTEM, (event) -> {
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

        gameCommandMap.put(GameCommand.RESTART, (event) -> {
            // TODO restart the match
            return new TextMessage("Game restart");
        });

        gameCommandMap.put(GameCommand.DESTROY, (event) -> {
            Game removedGame = GameController.getGameMap().remove(event.getSource().getSenderId());
            if (removedGame != null) {
                return new TextMessage("Game deleted");
            }
            return new TextMessage("Game doesn't exist, you cannot delete the unexist game");
        });
    }
}
