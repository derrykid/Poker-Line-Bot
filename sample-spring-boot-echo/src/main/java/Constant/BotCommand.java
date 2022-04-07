package Constant;

public enum BotCommand {

    HELP("幫助", "/help"),
    START("開局", "/start"),
    SYSTEM("系統數據", "/sys"),
    RESTART("重開局", "/restart");


    private final String description;
    private final String command;

    BotCommand(String description, String command) {
        this.description = description;
        this.command = command;
    }

    public String getDescription() {
        return this.description;
    }

    private String getCommand() {
        return this.command;
    }

    public static BotCommand getBotCommand(String command) {
        if (command == null) {
            throw new NullPointerException("The bot command is null");
        }
        for (BotCommand botCommand : values()) {
            if (botCommand.getCommand().equals(command)) {
                return botCommand;
            }
        }

        return null;
    }


}
