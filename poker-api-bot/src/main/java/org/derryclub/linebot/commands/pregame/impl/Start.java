package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.game.Player;
import org.derryclub.linebot.processor.GameController;
import org.derryclub.linebot.processor.LineAPIClient;

public final class Start extends PregameCommandAdapter {
    public Start() {
        super("start", "Start a poker game");
    }

    /**
     * Start a new game by invoking '/start'
     * A game can have up to 8 players
     * @return Message of asking players to participate
     */
    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {

        // If groupID & userID are equal, it's a personal chat
        Source source = event.getSource();
        if (source.getSenderId().equalsIgnoreCase(source.getUserId())) {
            return new TextMessage("一個人不能玩！ 請到群組聊天室開始遊戲!");
        }

        String groupID = source.getSenderId();
        String userID = source.getUserId();
        // todo create a game, refactor this part
        GameController.create(groupID);
        GameController.getPlayersInTheGroup(groupID).add(new Player(userID, LineAPIClient.getUserName(userID)));
        return new TextMessage("上限8人，要玩的請輸入 '+1' \n" + "輸入 '/end' 停止增加玩家");
    }
}
