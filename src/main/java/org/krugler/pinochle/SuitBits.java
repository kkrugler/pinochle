package org.krugler.pinochle;

public class SuitBits {
    private static final int NUM_SUITS = Suit.values().length;

    public static final int CARDS_IN_SUIT = PinochleDeck.CARDS_IN_DECK / SuitBits.NUM_SUITS;
    
    public static final int CLUBS_INDEX = Suit.CLUBS.ordinal();
    public static final int DIAMONDS_INDEX = Suit.DIAMONDS.ordinal();
    public static final int HEARTS_INDEX = Suit.HEARTS.ordinal();
    public static final int SPADES_INDEX = Suit.SPADES.ordinal();
}
