package org.krugler.pinochle;

public class Card {
    public static final int NINE_CLUBS = makeCard(Rank.NINE, Suit.CLUBS);
    public static final int JACK_CLUBS = makeCard(Rank.JACK, Suit.CLUBS);
    public static final int QUEEN_CLUBS = makeCard(Rank.QUEEN, Suit.CLUBS);
    public static final int KING_CLUBS = makeCard(Rank.KING, Suit.CLUBS);
    public static final int TEN_CLUBS = makeCard(Rank.TEN, Suit.CLUBS);
    public static final int ACE_CLUBS = makeCard(Rank.ACE, Suit.CLUBS);
    
    public static final int NINE_DIAMONDS = makeCard(Rank.NINE, Suit.DIAMONDS);
    public static final int JACK_DIAMONDS = makeCard(Rank.JACK, Suit.DIAMONDS);
    public static final int QUEEN_DIAMONDS = makeCard(Rank.QUEEN, Suit.DIAMONDS);
    public static final int KING_DIAMONDS = makeCard(Rank.KING, Suit.DIAMONDS);
    public static final int TEN_DIAMONDS = makeCard(Rank.TEN, Suit.DIAMONDS);
    public static final int ACE_DIAMONDS = makeCard(Rank.ACE, Suit.DIAMONDS);
    
    public static final int NINE_HEARTS = makeCard(Rank.NINE, Suit.HEARTS);
    public static final int JACK_HEARTS = makeCard(Rank.JACK, Suit.HEARTS);
    public static final int QUEEN_HEARTS = makeCard(Rank.QUEEN, Suit.HEARTS);
    public static final int KING_HEARTS = makeCard(Rank.KING, Suit.HEARTS);
    public static final int TEN_HEARTS = makeCard(Rank.TEN, Suit.HEARTS);
    public static final int ACE_HEARTS = makeCard(Rank.ACE, Suit.HEARTS);
    
    public static final int NINE_SPADES = makeCard(Rank.NINE, Suit.SPADES);
    public static final int JACK_SPADES = makeCard(Rank.JACK, Suit.SPADES);
    public static final int QUEEN_SPADES = makeCard(Rank.QUEEN, Suit.SPADES);
    public static final int KING_SPADES = makeCard(Rank.KING, Suit.SPADES);
    public static final int TEN_SPADES = makeCard(Rank.TEN, Suit.SPADES);
    public static final int ACE_SPADES = makeCard(Rank.ACE, Suit.SPADES);
    
    private static final int COUNTERS = KING_CLUBS | TEN_CLUBS | ACE_CLUBS 
                    | KING_DIAMONDS | TEN_DIAMONDS | ACE_DIAMONDS 
                    | KING_HEARTS | TEN_HEARTS | ACE_HEARTS 
                    | KING_SPADES | TEN_SPADES | ACE_SPADES;
    
    // Valid bits for a card.
    private static final int CARD_MASK = 0x0FFFFFF;
    
    public static int makeCard(int rank, int suit) {
        return rank << suit;
    }
    
    public static int compare(int cardA, int cardB) {
        if (Suit.getSuitFromCard(cardA) != Suit.getSuitFromCard(cardB)) {
            throw new IllegalArgumentException(String.format("Card %d has different suit than card %d", cardA, cardB));
        }
        
        return Integer.compare(cardA, cardB);
    }
    
    /**
     * Given previously played cards, return the position of the
     * winning card. We assume plays are all valid.
     * 
     * @param trump what suit is trump
     * @param playedCards
     * @return position (0..n) in playedCards of winning play
     */
    public static int winningCardPos(int trumpSuit, int[] playedCards, int handOffset, int handCardsPlayed) {
        if (handCardsPlayed == 0) {
            throw new IllegalArgumentException("Not enough cards played");
        } else if (handCardsPlayed > PinochleGame.NUM_PLAYERS) {
            throw new IllegalArgumentException("Too many cards played");
        }

        int topCard = playedCards[handOffset];
        int topCardPos = handOffset;
        int topCardSuit = Suit.getSuitFromCard(topCard);

        for (int i = 1; i < handCardsPlayed; i++) {
            int card = playedCards[handOffset + i];
            int cardSuit = Suit.getSuitFromCard(card);

            // If card is same suit as what was led, then we only care
            // about comparing.
            if (cardSuit == topCardSuit) {
                if (Card.compare(card, topCard) > 0) {
                    topCard = card;
                    topCardPos = handOffset + i;
                }
            } else if (cardSuit == trumpSuit) {
                topCard = card;
                topCardPos = handOffset + i;
                topCardSuit = trumpSuit;
            }
        }

        return topCardPos;
    }

    public static boolean isValid(int card) {
        // Only one bit can be set, and this bit has to be in the
        // range of valid bits
        return BitUtil.isPowerOfTwo(card)
            && ((card & CARD_MASK) != 0);
    }

    public static String toString(int card) {
        return String.format("%s of %s", Rank.toString(Rank.getRankFromCard(card)), Suit.toString(Suit.getSuitFromCard(card)));
    }

    public static String asBits(int card) {
        return String.format("%16s", Integer.toBinaryString(card)).replace(' ', '0');
    }

    public static int numCounters(int[] playedCards, int handOffset, int handCardsPlayed) {
        int result = 0;
        for (int i = 0; i < handCardsPlayed; i++) {
            if (Card.isCounter(playedCards[handOffset + i])) {
                result += 1;
            }
        }

        return result;
    }

    private static boolean isCounter(int card) {
        return (card & COUNTERS) != 0;
    }
    
}
