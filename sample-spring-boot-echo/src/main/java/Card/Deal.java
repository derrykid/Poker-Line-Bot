package Card;

public class Deal {

    public static String getCard(Deck deck) throws IllegalAccessException {
        Card card = deck.deal().orElseThrow(IllegalAccessError::new);
        return card.toString();
    }
}
