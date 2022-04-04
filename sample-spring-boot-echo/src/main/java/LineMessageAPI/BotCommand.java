package LineMessageAPI;

public enum BotCommand {

    HELP("幫助", "/help"),
    DEAL("發牌", "/deal"),
    DESTROY("刪除遊戲", "/destroy"),
    RESTART("重開局", "/rematch");


    private final String description;
    private final String command;

    private BotCommand(String description, String command) {
        this.description = description;
        this.command = command;
    }

    private String getDescription() {
        return this.description;
    }

    private String getCommand() {
        return this.command;
    }

    public static BotCommand getBotCommand(String command) {
        if (command == null) {
            throw new NullPointerException("The command is null");
        }
        for (BotCommand botCommand : values()) {
            if (botCommand.getCommand().equals(command)) {
                return botCommand;
            }
        }

        return null;
    }


}
