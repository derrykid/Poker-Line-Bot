package org.derryclub.linebot.commands.pregame;

import org.derryclub.linebot.commands.pregame.impl.Help;
import org.derryclub.linebot.commands.pregame.impl.Start;
import org.derryclub.linebot.commands.pregame.impl.System;

import java.util.ArrayList;
import java.util.List;

/**
 * register the pregame commands
 * It could only be accessed by static method
 */
public final class PregameCommandRegister {

    private static final List<PregameCommand> PREGAME_COMMANDS = new ArrayList<>();

    private PregameCommandRegister() {
        PREGAME_COMMANDS.add(new Help());
        PREGAME_COMMANDS.add(new Start());
        PREGAME_COMMANDS.add(new System());
    }

    public static List<PregameCommand> getPregameCommands(){
        return PREGAME_COMMANDS;
    }
}
