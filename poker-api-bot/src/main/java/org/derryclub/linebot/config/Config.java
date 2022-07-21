package org.derryclub.linebot.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    private LineBotConfig lineBotConfig;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    private Config(@JsonProperty("line.bot") LineBotConfig lineBotConfig) {
        this.lineBotConfig = lineBotConfig;
    }

    public LineBotConfig getLineBotConfig() {
        return lineBotConfig;
    }
}
