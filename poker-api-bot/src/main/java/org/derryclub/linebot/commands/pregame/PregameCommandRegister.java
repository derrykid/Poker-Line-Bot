package org.derryclub.linebot.commands.pregame;

import lombok.NoArgsConstructor;
import org.derryclub.linebot.commands.pregame.impl.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * register the pregame commands
 * It could only be accessed by static method
 */
@NoArgsConstructor
public final class PregameCommandRegister {

    private static final List<PregameCommand> PREGAME_COMMANDS = createRegister();

    private static List<PregameCommand> createRegister(){
        List<PregameCommand> pregameCommands = new ArrayList<>();

        pregameCommands.add(new StartCommand());
        pregameCommands.add(new PregameSystemCommand());
        pregameCommands.add(new PregameHelp());
        pregameCommands.add(new DrawCardCommand());
        pregameCommands.add(new BugReportCommand());

        return Collections.unmodifiableList(pregameCommands);
    }

    public static List<PregameCommand> getPregameCommands(){
        return PREGAME_COMMANDS;
    }
}
