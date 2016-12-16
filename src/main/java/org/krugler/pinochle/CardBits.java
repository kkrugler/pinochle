package org.krugler.pinochle;

/**
 * Bit-based card values. Bits are grouped by suit (low to high), and by rank (9 to A). So bit 0 is 9 of Clubs,
 * bit 1 is the other 9 of Clubs, bit 2 is the Jack of Clubs, bit 3 is the other Jack of clubs, on up to bit
 * 47 is the second Ace of Spaces.
 *
 */
public class CardBits {
    private static final long ALL_CARDS_ONE_SUIT = 0x0000000000000FFFL;
    
    public static final long ALL_CLUBS      = (ALL_CARDS_ONE_SUIT << (SuitBits.CLUBS_INDEX * SuitBits.CARDS_IN_SUIT));
    public static final long ALL_DIAMONDS   = (ALL_CARDS_ONE_SUIT << (SuitBits.DIAMONDS_INDEX * SuitBits.CARDS_IN_SUIT));
    public static final long ALL_HEARTS     = (ALL_CARDS_ONE_SUIT << (SuitBits.HEARTS_INDEX * SuitBits.CARDS_IN_SUIT));
    public static final long ALL_SPADES     = (ALL_CARDS_ONE_SUIT << (SuitBits.SPADES_INDEX * SuitBits.CARDS_IN_SUIT));
    
    public static final long ALL_CARDS      = ALL_CLUBS | ALL_DIAMONDS | ALL_HEARTS | ALL_SPADES;

    // Bit mask for all cards of a specific suit above a target rank.
    // Indexed by RankBits values for 9, J, Q, K, 10. No need for A, always highest
    public static final long[] ALL_CLUBS_ABOVE = new long[] {
        (ALL_CARDS_ONE_SUIT & ~0x0000000000000003L) << (SuitBits.CLUBS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000000FL) << (SuitBits.CLUBS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000003FL) << (SuitBits.CLUBS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000000FFL) << (SuitBits.CLUBS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000003FFL) << (SuitBits.CLUBS_INDEX * SuitBits.CARDS_IN_SUIT),
    };
    
    public static final long[] ALL_DIAMONDS_ABOVE = new long[] {
        (ALL_CARDS_ONE_SUIT & ~0x0000000000000003L) << (SuitBits.DIAMONDS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000000FL) << (SuitBits.DIAMONDS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000003FL) << (SuitBits.DIAMONDS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000000FFL) << (SuitBits.DIAMONDS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000003FFL) << (SuitBits.DIAMONDS_INDEX * SuitBits.CARDS_IN_SUIT),
    };
    
    public static final long[] ALL_HEARTS_ABOVE = new long[] {
        (ALL_CARDS_ONE_SUIT & ~0x0000000000000003L) << (SuitBits.HEARTS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000000FL) << (SuitBits.HEARTS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000003FL) << (SuitBits.HEARTS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000000FFL) << (SuitBits.HEARTS_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000003FFL) << (SuitBits.HEARTS_INDEX * SuitBits.CARDS_IN_SUIT),
    };
    
    public static final long[] ALL_SPADES_ABOVE = new long[] {
        (ALL_CARDS_ONE_SUIT & ~0x0000000000000003L) << (SuitBits.SPADES_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000000FL) << (SuitBits.SPADES_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x000000000000003FL) << (SuitBits.SPADES_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000000FFL) << (SuitBits.SPADES_INDEX * SuitBits.CARDS_IN_SUIT),
        (ALL_CARDS_ONE_SUIT & ~0x00000000000003FFL) << (SuitBits.SPADES_INDEX * SuitBits.CARDS_IN_SUIT),
    };

    public static Card toCard(long cardBits) {
        // Convert a cardBit with one bit set.
        if (cardBits == 0) {
            throw new IllegalArgumentException("Invalid card bit value: 0");
        }
        
        int cardBit = 0;
        long savedCardBits = cardBits;
        while (cardBits != 0) {
            if ((cardBits & 0x01L) == 1) {
                if (cardBits != 1L) {
                    throw new IllegalArgumentException("Invalid card bit value: " + cardBits);
                }

                break;
            } else {
                cardBit += 1;
                cardBits >>= 1;
            }
        }
        
        if (cardBit >= PinochleDeck.CARDS_IN_DECK) {
            throw new IllegalArgumentException("Invalid card bit value: " + savedCardBits);
        }
        
        // Figure out SuitBit
        int suitBit = cardBit / SuitBits.CARDS_IN_SUIT;
        int rankBit = cardBit % SuitBits.CARDS_IN_SUIT;
        
        return new Card(Rank.values()[rankBit], Suit.values()[suitBit]);
    }
    
}
