package org.krugler.pinochle;


public class PinochleMeld {
    
    private static final PinochleMeld[] MELD = {
        new PinochleMeld(0, 1, true, Card.NINE_CLUBS),
        new PinochleMeld(0, 1, true, Card.NINE_DIAMONDS),
        new PinochleMeld(0, 1, true, Card.NINE_HEARTS),
        new PinochleMeld(0, 1, true, Card.NINE_SPADES),
        
        new PinochleMeld(2, 4, true, Card.KING_CLUBS, Card.QUEEN_CLUBS),
        new PinochleMeld(2, 4, true, Card.KING_DIAMONDS, Card.QUEEN_DIAMONDS),
        new PinochleMeld(2, 4, true, Card.KING_HEARTS, Card.QUEEN_HEARTS),
        new PinochleMeld(2, 4, true, Card.KING_SPADES, Card.QUEEN_SPADES),

        // Pinochle & double Pinochle
        new PinochleMeld(4, 30, 4, 30, Card.JACK_DIAMONDS, Card.QUEEN_SPADES),
        
        new PinochleMeld(4, 40, 4, 40, Card.JACK_CLUBS, Card.JACK_DIAMONDS, Card.JACK_HEARTS, Card.JACK_SPADES),
        new PinochleMeld(6, 60, 6, 60, Card.QUEEN_CLUBS, Card.QUEEN_DIAMONDS, Card.QUEEN_HEARTS, Card.QUEEN_SPADES),
        new PinochleMeld(8, 80, 8, 80, Card.KING_CLUBS, Card.KING_DIAMONDS, Card.KING_HEARTS, Card.KING_SPADES),
        new PinochleMeld(10, 100, 10, 100, Card.ACE_CLUBS, Card.ACE_DIAMONDS, Card.ACE_HEARTS, Card.ACE_SPADES),

        // Run & double-run. Subtract marriage score, which will also be added in.
        new PinochleMeld(0, 0, 11, 142, Card.JACK_CLUBS, Card.QUEEN_CLUBS, Card.KING_CLUBS, Card.TEN_CLUBS, Card.ACE_CLUBS),
        new PinochleMeld(0, 0, 11, 142, Card.JACK_DIAMONDS, Card.QUEEN_DIAMONDS, Card.KING_DIAMONDS, Card.TEN_DIAMONDS, Card.ACE_DIAMONDS),
        new PinochleMeld(0, 0, 11, 142, Card.JACK_HEARTS, Card.QUEEN_HEARTS, Card.KING_HEARTS, Card.TEN_HEARTS, Card.ACE_HEARTS),
        new PinochleMeld(0, 0, 11, 142, Card.JACK_SPADES, Card.QUEEN_SPADES, Card.KING_SPADES, Card.TEN_SPADES, Card.ACE_SPADES),
    };
    
    private int[] _cards;
    private int _score;
    private int _trumpScore;
    private int _doubledScore;
    private int _doubledTrumpScore;
    
    /**
     * @param score
     * @param trumpScore
     * @param doubleScore bogus param so compiler doesn't call us with fully expanded data (int... and int, int, int... look the same)
     * @param cards
     */
    public PinochleMeld(int score, int trumpScore, boolean doubleScore, int... cards) {
        this(score, score * 2, trumpScore, trumpScore * 2, cards);
    }
    
    public PinochleMeld(int score, int doubledScore, int trumpScore, int doubledTrumpScore, int... cards) {
        _cards = cards;
        _score = score;
        _doubledScore = doubledScore;
        _trumpScore = trumpScore;
        _doubledTrumpScore = doubledTrumpScore;
    }
    
    public int getScore(PinochleHand hand, int trumpSuit) {
        boolean inTrump = trumpSuit == Suit.getSuitFromCard(_cards[0]);
        
        // If we won't get any score unless the suit matches trump, do that check first.
        if ((_score == 0) && !inTrump) {
            return 0;
        }
        
        boolean twoOfEach = true;
        
        for (int card : _cards) {
            int count = hand.countCards(card);
            if (count == 0) {
                return 0;
            } else if (count == 1) {
                twoOfEach = false;
            } else if (count != 2) {
                throw new RuntimeException("Count of any card type in hand must be 0, 1, or 2; we got " + count);
            }
        }
        
        if (twoOfEach) {
            if (inTrump) {
                return _doubledTrumpScore;
            } else {
                return _doubledScore;
            }
        } else {
            if (inTrump) {
                return _trumpScore;
            } else {
                return _score;
            }
        }
    }
    
    public static int calcMeld(PinochleHand hand, int trumpSuit) {
        int result = 0;
        for (PinochleMeld meld : MELD) {
            result += meld.getScore(hand, trumpSuit);
        }
        
        return result;
    }
}
