package org.krugler.pinochle;

public class Suit {
    public static final int NUM_SUITS = 4;
    
    // Each suit has 6 rank, so we define each suit
    // as the number of bits we have to shift in the card to get to the
    // set of cards for the suit.
    public static final int CLUBS = 0;
    public static final int DIAMONDS = CLUBS + 6;
    public static final int HEARTS = DIAMONDS + 6;
    public static final int SPADES = HEARTS + 6;

    // Calling no trump is an option, so we define a special value for that.
    public static final int NO_TRUMP = SPADES + 6;

    public static final int[] SUITS = {CLUBS, DIAMONDS, HEARTS, SPADES};
    
    public static int getSuitFromCard(int card) {
        int shiftAmount = BitUtil.ntz(card);
        if (shiftAmount >= NO_TRUMP) {
            throw new IllegalArgumentException(String.format("Invalid card: %s", Card.asBits(card)));
        } else if (shiftAmount >= SPADES) {
            return SPADES;
        } else if (shiftAmount >= HEARTS) {
            return HEARTS;
        } else if (shiftAmount >= DIAMONDS) {
            return DIAMONDS;
        } else {
            return CLUBS;
        }
    }

    public static int lowestCardInSuit(int suit) {
        return 1 << suit;
    }
    
    public static int highestCardInSuit(int suit) {
        return 1 << (suit + Rank.NUM_RANKS - 1);
    }

    public static String toString(int suit) {
        if (suit == CLUBS) {
            return "CLUBS";
        } else if (suit == DIAMONDS) {
            return "DIAMONDS";
        } else if (suit == HEARTS) {
            return "HEARTS";
        } else if (suit == SPADES) {
            return "SPADES";
        } else {
            throw new IllegalArgumentException(String.format("Invalid suit: %d", suit));
        }
    }

}
