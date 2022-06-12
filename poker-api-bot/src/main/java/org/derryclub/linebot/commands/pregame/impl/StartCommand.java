package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;

public final class StartCommand extends PregameCommandAdapter {

    public StartCommand() {
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
        String groupId = source.getSenderId();
        String userId = source.getUserId();
        if (groupId.equals(userId)) {
            return new TextMessage("一個人不能玩！ 請到群組聊天室開始遊戲!");
        }

        GameManagerImpl.getManager().createGame(groupId);
        PlayerManagerImpl.getManager().createPlayer(groupId, userId);

        return new TextMessage("上限8人，要玩的請輸入 '+1' \n" + "輸入 '/end' 開始遊戲。請一定要先加我好友才能發牌給你！");
    }
}
