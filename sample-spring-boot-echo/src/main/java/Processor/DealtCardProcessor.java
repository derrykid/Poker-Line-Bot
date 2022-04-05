package Processor;

import poker.Deck;

/*
* This processor do 2 jobs
* 1. Save the dealt card from the deck: a. in players' hand and on the board
* 2. when the game finishes, it sends the cards to Poker API, and get the winner
* */
public class DealtCardProcessor {
    private Deck deck;
    private StringBuilder dealtCards;
}
