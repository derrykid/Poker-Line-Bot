package org.derryclub.linebot.commands.pregame.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.derryclub.linebot.commands.pregame.PregameCommandAdapter;

public final class BugReportCommand extends PregameCommandAdapter {
    public BugReportCommand() {
        super("bug", "回報bug, 請詳述內容");
    }

    @Override
    public Message onSlashCommand(@NonNull MessageEvent<TextMessageContent> event) {
        return new TextMessage("Thanks for your report");
    }
}
