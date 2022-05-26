package org.derryclub.linebot.commands.ingame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class GameCommandAdapter implements GameCommand {

    private static final GameStatus STATUS = GameStatus.IN_GAME;
    private final String name;
    private final String description;

    @Override
    public GameStatus getStatus(){
        return STATUS;
    }

}
