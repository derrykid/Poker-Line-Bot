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

    private final List<PregameCommand> pregameCommands = new ArrayList<>();

    PregameCommandRegister() {
        pregameCommands.add(new Help());
        pregameCommands.add(new Start());
        pregameCommands.add(new System());
    }

    public List<PregameCommand> getPregameCommands(){
        return pregameCommands;
    }
}
