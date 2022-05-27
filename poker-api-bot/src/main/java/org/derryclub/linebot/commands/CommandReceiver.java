package org.derryclub.linebot.commands;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

/**
 * Whenever there's a command invoked by a user, reply with a Message
 */
public interface CommandReceiver {
    Message handle(MessageEvent<TextMessageContent> event);
}
