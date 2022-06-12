package org.derryclub.linebot.service.pokergame.gamemanage;

import org.derryclub.linebot.poker.card.Card;
import org.derryclub.linebot.service.pokergame.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommunityCardManager implements Manager {

    private static CommunityCardManager instance;
    private final Map<String, List<Card>> communityCards;

    private CommunityCardManager() {
        communityCards = new HashMap<>();
    }

    public static CommunityCardManager getManager() {
        if (instance == null) {
            instance = new CommunityCardManager();
        }
        return instance;
    }

    public Map<String, List<Card>> getCommunityCardsMap() {
        return this.communityCards;
    }

    public void clearCommunityCard(String groupId) {
        communityCards.get(groupId).clear();
    }
}
