package card;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Deal {

    public static Card getCard(Deck deck) {
        return deck.deal().orElseThrow(IllegalAccessError::new);
    }
}
