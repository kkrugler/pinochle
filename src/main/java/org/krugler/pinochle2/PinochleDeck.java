package org.krugler.pinochle2;

import java.util.Arrays;
import java.util.Random;

public class PinochleDeck {
    private int _cards[];
    private Random _rand;
    
    public PinochleDeck() {
        _cards = new int[48];
        _rand = new Random(666L);
    }
    
    public void shuffle() {
        int index = 0;
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 6; rank++) {
                int card = (suit << 8) | rank;
                _cards[index++] = card;
                _cards[index++] = card;
            }
        }
        
        for (int i = _cards.length - 1; i > 0; i--) {
            index = _rand.nextInt(i + 1);
            // Simple swap
            int a = _cards[index];
            _cards[index] = _cards[i];
            _cards[i] = a;
        }
        
        // For each player, sort by suit and then rank
        for (int player = 0; player < 4; player++) {
            int firstCard = player * 12;
            Arrays.sort(_cards, firstCard, firstCard + 12);
        }
    }
    
    public int calcMeld(int player, int trump) {
        int firstCard = player * 12;
        
        // Check for various meld
        for (int i = 0; i < 12; i++) {
            int cardIndex = (player * 12) + i;
            int card = _cards[cardIndex];
            
            
        }
    }
}
