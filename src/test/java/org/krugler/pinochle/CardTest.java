package org.krugler.pinochle;

import junit.framework.Assert;

import org.junit.Test;


public class CardTest {

    @Test
    public void testCompare() throws Exception {
        Assert.assertTrue(Card.compare(Card.KING_CLUBS, Card.ACE_CLUBS) < 0);
        Assert.assertTrue(Card.compare(Card.KING_SPADES, Card.NINE_SPADES) > 0);
        Assert.assertTrue(Card.compare(Card.JACK_DIAMONDS, Card.JACK_DIAMONDS) == 0);
        
        try {
            Card.compare(Card.JACK_DIAMONDS, Card.JACK_SPADES);
            Assert.fail("Should have thrown exception, since suits are different");
        } catch (Exception e) {
            // expected.
        }
        
        try {
            Card.compare(0, Card.JACK_SPADES);
            Assert.fail("Should have thrown exception, since first card is invalid");
        } catch (Exception e) {
            // expected.
        }
        
        try {
            Card.compare(Card.JACK_DIAMONDS, 0);
            Assert.fail("Should have thrown exception, since second card is invalid");
        } catch (Exception e) {
            // expected.
        }
        
        try {
            Card.compare(Card.JACK_DIAMONDS, 1 << Suit.NO_TRUMP);
            Assert.fail("Should have thrown exception, since second card is invalid");
        } catch (Exception e) {
            // expected.
        }
    }
    
    @Test
    public void testCounters() {
        int[] playedCards = new int[] {Card.NINE_CLUBS, Card.QUEEN_CLUBS, Card.JACK_SPADES};
        Assert.assertEquals(0, Card.numCounters(playedCards, 0, 3));
        
        playedCards = new int[] {Card.NINE_CLUBS, Card.QUEEN_CLUBS, Card.ACE_DIAMONDS, Card.TEN_CLUBS, Card.ACE_HEARTS};
        Assert.assertEquals(0, Card.numCounters(playedCards, 0, 2));
        Assert.assertEquals(1, Card.numCounters(playedCards, 0, 3));
        Assert.assertEquals(2, Card.numCounters(playedCards, 0, 4));

        Assert.assertEquals(1, Card.numCounters(playedCards, 4, 1));
    }
    
}
