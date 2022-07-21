package org.derryclub.linebot.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("line.bot")
public class LineBotConfig {

    private String channelToken;
    private String channelSecret;
    private String handlerPath;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    private LineBotConfig(
            @JsonProperty("channel-token") String channelToken,
            @JsonProperty("channel-secret") String channelSecret,
            @JsonProperty("handler.path") String path) {
        this.channelSecret = channelSecret;
        this.channelToken = channelToken;
        this.handlerPath = path;
    }

    public String getChannelToken() {
        return channelToken;
    }

    public String getChannelSecret() {
        return channelSecret;
    }

}
