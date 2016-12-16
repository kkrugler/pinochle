package org.krugler.pinochle;

public class Card implements Comparable<Card> {
    public static final Card NINE_CLUBS = new Card(Rank.NINE, Suit.CLUBS);
    public static final Card JACK_CLUBS = new Card(Rank.JACK, Suit.CLUBS);
    public static final Card QUEEN_CLUBS = new Card(Rank.QUEEN, Suit.CLUBS);
    public static final Card KING_CLUBS = new Card(Rank.KING, Suit.CLUBS);
    public static final Card TEN_CLUBS = new Card(Rank.TEN, Suit.CLUBS);
    public static final Card ACE_CLUBS = new Card(Rank.ACE, Suit.CLUBS);
    
    public static final Card NINE_DIAMONDS = new Card(Rank.NINE, Suit.DIAMONDS);
    public static final Card JACK_DIAMONDS = new Card(Rank.JACK, Suit.DIAMONDS);
    public static final Card QUEEN_DIAMONDS = new Card(Rank.QUEEN, Suit.DIAMONDS);
    public static final Card KING_DIAMONDS = new Card(Rank.KING, Suit.DIAMONDS);
    public static final Card TEN_DIAMONDS = new Card(Rank.TEN, Suit.DIAMONDS);
    public static final Card ACE_DIAMONDS = new Card(Rank.ACE, Suit.DIAMONDS);
    
    public static final Card NINE_HEARTS = new Card(Rank.NINE, Suit.HEARTS);
    public static final Card JACK_HEARTS = new Card(Rank.JACK, Suit.HEARTS);
    public static final Card QUEEN_HEARTS = new Card(Rank.QUEEN, Suit.HEARTS);
    public static final Card KING_HEARTS = new Card(Rank.KING, Suit.HEARTS);
    public static final Card TEN_HEARTS = new Card(Rank.TEN, Suit.HEARTS);
    public static final Card ACE_HEARTS = new Card(Rank.ACE, Suit.HEARTS);
    
    public static final Card NINE_SPADES = new Card(Rank.NINE, Suit.SPADES);
    public static final Card JACK_SPADES = new Card(Rank.JACK, Suit.SPADES);
    public static final Card QUEEN_SPADES = new Card(Rank.QUEEN, Suit.SPADES);
    public static final Card KING_SPADES = new Card(Rank.KING, Suit.SPADES);
    public static final Card TEN_SPADES = new Card(Rank.TEN, Suit.SPADES);
    public static final Card ACE_SPADES = new Card(Rank.ACE, Suit.SPADES);
    
    private final Rank _rank;
    private final Suit _suit;
    
    public Card(Rank rank, Suit suit) {
        _rank = rank;
        _suit = suit;
    }

    public Rank getRank() {
        return _rank;
    }

    public Suit getSuit() {
        return _suit;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_rank == null) ? 0 : _rank.hashCode());
        result = prime * result + ((_suit == null) ? 0 : _suit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Card other = (Card) obj;
        if (_rank == null) {
            if (other._rank != null)
                return false;
        } else if (!_rank.equals(other._rank))
            return false;
        if (_suit == null) {
            if (other._suit != null)
                return false;
        } else if (!_suit.equals(other._suit))
            return false;
        return true;
    }

    @Override
    public int compareTo(Card card) {
        return _rank.compareTo(card._rank);
    }

    /**
     * Return true if this card sorts after (is higher than)
     * all of the provided cards.
     *  
     * @param cards
     * @return
     */
    public boolean isGreaterThanAll(Card... cards) {
        for (Card card : cards) {
            if (compareTo(card) <= 0) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("%s of %s", _rank, _suit);
    }

    /**
     * Given previously played cards, return the position of the
     * winning card. We assume plays are all valid.
     * 
     * @param trump what suit is trump
     * @param playedCards
     * @return position (0..n) in playedCards of winning play
     */
    public static int winningCardPos(Suit trump, Card[] playedCards) {
        if (playedCards.length != PinochleGame.NUM_PLAYERS) {
            throw new IllegalArgumentException("Not enough cards played");
        }

        Suit ledSuit = playedCards[0].getSuit();
        boolean trumpLed = (ledSuit == trump);

        Card topCard = null;
        int topCardPos = 0;
        
        if (trumpLed) {
            for (int i = 0; i < playedCards.length; i++) {
                Card card = playedCards[i];
                if ((topCard == null) || (card.compareTo(topCard) > 0)) {
                    topCard = card;
                    topCardPos = i;
                }
            }
        } else {
            boolean trumped = false;
            for (int i = 0; i < playedCards.length; i++) {
                Card card = playedCards[i];
                if ((topCard == null)
                 || (!trumped && (card.compareTo(topCard) > 0))
                 || (trumped && (card.getSuit() == trump) && (card.compareTo(topCard) > 0))) {
                    topCard = card;
                    topCardPos = i;
                    
                    trumped = (topCard.getSuit() == trump);
                }
            }
        }
        
        return topCardPos;
    }
    
}
