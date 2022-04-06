package Processor;

import poker.Deck;

import java.util.HashMap;

/*
* This processor does 2 jobs
* 1. Save the dealt card from the deck: a. in players' hand and on the board
* 2. when the game finishes, it sends the cards to Poker API, and get the winner
* */
public class DealtCardProcessor {
    private Deck deck;
    /*
    * Every card dealt is saved in this StringBuilder.
    * the 1st, 2nd cards = player1
    * the 3rd, 4th cards = player2
    * the 5th, 6th cards = player3
    * and so on
    * the board cards are appended
    * */
    private StringBuilder dealtCards;

    public DealtCardProcessor(Deck deck) {
        this.deck = deck;
        this.dealtCards = new StringBuilder();
    }

    public StringBuilder getDealtCards(){
        return this.dealtCards;
    }

    public  StringBuilder append(StringBuilder dealtCards) {
        return this.dealtCards.append(dealtCards);
    }
}
