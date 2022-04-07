package Constant;

import Game.Game;

import java.util.ArrayList;

public enum GameCommand {

    HELP("幫助", "/help"),
    DEAL("發牌", "/deal"),
    // this command should only in dev mode
    RESTART("restart", "/restart"),
    DESTROY("刪除遊戲", "/destroy"),
    SYSTEM("系統數據", "/sys");


    private final String description;
    private final String command;

    GameCommand(String description, String command) {
        this.description = description;
        this.command = command;
    }

    public String getDescription() {
        return this.description;
    }

    private String getCommand() {
        return this.command;
    }

    public static ArrayList<String> getGameCommandList() {
        ArrayList<String> list = new ArrayList<>();
        for (GameCommand per: values()){
            list.add(per.getCommand());
        }
        return list;
    }

    public static GameCommand getGameCommand(String command) {
        if (command == null) {
            throw new NullPointerException("This game command is null");
        }

        for (GameCommand per: values()){
            if (per.getCommand().equals(command)){
                return per;
            }
        }

        return null;
    }


}
