package org.derryclub.linebot.commands.pregame;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Arrays.stream;
//    RESTART("重開局", "/restart");

@AllArgsConstructor
@Getter
public abstract class PregameCommandAdapter implements PregameCommand {

    private static final GameStatus STATUS = GameStatus.PRE_GAME;
    private final String name;
    private final String description;

    @Override
    public GameStatus getStatus() {
        return STATUS;
    }

}
