package Constant;

public enum Constant {

    POKER_API("https://api.pokerapi.dev/v1/winner/texas_holdem?cc=");


    private final String uri;

    Constant(String uri) {
        this.uri = uri;
    }

    public String getUri(){
        return this.uri;
    }

}
