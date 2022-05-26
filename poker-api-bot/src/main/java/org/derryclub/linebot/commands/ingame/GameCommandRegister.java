package org.derryclub.linebot.commands.ingame;

import org.derryclub.linebot.commands.ingame.impl.Help;

import java.util.ArrayList;
import java.util.List;

public final class GameCommandRegister {

    private static final List<GameCommand> GAME_COMMANDS = new ArrayList<>();

    private GameCommandRegister(){
        GAME_COMMANDS.add(new Help());
    }

    public static List<GameCommand> getGameCommands() {
        return GAME_COMMANDS;
    }
}
