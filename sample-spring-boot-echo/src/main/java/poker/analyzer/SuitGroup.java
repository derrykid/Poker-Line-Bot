package poker.analyzer;

import card.*;

import java.util.*;
import java.util.stream.Collectors;

public class SuitGroup implements Iterable<Map.Entry<Suit, List<Card>>> {

    private final Map<Suit, List<Card>> suitMap;

    public SuitGroup(final SortedSet<Card> cards) {
        this.suitMap = initSuitGroup(cards);
    }

    public Map<Suit, List<Card>> getSuitMap() {
        return this.suitMap;
    }

    private static Map<Suit, List<Card>> initSuitGroup(SortedSet<Card> cards) {
        return Collections.unmodifiableMap(new TreeMap<>(cards.stream().collect(Collectors.groupingBy(Card::getSuit))));
    }

    @Override
    public Iterator<Map.Entry<Suit, List<Card>>> iterator() {
        return this.suitMap.entrySet().iterator();
    }
}
