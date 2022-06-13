package org.derryclub.linebot.commands.ingame;

import lombok.NoArgsConstructor;
import org.derryclub.linebot.commands.ingame.impl.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Register in-game commands
 */
@NoArgsConstructor
public final class GameCommandRegister {

    private static final List<GameCommand> GAME_COMMANDS = createRegister();

    // game restart command is removed now.
    private static List<GameCommand> createRegister() {
        List<GameCommand> inGameCommands = new ArrayList<>();
        inGameCommands.add(new InGameHelp());
        inGameCommands.add(new InGameSystemCommand());
        inGameCommands.add(new GameStartCommand());
        inGameCommands.add(new FoldCommand());
        inGameCommands.add(new CheckCommand());
        inGameCommands.add(new BetCommand());
        inGameCommands.add(new CallCommand());
        inGameCommands.add(new AllInCommand());
        return Collections.unmodifiableList(inGameCommands);
    }

    public static List<GameCommand> getGameCommands() {
        return GAME_COMMANDS;
    }
}
