package org.derryclub.linebot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.derryclub.linebot.commands.ingame.GameCommandReceiver;
import org.derryclub.linebot.commands.pregame.PregameCommandReceiver;
import org.derryclub.linebot.gameConfig.player.Player;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;
import org.derryclub.linebot.service.util.Threads;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * This class handles all incoming events
 * <br/>
 * Dispense command to the responsive handler
 */
@LineMessageHandler
@Slf4j
@RestController
public class LineMessageAPI implements EventHandler {

    private final PregameCommandReceiver pregameCommandReceiver;
    private final GameCommandReceiver gameCommandReceiver;
    private final ExecutorService executor = Threads.getExecutor();

    private LineMessageAPI() {
        pregameCommandReceiver = PregameCommandReceiver.getInstance();
        gameCommandReceiver = GameCommandReceiver.getInstance();
    }

    @EventMapping
    public Message handleEvent(MessageEvent<TextMessageContent> event) {

        log.info("Coming event: {}", event);

        // Adding player is a special event, in which user can simply '+1' to enroll
        // also every msg user sends, it always replies with sth
        if (GameManagerImpl.getManager().isAddingPlayerStage(event)) {

            String isEndCommand = event.getMessage().getText().split(" ")[0]
                    .substring(1).toLowerCase();
            if (!isEndCommand.equalsIgnoreCase("end")) {
                if (PlayerManagerImpl.getManager().plusOneCommandAddPlayer(event)) {
                    String participant = PlayerManagerImpl.getManager().getPlayers(event.getSource().getSenderId())
                            .stream().map(Player::getUserName).reduce("", (a, b) -> a + b + "\n");
                    return new TextMessage("Welcome! 目前參與玩家：" + "\n" + participant);
                }
                return null;
            }
        }

        // If user text doesn't start with "/", it's not a command
        if (!event.getMessage().getText().startsWith("/")) {
            return null;
        }

        return GameManagerImpl.getManager().isGameExist(event)
                ? gameCommandReceiver.handle(event)
                : pregameCommandReceiver.handle(event);
    }

}
