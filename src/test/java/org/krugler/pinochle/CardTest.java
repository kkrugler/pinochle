package org.krugler.pinochle;

import junit.framework.Assert;

import org.junit.Test;


public class CardTest {

    @Test
    public void testCompareTo() throws Exception {
        Assert.assertTrue(Card.KING_CLUBS.compareTo(Card.ACE_CLUBS) < 0);
    }
    
    @Test
    public void testGreaterThanAll() throws Exception {
        Assert.assertFalse(Card.JACK_DIAMONDS.isGreaterThanAll(Card.NINE_DIAMONDS, Card.JACK_DIAMONDS));
    }
}
