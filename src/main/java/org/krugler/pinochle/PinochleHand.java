package org.krugler.pinochle;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PinochleHand {
    public static final int CARDS_IN_HAND = PinochleDeck.CARDS_IN_DECK / 4;
    
    private Map<Card, Integer> _counts;
    private int _totalCards;
    
    public PinochleHand(List<Card> cards) {
        this(cards.toArray(new Card[cards.size()]));
    }
    
    public PinochleHand(Card... cards) {
        if (cards.length != CARDS_IN_HAND) {
            throw new IllegalArgumentException("Not enough cards in the hand");
        }
        _totalCards = CARDS_IN_HAND;
        
        _counts = new HashMap<Card, Integer>(CARDS_IN_HAND);
        
        for (Card card : cards) {
            Integer count = _counts.get(card);
            if (count == null) {
                _counts.put(card, 1);
            } else {
                _counts.put(card, count + 1);
            }
        }
    }
    
    public int countCards(Card targetCard) {
        Integer count = _counts.get(targetCard);
        return (count == null ? 0 : count);
    }
    
    public boolean isEmpty() {
        return _totalCards == 0;
    }

    public boolean isFull() {
        return _totalCards == CARDS_IN_HAND;
    }

    public void playCard(Card card) {
        int count = countCards(card);
        if (count == 0) {
            throw new InvalidParameterException("Card " + card + " isn't in hand");
        } else if (count == 1) {
            _counts.remove(card);
        } else {
            _counts.put(card, 1);
        }
        
        _totalCards -= 1;
    }
    
    public void unplayCard(Card card) {
        int count = countCards(card);
        if (count == 0) {
            _counts.put(card, 1);
        } else if (count == 1) {
            _counts.put(card, 2);
        } else {
            throw new InvalidParameterException("Card " + card + " already exists twice in hand");
        }
        
        _totalCards += 1;
    }
    
    /**
     * Return all of the legal plays, given the state of the hand, what's trump, and
     * the previously played cards in the round.
     * 
     * @param trump
     * @param playedCards
     * @return list of valid plays
     */
    public List<Card> getPlays(Suit trump, Card... playedCards) {
        Set<Card> plays = new HashSet<Card>();
        
        // See if we get to lead
        if (playedCards.length == 0) {
            // Return all unique cards we've got in our hand.
            for (Card card : _counts.keySet()) {
                plays.add(card);
            }
        } else {
            Suit ledSuit = playedCards[0].getSuit();
            boolean trumpLed = (ledSuit == trump);
            
            Card topTrump = null;
            if (!trumpLed) {
                for (Card playedCard : playedCards) {
                    if (playedCard.getSuit() == trump) {
                        if ((topTrump == null) || (playedCard.compareTo(topTrump) > 0)) {
                            topTrump = playedCard;
                        }
                    }
                }
            }
            
            boolean wasTrumped = topTrump != null;

            // First try to play a card from the target suit which is
            // higher than the target card, but only if it hasn't been
            // trumped.
            if (!wasTrumped) {
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
    
    // For testing
    public void replaceCard(Card curCard, Card newCard) {
        Integer count = _counts.get(curCard);
        if (count == null) {
            throw new InvalidParameterException("Card " + curCard + " isn't in hand");
        }
        
        if (count == 1) {
            _counts.remove(curCard);
        } else {
            _counts.put(curCard, count - 1);
        }
        
        count = _counts.get(newCard);
        if (count == null) {
            _counts.put(newCard, 1);
        } else if (count == 1) {
            _counts.put(newCard, 2);
        } else {
            throw new InvalidParameterException("Card " + newCard + " already occurs twice in hand");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_counts == null) ? 0 : _counts.hashCode());
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
        PinochleHand other = (PinochleHand) obj;
        if (_counts == null) {
            if (other._counts != null)
                return false;
        } else if (!_counts.equals(other._counts))
            return false;
        return true;
    }

    
}
