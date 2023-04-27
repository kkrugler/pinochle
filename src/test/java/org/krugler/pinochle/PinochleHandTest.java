package org.krugler.pinochle;

import java.util.Arrays;
import java.util.Set;

import static junit.framework.Assert.*;

import org.junit.Test;


public class PinochleHandTest {

    @Test
    public void testValidCreation() throws Exception {
        try {
            new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.KING_CLUBS,
                        Card.QUEEN_CLUBS,
                        Card.JACK_CLUBS,
                        
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_HEARTS);
            fail("Should have throw exception due to not enough cards");
        } catch (Exception e) {
            // all good.
        }

        try {
            new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.NINE_SPADES,
                        Card.NINE_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.KING_CLUBS,
                        Card.QUEEN_CLUBS,
                        Card.JACK_CLUBS,
                        
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_HEARTS);
            fail("Should have throw exception due to three of same card");
        } catch (Exception e) {
            // all good.
        }

    }
    
    @Test
    public void testFirstPlay() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.KING_CLUBS,
                        Card.QUEEN_CLUBS,
                        Card.JACK_CLUBS,
                        
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_HEARTS);

        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int numPlays = hand.getPlays(Suit.SPADES, new int[0], 0, 0, plays);
        assertEquals(12, numPlays);
        
        Set<Integer> handAsSet = hand.asSet();
        
        for (int i = 0; i < numPlays; i++) {
            assertTrue(handAsSet.remove(plays[i]));
        }
        
        assertTrue(handAsSet.isEmpty());
    }
    
    @Test
    public void testNoDuplicates() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES, Card.KING_SPADES,
                        Card.QUEEN_SPADES, Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.JACK_DIAMONDS, Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        
                        Card.JACK_HEARTS);
        
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int numPlays = hand.getPlays(Suit.SPADES, new int[0], 0, 0, plays);
        
        // Three cards are dups in hand, so only 9 unique plays
        assertEquals(9, numPlays);
        
        Set<Integer> handAsSet = hand.asSet();
        assertEquals(9, handAsSet.size());

        for (int i = 0; i < numPlays; i++) {
            assertTrue(handAsSet.remove(plays[i]));
        }
        
        assertTrue(handAsSet.isEmpty());
    }
    
    @Test
    public void testLeadWasTrumped() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES, Card.KING_SPADES,
                        Card.QUEEN_SPADES, Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Hearts was led, then got trumped and over-trumped. So we have to play something
        // higher than the jack of spades.
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.NINE_HEARTS, Card.NINE_SPADES, Card.JACK_SPADES};
        int numPlays = hand.getPlays(Suit.SPADES, cardsPlayed, 0, 3, plays);
        assertEquals(4, numPlays);
        Arrays.sort(plays, 0, numPlays);
        assertEquals(Card.QUEEN_SPADES, plays[0]);
        assertEquals(Card.KING_SPADES, plays[1]);
        assertEquals(Card.TEN_SPADES, plays[2]);
        assertEquals(Card.ACE_SPADES, plays[3]);
    }
    
    @Test
    public void testHigherCardInSuit() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.TEN_DIAMONDS,
                        Card.KING_DIAMONDS,
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Diamonds is trump, Spades was led, and we have to play a higher card
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.NINE_SPADES, Card.QUEEN_SPADES};
        int numPlays = hand.getPlays(Suit.DIAMONDS, cardsPlayed, 0, 2, plays);
        assertEquals(3, numPlays);
        Arrays.sort(plays, 0, numPlays);
        
        assertEquals(Card.KING_SPADES, plays[0]);
        assertEquals(Card.TEN_SPADES, plays[1]);
        assertEquals(Card.ACE_SPADES, plays[2]);
    }
    
    @Test
    public void testNoCardInLedSuitButHasTrump() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.TEN_DIAMONDS,
                        Card.KING_DIAMONDS,
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Diamonds is trump, Hearts was led, so we can play any trump
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.NINE_HEARTS};
        int numPlays = hand.getPlays(Suit.DIAMONDS, cardsPlayed, 0, 1, plays);
        assertEquals(5, numPlays);
        Arrays.sort(plays, 0, numPlays);
        
        assertEquals(Card.NINE_DIAMONDS, plays[0]);
        assertEquals(Card.JACK_DIAMONDS, plays[1]);
        assertEquals(Card.QUEEN_DIAMONDS, plays[2]);
        assertEquals(Card.KING_DIAMONDS, plays[3]);
        assertEquals(Card.TEN_DIAMONDS, plays[4]);
    }
    
    @Test
    public void testAnyCardInSuitWhenTrumped() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.TEN_DIAMONDS,
                        Card.KING_DIAMONDS,
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Diamonds is trump, Spades was led, and was trumped, so we can play any card
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.QUEEN_SPADES, Card.NINE_DIAMONDS};
        int numPlays = hand.getPlays(Suit.DIAMONDS, cardsPlayed, 0, 2, plays);
        assertEquals(6, numPlays);
        Arrays.sort(plays, 0, numPlays);
        
        assertEquals(Card.NINE_SPADES, plays[0]);
        assertEquals(Card.ACE_SPADES, plays[5]);
    }
    
    @Test
    public void testAnyCardInSuitWhenTrumpedWithPreviouslyPlayedCards() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.TEN_DIAMONDS,
                        Card.KING_DIAMONDS,
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Diamonds is trump, Spades was led, and was trumped, so we can play any card
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.ACE_DIAMONDS, Card.NINE_DIAMONDS, Card.KING_DIAMONDS, Card.JACK_DIAMONDS, Card.QUEEN_SPADES, Card.NINE_DIAMONDS};
        int numPlays = hand.getPlays(Suit.DIAMONDS, cardsPlayed, 4, 2, plays);
        assertEquals(6, numPlays);
        Arrays.sort(plays, 0, numPlays);
        
        assertEquals(Card.NINE_SPADES, plays[0]);
        assertEquals(Card.ACE_SPADES, plays[5]);
    }
    
    @Test
    public void testAnyCardWithNoTrump() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.TEN_DIAMONDS,
                        Card.KING_DIAMONDS,
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Hearts is trump, and was led, we have none, so we can play anything.
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.NINE_HEARTS};
        int numPlays = hand.getPlays(Suit.HEARTS, cardsPlayed, 0, 1, plays);
        assertEquals(12, numPlays);
        Arrays.sort(plays, 0, numPlays);
        
        assertEquals(Card.JACK_CLUBS, plays[0]);
        assertEquals(Card.ACE_SPADES, plays[11]);
    }
    
    @Test
    public void testWithPreviouslyPlayedHands() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.TEN_DIAMONDS,
                        Card.KING_DIAMONDS,
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        Card.NINE_DIAMONDS,
                        
                        Card.JACK_CLUBS);
        
        // Hearts is trump, and was led, we have none, so we can play anything.
        int[] plays = new int[PinochleHand.CARDS_IN_HAND];
        int[] cardsPlayed = new int[] {Card.QUEEN_CLUBS, Card.NINE_CLUBS, Card.KING_CLUBS, Card.ACE_CLUBS, Card.NINE_HEARTS};
        int numPlays = hand.getPlays(Suit.HEARTS, cardsPlayed, 4, 1, plays);
        assertEquals(12, numPlays);
        Arrays.sort(plays, 0, numPlays);
        
        assertEquals(Card.JACK_CLUBS, plays[0]);
        assertEquals(Card.ACE_SPADES, plays[11]);
    }
    
    @Test
    public void testPlayingCards() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES, Card.KING_SPADES,
                        Card.QUEEN_SPADES, Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        
                        Card.JACK_HEARTS);

        hand.playCard(Card.ACE_SPADES);
        assertEquals(0, hand.countCards(Card.ACE_SPADES));
        
        hand.playCard(Card.KING_SPADES);
        assertEquals(1, hand.countCards(Card.KING_SPADES));
        hand.playCard(Card.KING_SPADES);
        assertEquals(0, hand.countCards(Card.KING_SPADES));
        
        hand.unplayCard(Card.KING_SPADES);
        assertEquals(1, hand.countCards(Card.KING_SPADES));
        
        hand.unplayCard(Card.KING_SPADES);
        assertEquals(2, hand.countCards(Card.KING_SPADES));
        
        try {
            hand.unplayCard(Card.KING_SPADES);
            fail("Should have thrown exception");
        } catch (Exception e) {
            // all good
        }
    }
    
    @Test
    public void testGetUniqueCards() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES, Card.KING_SPADES,
                        Card.QUEEN_SPADES, Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        
                        Card.JACK_HEARTS);
        
        int[] cards = new int[PinochleHand.CARDS_IN_HAND];
        int count = hand.getUniqueCards(cards, 0);
        assertEquals(10, count);
        int i = 0;
        assertEquals(Card.JACK_CLUBS, cards[i++]);
        assertEquals(Card.JACK_DIAMONDS, cards[i++]);
        assertEquals(Card.QUEEN_DIAMONDS, cards[i++]);
        assertEquals(Card.JACK_HEARTS, cards[i++]);
        assertEquals(Card.NINE_SPADES, cards[i++]);
        assertEquals(Card.JACK_SPADES, cards[i++]);
        assertEquals(Card.QUEEN_SPADES, cards[i++]);
        assertEquals(Card.KING_SPADES, cards[i++]);
        assertEquals(Card.TEN_SPADES, cards[i++]);
        assertEquals(Card.ACE_SPADES, cards[i++]);
        
        while (count > 0) {
            hand.playCard(cards[--count]);
        }
        
        hand.playCard(Card.KING_SPADES);
        hand.playCard(Card.QUEEN_SPADES);
        assertTrue(hand.isEmpty());
        assertEquals(0, hand.getUniqueCards(cards, 0));
    }
    
    @Test
    public void testIsFullIsEmpty() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES, Card.KING_SPADES,
                        Card.QUEEN_SPADES, Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        
                        Card.JACK_HEARTS);
        
        assertTrue(hand.isFull());
        assertFalse(hand.isEmpty());
        hand.playCard(Card.KING_SPADES);
        assertFalse(hand.isFull());
        
        hand.playCard(Card.ACE_SPADES);
        hand.playCard(Card.TEN_SPADES);
        hand.playCard(Card.KING_SPADES);
        hand.playCard(Card.QUEEN_SPADES);
        hand.playCard(Card.QUEEN_SPADES);
        hand.playCard(Card.JACK_SPADES);
        hand.playCard(Card.NINE_SPADES);
        hand.playCard(Card.QUEEN_DIAMONDS);
        hand.playCard(Card.JACK_DIAMONDS);
        hand.playCard(Card.JACK_CLUBS);
        assertFalse(hand.isFull());
        assertFalse(hand.isEmpty());

        hand.playCard(Card.JACK_HEARTS);
        assertFalse(hand.isFull());
        assertTrue(hand.isEmpty());
    }
}
