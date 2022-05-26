package org.derryclub.linebot.commands.ingame;

import org.derryclub.linebot.commands.Command;

public interface GameCommand extends Command {
    String getName();

    String getDescription();
}
