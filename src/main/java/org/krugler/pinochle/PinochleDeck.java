package org.krugler.pinochle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PinochleDeck {
    public static int CARDS_IN_DECK = 48;
    
    private ArrayList<Card> _deck;
    
    public PinochleDeck() {
        _deck = new ArrayList<Card>(CARDS_IN_DECK);
        
        _deck.add(Card.ACE_SPADES);
        _deck.add(Card.ACE_SPADES);
        _deck.add(Card.TEN_SPADES);
        _deck.add(Card.TEN_SPADES);
        _deck.add(Card.KING_SPADES);
        _deck.add(Card.KING_SPADES);
        _deck.add(Card.QUEEN_SPADES);
        _deck.add(Card.QUEEN_SPADES);
        _deck.add(Card.JACK_SPADES);
        _deck.add(Card.JACK_SPADES);
        _deck.add(Card.NINE_SPADES);
        _deck.add(Card.NINE_SPADES);

        _deck.add(Card.ACE_HEARTS);
        _deck.add(Card.ACE_HEARTS);
        _deck.add(Card.TEN_HEARTS);
        _deck.add(Card.TEN_HEARTS);
        _deck.add(Card.KING_HEARTS);
        _deck.add(Card.KING_HEARTS);
        _deck.add(Card.QUEEN_HEARTS);
        _deck.add(Card.QUEEN_HEARTS);
        _deck.add(Card.JACK_HEARTS);
        _deck.add(Card.JACK_HEARTS);
        _deck.add(Card.NINE_HEARTS);
        _deck.add(Card.NINE_HEARTS);

        _deck.add(Card.ACE_DIAMONDS);
        _deck.add(Card.ACE_DIAMONDS);
        _deck.add(Card.TEN_DIAMONDS);
        _deck.add(Card.TEN_DIAMONDS);
        _deck.add(Card.KING_DIAMONDS);
        _deck.add(Card.KING_DIAMONDS);
        _deck.add(Card.QUEEN_DIAMONDS);
        _deck.add(Card.QUEEN_DIAMONDS);
        _deck.add(Card.JACK_DIAMONDS);
        _deck.add(Card.JACK_DIAMONDS);
        _deck.add(Card.NINE_DIAMONDS);
        _deck.add(Card.NINE_DIAMONDS);

        _deck.add(Card.ACE_CLUBS);
        _deck.add(Card.ACE_CLUBS);
        _deck.add(Card.TEN_CLUBS);
        _deck.add(Card.TEN_CLUBS);
        _deck.add(Card.KING_CLUBS);
        _deck.add(Card.KING_CLUBS);
        _deck.add(Card.QUEEN_CLUBS);
        _deck.add(Card.QUEEN_CLUBS);
        _deck.add(Card.JACK_CLUBS);
        _deck.add(Card.JACK_CLUBS);
        _deck.add(Card.NINE_CLUBS);
        _deck.add(Card.NINE_CLUBS);
    }
    
    public void shuffle() {
        // 3 seconds for 1M shuffles of full deck with -server JVM option
        Collections.shuffle(_deck);
    }
    
    public List<Card> getCards(int startIndex, int numCards) {
        return _deck.subList(startIndex, startIndex + numCards);
    }
    

}
