package org.krugler.pinochle;

import static org.junit.Assert.*;

import org.junit.Test;

public class PinochleGameTest {

    @Test
    public void test() {
        PinochleGame game = new PinochleGame(666L);
        game.deal();
        
        for (int i = 0; i < PinochleGame.NUM_PLAYERS; i++) {
            PinochleHand hand = game.getHand(i);
            
            int topMeld = 0;
            int topSuit = Suit.NO_TRUMP;
            for (int suit : Suit.SUITS) {
                int meld = PinochleMeld.calcMeld(hand, suit);
                if (meld > topMeld) {
                    topMeld = meld;
                    topSuit = suit;
                }
            }
            
            System.out.format("Player %d [%d|%s]: %s\n", i, topMeld, Suit.toString(topSuit), hand.toString());
        }
    }

}
