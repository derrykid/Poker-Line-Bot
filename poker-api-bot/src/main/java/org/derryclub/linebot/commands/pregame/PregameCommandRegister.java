package org.derryclub.linebot.commands.pregame;

import org.derryclub.linebot.commands.pregame.impl.Help;
import org.derryclub.linebot.commands.pregame.impl.Start;
import org.derryclub.linebot.commands.pregame.impl.System;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * register the pregame commands
 * It could only be accessed by static method
 */
public final class PregameCommandRegister {

    private static final List<PregameCommand> PREGAME_COMMANDS = createRegister();

    private static List<PregameCommand> createRegister(){
        List<PregameCommand> pregameCommands = new ArrayList<>();
        pregameCommands.add(new Start());
        pregameCommands.add(new System());
        pregameCommands.add(new Help());
        return Collections.unmodifiableList(pregameCommands);
    }

    public static List<PregameCommand> getPregameCommands(){
        return PREGAME_COMMANDS;
    }
}
