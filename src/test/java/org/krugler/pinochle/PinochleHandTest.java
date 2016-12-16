package org.krugler.pinochle;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;


public class PinochleHandTest {

    @Test
    public void testFirstCard() throws Exception {
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

        List<Card> plays = hand.getPlays(Suit.SPADES);
        Assert.assertEquals(12, plays.size());
        
        PinochleHand hand2 = new PinochleHand(plays);
        Assert.assertTrue(hand.equals(hand2));
    }
    
    @Test
    public void testNoDuplicates() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES);
                        
        List<Card> plays = hand.getPlays(Suit.SPADES);
        Assert.assertEquals(6, plays.size());

        Set<Card> uniqueCards = new HashSet<Card>(plays);
        Assert.assertEquals(6, uniqueCards.size());
    }
    
    @Test
    public void testSimpleCase() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES, Card.KING_SPADES,
                        Card.QUEEN_SPADES, Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.JACK_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        
                        Card.JACK_HEARTS);
        
        List<Card> plays = hand.getPlays(Suit.SPADES, Card.QUEEN_CLUBS);
        Assert.assertEquals(1, plays.size());
        Assert.assertEquals(Card.JACK_CLUBS, plays.get(0));
    }
    
    @Test
    public void testLeadTrumped() throws Exception {
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
        List<Card> plays = hand.getPlays(Suit.SPADES, Card.NINE_HEARTS, Card.NINE_SPADES, Card.JACK_SPADES);
        Assert.assertEquals(4, plays.size());
        Collections.sort(plays);
        Assert.assertEquals(Card.QUEEN_SPADES, plays.get(0));
        Assert.assertEquals(Card.KING_SPADES, plays.get(1));
        Assert.assertEquals(Card.TEN_SPADES, plays.get(2));
        Assert.assertEquals(Card.ACE_SPADES, plays.get(3));
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
        List<Card> plays = hand.getPlays(Suit.HEARTS, Card.NINE_HEARTS);
        Assert.assertEquals(12, plays.size());
    }
    
    @Test
    public void testPlayingHigherCard() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        
                        Card.QUEEN_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        
                        Card.JACK_HEARTS);
        
        List<Card> plays = hand.getPlays(Suit.SPADES, Card.NINE_DIAMONDS, Card.JACK_DIAMONDS);
        Assert.assertEquals(1, plays.size());
        Assert.assertEquals(Card.QUEEN_DIAMONDS, plays.get(0));
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
        Assert.assertEquals(0, hand.countCards(Card.ACE_SPADES));
        
        hand.playCard(Card.KING_SPADES);
        Assert.assertEquals(1, hand.countCards(Card.KING_SPADES));
        hand.playCard(Card.KING_SPADES);
        Assert.assertEquals(0, hand.countCards(Card.KING_SPADES));
        
        hand.unplayCard(Card.ACE_SPADES);
        Assert.assertEquals(1, hand.countCards(Card.ACE_SPADES));
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
        
        Assert.assertTrue(hand.isFull());
        Assert.assertFalse(hand.isEmpty());
        hand.playCard(Card.KING_SPADES);
        Assert.assertFalse(hand.isFull());
        
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
        Assert.assertFalse(hand.isFull());
        Assert.assertFalse(hand.isEmpty());

        hand.playCard(Card.JACK_HEARTS);
        Assert.assertFalse(hand.isFull());
        Assert.assertTrue(hand.isEmpty());
    }
}
