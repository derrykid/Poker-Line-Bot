package Card;

public class Deal {

    public static Card getCard(Deck deck) throws IllegalAccessException {
        Card card = deck.deal().orElseThrow(IllegalAccessError::new);
        return card;
    }
}
