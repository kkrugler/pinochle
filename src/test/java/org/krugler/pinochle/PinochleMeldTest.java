package org.krugler.pinochle;

import junit.framework.Assert;

import org.junit.Test;


public class PinochleMeldTest {

    @Test
    public void testSimpleMeld() throws Exception {
        PinochleHand hand = new PinochleHand(
                                Card.ACE_CLUBS,
                                Card.ACE_DIAMONDS,
                                Card.ACE_HEARTS,
                                
                                Card.TEN_CLUBS,
                                Card.TEN_DIAMONDS,
                                Card.TEN_HEARTS,
                                
                                Card.KING_CLUBS,
                                Card.KING_DIAMONDS,
                                Card.KING_HEARTS,
                                
                                Card.JACK_CLUBS,
                                Card.JACK_DIAMONDS,
                                Card.JACK_HEARTS);
        
        Assert.assertEquals(0, PinochleMeld.totalScore(hand, Suit.CLUBS));
        
        hand.replaceCard(Card.ACE_HEARTS, Card.NINE_CLUBS);
        Assert.assertEquals(1, PinochleMeld.totalScore(hand, Suit.CLUBS));
        Assert.assertEquals(0, PinochleMeld.totalScore(hand, Suit.HEARTS));
        
        hand.replaceCard(Card.ACE_DIAMONDS, Card.NINE_CLUBS);
        Assert.assertEquals(2, PinochleMeld.totalScore(hand, Suit.CLUBS));
        Assert.assertEquals(0, PinochleMeld.totalScore(hand, Suit.HEARTS));

        hand.replaceCard(Card.TEN_CLUBS, Card.QUEEN_CLUBS);
        Assert.assertEquals(6, PinochleMeld.totalScore(hand, Suit.CLUBS));
        Assert.assertEquals(2, PinochleMeld.totalScore(hand, Suit.HEARTS));
    }
    
    @Test
    public void testRun() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_CLUBS,
                        Card.TEN_CLUBS,
                        Card.KING_CLUBS,
                        Card.QUEEN_CLUBS,
                        Card.JACK_CLUBS,

                        Card.ACE_DIAMONDS,
                        Card.ACE_HEARTS,
                        
                        Card.TEN_DIAMONDS,
                        Card.TEN_HEARTS,
                        
                        Card.KING_DIAMONDS,
                        Card.KING_HEARTS,
                        
                        Card.JACK_DIAMONDS);

        Assert.assertEquals(15, PinochleMeld.totalScore(hand, Suit.CLUBS));
        Assert.assertEquals(2, PinochleMeld.totalScore(hand, Suit.HEARTS));
    }

    @Test
    public void testDoubleRun() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_CLUBS,
                        Card.TEN_CLUBS,
                        Card.KING_CLUBS,
                        Card.QUEEN_CLUBS,
                        Card.JACK_CLUBS,

                        Card.ACE_CLUBS,
                        Card.TEN_CLUBS,
                        Card.KING_CLUBS,
                        Card.QUEEN_CLUBS,
                        Card.JACK_CLUBS,

                        Card.ACE_DIAMONDS,
                        Card.ACE_HEARTS);

        Assert.assertEquals(150, PinochleMeld.totalScore(hand, Suit.CLUBS));
        Assert.assertEquals(4, PinochleMeld.totalScore(hand, Suit.HEARTS));
    }

    @Test
    public void testComplexMix() throws Exception {
        PinochleHand hand = new PinochleHand(
                        Card.ACE_SPADES,
                        Card.TEN_SPADES,
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        Card.JACK_SPADES,
                        Card.NINE_SPADES,
                        
                        Card.KING_SPADES,
                        Card.QUEEN_SPADES,
                        
                        Card.JACK_DIAMONDS,
                        Card.JACK_DIAMONDS,
                        
                        Card.JACK_CLUBS,
                        Card.JACK_HEARTS);

        Assert.assertEquals(28, PinochleMeld.totalScore(hand, Suit.SPADES));
        Assert.assertEquals(12, PinochleMeld.totalScore(hand, Suit.HEARTS));
    }
}
