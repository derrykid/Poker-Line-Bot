package Game;

public class Player {
    private String userID;
    private int position;
    private int chip;
    private StringBuilder holeCards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (position != player.position) return false;
        if (chip != player.chip) return false;
        if (userID != null ? !userID.equals(player.userID) : player.userID != null) return false;
        return holeCards != null ? holeCards.equals(player.holeCards) : player.holeCards == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + position;
        result = 31 * result + chip;
        result = 31 * result + (holeCards != null ? holeCards.hashCode() : 0);
        return result;
    }

    public Player(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChip() {
        return chip;
    }

    public void setChip(int chip) {
        this.chip = chip;
    }

    public StringBuilder getHoleCards() {
        return holeCards;
    }

    public void setHoleCards(StringBuilder holeCards) {
        this.holeCards = holeCards;
    }
}
