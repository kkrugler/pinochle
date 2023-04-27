package org.krugler.pinochle;

import java.util.HashSet;
import java.util.Set;


public class PinochleHand {
    public static final int CARDS_IN_HAND = PinochleDeck.CARDS_IN_DECK / 4;
    
    private int _totalCards;
    
    // A hand has two sets of 32 bits, each representing the 24 unique
    // cards. Low set has the first card, high set has the second card.
    private long _hand;
    
    public PinochleHand(PinochleDeck deck, int player) {
        init();
        
        for (int i = 0; i < CARDS_IN_HAND; i++) {
            addCard(deck.getCard(player, i));
        }
    }
    
    public PinochleHand(int... cards) {
        init();
        
        if (cards.length != CARDS_IN_HAND) {
            throw new IllegalArgumentException("Not enough cards in the hand");
        }
        
        for (int card : cards) {
            addCard(card);
        }
    }
    
    private void init() {
        _totalCards = 0;
        _hand = 0;
    }
    
    private void addCard(int card) {
        if (!Card.isValid(card)) {
            throw new IllegalArgumentException(String.format("Invalid card: %d", card));
        }
        
        // See if we have one of these cards already.
        long handCard = card;
        if (isHandCardInHand(handCard)) {
            handCard <<= 32;
            
            if (isHandCardInHand(handCard)) {
                throw new IllegalArgumentException(String.format("Card already in hand: %d", card));
            }
        }
        
        _hand |= handCard;
        _totalCards++;
    }

    public int countCards(int card) {
        if (!Card.isValid(card)) {
            throw new IllegalArgumentException(String.format("Invalid card: %d", card));
        }
        
        long handCard = card;
        if (isHandCardInHand(handCard)) {
            handCard <<= 32;
            if (isHandCardInHand(handCard)) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }
    
    public boolean isEmpty() {
        return _totalCards == 0;
    }

    public boolean isFull() {
        return _totalCards == CARDS_IN_HAND;
    }

    public void playCard(int card) {
        if (!Card.isValid(card)) {
            throw new IllegalArgumentException(String.format("Invalid card: %d", card));
        }

        long handCard = card;
        if (!isHandCardInHand(handCard)) {
            throw new IllegalArgumentException(String.format("Card not in hand: %d", card));
        }

        // See if we have the second card. If so, remove that one.
        long secondCard = handCard << 32;
        if (isHandCardInHand(secondCard)) {
            _hand &= ~secondCard;
        } else {
            _hand &= ~handCard;
        }
        
        _totalCards -= 1;
    }
    
    public void unplayCard(int card) {
        addCard(card);
    }
    
    /**
     * Return all of the legal plays, given the state of the hand, what's trump, and
     * the previously played cards.
     * 
     * TODO If we're not winning the trick, don't bother returning anything but the
     * lowest counter and the lowest non-counter, as there's no reason to play anything
     * else but those two? Hmm, actually not true, you might throw the Ace vs. the 10
     * just to let your partner know that you've got it...but ignore that for now.
     * 
     * @param trump
     * @param playedCards
     * @return number of results in plays[]
     */
    public int getPlays(int trumpSuit, int[] playedCards, int handOffset, int handCardsPlayed, int[] plays) {
        int numPlays = 0;
        
        // See if we get to lead
        if (handCardsPlayed == 0) {
            numPlays += getUniqueCards(plays, numPlays);
        } else {
            int ledSuit = Suit.getSuitFromCard(playedCards[handOffset]);
            int winningPlayPos = Card.winningCardPos(trumpSuit, playedCards, handOffset, handCardsPlayed);
            
            int winningCard = playedCards[winningPlayPos];
            int winningSuit = Suit.getSuitFromCard(winningCard);
            boolean trumped = (winningSuit == trumpSuit) && (winningSuit != ledSuit);
            
            // First check for cards in the suit that was led.
            boolean hasHigherCardInLedSuit = false;
            for (int card = Suit.highestCardInSuit(ledSuit); card >= Suit.lowestCardInSuit(ledSuit); card >>= 1) {
                if (isCardInHand(card)) {
                    if (trumped) {
                        plays[numPlays++] = card;
                    } else if (Card.compare(card, winningCard) > 0) {
                        plays[numPlays++] = card;
                        hasHigherCardInLedSuit = true;
                    } else if (!hasHigherCardInLedSuit) {
                        // No higher card in suit, so we can play it.
                        plays[numPlays++] = card;
                    } else {
                        // We have a higher card in the suit, so we're done.
                        break;
                    }
                }
            }
            
            // If we don't have any play, then we must not have any cards in the led suit, so then
            // see if we should check for being able to trump.
            if ((numPlays == 0) && (ledSuit != trumpSuit)) {
                boolean hasHigherCardInTrumpSuit = false;
                for (int card = Suit.highestCardInSuit(trumpSuit); card >= Suit.lowestCardInSuit(trumpSuit); card >>= 1) {
                    if (isCardInHand(card)) {
                        if (!trumped || Card.compare(card, winningCard) > 0) {
                            plays[numPlays++] = card;
                            hasHigherCardInTrumpSuit = true;
                        } else if (!trumped || !hasHigherCardInTrumpSuit) {
                            plays[numPlays++] = card;
                        } else {
                            // We have a higher card in trump, so we're done.
                            break;
                        }
                    }
                }
            }
            
            // Finally, if we still have no cards to play, then we must not have a card in the led suit or trump, so
            // just return all of the cards we've got
            if (numPlays == 0) {
                // Return all unique cards we've got in our hand. We'll worry about sorting these
                // later.
                for (int card = 1, cardIndex = 0; cardIndex < PinochleDeck.UNIQUE_CARDS_IN_DECK; cardIndex++, card <<= 1) {
                    if (isCardInHand(card)) {
                        plays[numPlays++] = card;
                    }
                }
            }
        }
        
        return numPlays;
    }
    

    /**
     * Add unique cards in hand to <plays>, starting at <offset>.
     * 
     * @param plays
     * @param offset
     * @return number of cards added.
     */
    public int getUniqueCards(int[] plays, int offset) {
        int count = 0;
        int remainingCards = _totalCards;
        // We only care about the first instance of any card.
        int curHandBits = (int)_hand;
        int curCard = Card.makeCard(Rank.FIRST, Suit.FIRST);
        while ((remainingCards > 0) && (curHandBits != 0)) {
            if ((curHandBits & 0x01) != 0) {
                plays[offset++] = curCard;
                remainingCards--;
                count++;
            }
            
            curCard <<= 1;
            curHandBits >>= 1;
        }
        
        return count;
    }

    private boolean isCardInHand(int card) {
        return (_hand & card) != 0;
    }

    // A "hand card" is the full set of cards (all 48 possible cards), not just the
    // 24 unique cards.
    private boolean isHandCardInHand(long handCard) {
        return (_hand & handCard) != 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (_hand ^ (_hand >>> 32));
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
        if (_hand != other._hand)
            return false;
        return true;
    }

    /**
     * Turn cards in our hand into a set. This is for testing only, so it doesn't need to
     * be fast.
     * 
     * @return
     */
    public Set<Integer> asSet() {
        Set<Integer> result = new HashSet<Integer>();
        for (int suit : Suit.SUITS) {
            for (int rank : Rank.RANKS) {
                int card = Card.makeCard(rank, suit);
                for (int i = 0; i < countCards(card); i++) {
                    result.add(card);
                }
            }
        }
        
        return result;
    }

    public void replaceCard(int currentCard, int newCard) {
        playCard(currentCard);
        unplayCard(newCard);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        int curSuit = Suit.NO_TRUMP;
        for (int suit : Suit.SUITS) {
            for (int rank : Rank.RANKS) {
                int card = Card.makeCard(rank, suit);
                for (int i = 0; i < countCards(card); i++) {
                    if (suit != curSuit) {
                        if (curSuit != Suit.NO_TRUMP) {
                            result.append(", ");
                        }
                        
                        result.append(Suit.toString(suit));
                        result.append(": ");
                        curSuit = suit;
                    }
                    
                    result.append(Rank.toShortString(rank));
                }
            }
        }

        return result.toString();
    }
    
}
