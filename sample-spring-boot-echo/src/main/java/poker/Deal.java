package poker;

public class Deal {


    public static String getStartHand(Deck deck) throws IllegalAccessException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getCard(deck));
        stringBuilder.append(getCard(deck));

        return stringBuilder.toString();

    }

    public static String getCard(Deck deck) throws IllegalAccessException {
        Card card = deck.deal().orElseThrow(IllegalAccessError::new);
        return card.toString();
    }
}
