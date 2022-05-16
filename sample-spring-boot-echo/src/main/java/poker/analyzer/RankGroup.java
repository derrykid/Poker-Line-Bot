package poker.analyzer;

import card.*;

import java.util.*;
import java.util.stream.Collectors;

public class RankGroup implements Iterable<Map.Entry<Rank, List<Card>>> {

    private final Map<Rank, List<Card>> rankMap;
    private final int quadCount;
    private final int setCount;
    private final int pairCount;

    public RankGroup(final SortedSet<Card> cards) {
        this.rankMap = initRankGroup(cards);
        this.quadCount = groupCount(4); // if there is one, there's a quad
        this.setCount = groupCount(3); // if there is one, there's a three of a kind
        this.pairCount = groupCount(2); // one - one pair, two - two pairs
    }

    public Map<Rank,List<Card>> getRankMap(){
        return this.rankMap;
    }

    int getQuadCount() {
        return this.quadCount;
    }
    int getSetCount(){
        return this.setCount;
    }
    int getPairCount() {
        return this.pairCount;
    }

    private int groupCount(int groupSize) {
        return Math.toIntExact(this.rankMap.values().stream().filter(n -> n.size() == groupSize).count());
    }

    private static Map<Rank, List<Card>> initRankGroup(SortedSet<Card> cards) {

        Comparator<Map.Entry<Rank, List<Card>>> valueComparator =
                (o1, o2) -> o2.getValue().size() == o1.getValue().size() ? o2.getKey().getRankValue() - o1.getKey().getRankValue() : o2.getValue().size() - o1.getValue().size();

        List<Map.Entry<Rank, List<Card>>> listOfEntries = new ArrayList<>(cards.stream().collect(Collectors.groupingBy(Card::getRank)).entrySet());

        listOfEntries.sort(valueComparator);

        Map<Rank, List<Card>> sortedResults = new LinkedHashMap<>();

        for (Map.Entry<Rank, List<Card>> per: listOfEntries) {
            sortedResults.put(per.getKey(), per.getValue());
        }
        return Collections.unmodifiableMap(sortedResults);
    }

    @Override
    public Iterator<Map.Entry<Rank, List<Card>>> iterator() {
        return this.rankMap.entrySet().iterator();
    }
}
