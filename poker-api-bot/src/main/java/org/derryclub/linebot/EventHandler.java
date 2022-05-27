package org.derryclub.linebot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

public interface EventHandler {
    Message handleEvent(MessageEvent<TextMessageContent> event);
}
