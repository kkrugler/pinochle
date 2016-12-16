package org.krugler.pinochle;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PinochleHandBits {

    private long _cardBits;
    private int _numCards = 0;
    
    public PinochleHandBits(long cardBits) {
        _cardBits = cardBits;
        
        // TODO use bit-counting utility
        while (cardBits != 0) {
            if ((cardBits & 0x01L) != 0) {
                _numCards += 1;
            }
            
            cardBits >>= 1;
        }
        
        if (_numCards != PinochleHand.CARDS_IN_HAND) {
            throw new IllegalArgumentException("Not enough cards in the hand");
        }
    }
    
    public boolean isEmpty() {
        return _numCards == 0;
    }

    public boolean isFull() {
        return _numCards == PinochleHand.CARDS_IN_HAND;
    }

    public void playCard(long cardBit) {
        if ((_cardBits & cardBit) == 0) {
            throw new InvalidParameterException("Card " + CardBits.toCard(cardBit) + " isn't in hand");
        }
        
        _cardBits &= ~cardBit;
        _numCards -= 1;
    }
    
    public void unplayCard(long cardBit) {
        if ((_cardBits & cardBit) != 0) {
            throw new InvalidParameterException("Card " + CardBits.toCard(cardBit) + " already exists in hand");
        }

        _cardBits |= cardBit;
        _numCards += 1;
    }
    
    /**
     * Return all of the legal plays, given the state of the hand, what's trump, and
     * the previously played cards in the round.
     * 
     * @param trump
     * @param playedCards
     * @return list of valid plays
     */
    public long getPlays(int trump, long... playedCardBits) {
        long result = 0;
        
        // See if we get to lead
        if (playedCardBits.length == 0) {
            // Return all unique cards we've got in our hand.
            return _cardBits;
        } else {
            int ledSuit = CardBits.getSuit(playedCardBits[0]);
            boolean trumpLed = (ledSuit == trump);
            
            long topTrump = 0;
            if (!trumpLed) {
                for (int i = 1; i < playedCardBits.length; i++) {
                    long playedCardBit = playedCardBits[i];
                    if (CardBits.getSuit(playedCardBit) == trump) {
                        if ((topTrump == 0) || (CardBits.getRank(playedCardBit) > CardBits.getRank(topTrump))) {
                            topTrump = playedCardBit;
                        }
                    }
                }
            }
            
            boolean wasTrumped = topTrump != 0;

            // First try to play a card from the target suit which is
            // higher than the target card, but only if it hasn't been
            // trumped.
            if (!wasTrumped) {
                result = 
                for (Card card : _counts.keySet()) {
                    if ((card.getSuit() == ledSuit) && (card.isGreaterThanAll(playedCards))) {
                        plays.add(card);
                    }
                }
            }
            
            // If we don't have anything to play, see if we can play any card from
            // the led suit.
            if (plays.size() == 0) {
                for (Card card : _counts.keySet()) {
                    if (card.getSuit() == ledSuit) {
                        plays.add(card);
                    }
                }
            }
            
            // If we don't have anything to play, see if we can play
            // trump, but we have to beat trump if it's been played.
            if ((plays.size() == 0) && (ledSuit != trump)) {
                for (Card card : _counts.keySet()) {
                    if ((card.getSuit() == trump) 
                     && ((topTrump == null) || (card.compareTo(topTrump) > 0))) {
                        plays.add(card);
                    }
                }
                
                // See if we can play any trump
                if ((plays.size() == 0) && (topTrump != null)) {
                    for (Card card : _counts.keySet()) {
                        if (card.getSuit() == trump) {
                            plays.add(card);
                        }
                    }
                }
            }
            
            // If we still don't have anything to play, play from any suit.
            if (plays.size() == 0) {
                for (Card card : _counts.keySet()) {
                    plays.add(card);
                }
            }
        }
        
        return new ArrayList<Card>(plays);
    }
    

}
