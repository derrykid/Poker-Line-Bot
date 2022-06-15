package org.derryclub.linebot.commands.ingame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.ingame.GameCommandAdapter;
import org.derryclub.linebot.service.pokergame.gamemanage.GameManagerImpl;
import org.derryclub.linebot.service.pokergame.playermanage.PlayerManagerImpl;

/**
 * create a new game, gc will collect the previous one
 */
public final class CardRankingCommand extends GameCommandAdapter {

    private static final String CARD_RANKING = "1. 同花順 " + "\n" +
            "2. 鐵支, 四條 (相同數字的四張牌)" + "\n" +
            "3. 葫蘆 （三條配一個對子）" + "\n" +
            "4. 同花 （同花色，不按順序的五張牌，如黑桃A, 5, 7, 9, J）" + "\n" +
            "5. 順子" + "\n" +
            "6. 三條" + "\n" +
            "7. 兩對" + "\n" +
            "8.一對" + "\n" +
            "9.高牌 （未組成上述任何一種的牌型）";

    public CardRankingCommand() {
        super("牌型大小", "列出牌型大小");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {

        return new TextMessage(CARD_RANKING);
    }
}
