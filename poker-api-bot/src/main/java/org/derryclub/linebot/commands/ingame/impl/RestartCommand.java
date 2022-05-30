package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gameinstances.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playerinstances.PlayerManagerImpl;

/**
 *  create a new game, gc will collect the previous one
 */
public final class RestartCommand extends GameCommandAdapter {

    public RestartCommand() {
        super("restart", "Restart this game");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        Source source = event.getSource();
        String groupId = source.getSenderId();
        String userId = source.getUserId();
        if (groupId.equals(userId)) {
            return new TextMessage("一個人不能玩！ 請到群組聊天室開始遊戲!");
        }

        GameManagerImpl.getManager().createGame(groupId);
        PlayerManagerImpl.getManager().addPlayer(groupId, userId);

        return new TextMessage("上限8人，要玩的請輸入 '+1' \n" + "輸入 '/end' 停止增加玩家");
    }
}
