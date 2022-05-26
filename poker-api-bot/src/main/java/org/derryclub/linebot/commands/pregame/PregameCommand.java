package org.derryclub.linebot.commands.pregame;

import org.derryclub.linebot.commands.Command;

public interface PregameCommand extends Command {
    String getName();
    String getDescription();
}
