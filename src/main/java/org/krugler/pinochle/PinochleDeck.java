package org.krugler.pinochle;

import java.util.Random;

public class PinochleDeck {
    public static int CARDS_IN_DECK = Rank.NUM_RANKS * Suit.NUM_SUITS * 2;
    public static int UNIQUE_CARDS_IN_DECK = Rank.NUM_RANKS * Suit.NUM_SUITS;
    
    private int[] _deck;
    private Random _random;
    
    public PinochleDeck(long randSeed) {
        _deck = new int[CARDS_IN_DECK];
        
        int i = 0;
        for (int suit : Suit.SUITS) {
            for (int rank : Rank.RANKS) {
                int card = Card.makeCard(rank, suit);
                _deck[i++] = card;
                _deck[i++] = card;
            }
        }
        
        _random = new Random(randSeed);
    }
    
    public void shuffle() {
        // Durstenfeld version of a Fisher–Yates shuffle
        for (int i = _deck.length - 1; i > 0; i--) {
            int index = _random.nextInt(i + 1);
            // Simple swap
            int a = _deck[index];
            _deck[index] = _deck[i];
            _deck[i] = a;
        }
    }

    /**
     * After a shuffle, used to allocate cards to players (the deal).
     * 
     * @param player
     * @param i
     * @return card at indicated position in deck for requested player & card index.
     */
    public int getCard(int player, int i) {
        if ((player < 0) || (player >= PinochleGame.NUM_PLAYERS)) {
            throw new IllegalArgumentException("Invalid player index: " + player);
        }
        
        if ((i < 0) || (i >= PinochleHand.CARDS_IN_HAND)) {
            throw new IllegalArgumentException("Invalid card index: " + i);
        }
        
        return _deck[(player * PinochleHand.CARDS_IN_HAND) + i];
    }
      
    
}
