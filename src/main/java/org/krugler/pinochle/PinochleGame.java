package org.krugler.pinochle;

import java.util.List;

/**
 * TODO make version that uses array of CardBits for shuffling/dealing
 * TODO create alpha-beta pruning version (see https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)
 *  Requires keeping track of game score (pointers taken).
 *  Use a 12 entry array of "current hand" objects that's preallocated. Index by hand depth. Has cards played, array of long CardBits,
 *      who's winning(?), counters played. So make it fast to figure out valid plays, and score a hand.
 *
 */
public class PinochleGame {

    public static final int NUM_PLAYERS = 4;
    
    private PinochleDeck _deck;
    private PinochleHand _hands[];
    private long _handsPlayed;
    
    public PinochleGame() {
        _deck = new PinochleDeck();
    }
    
    public void deal() {
        _deck.shuffle();
        
        _hands = new PinochleHand[PinochleGame.NUM_PLAYERS];
        for (int i = 0; i < PinochleGame.NUM_PLAYERS; i++) {
            _hands[i] = new PinochleHand(_deck.getCards(i * PinochleHand.CARDS_IN_HAND, PinochleHand.CARDS_IN_HAND));
        }
    }
    
    private void makePlay(Suit trump, int depth, int player, Card... cards) {
        List<Card> plays = _hands[player].getPlays(trump, cards);
        
        // We'll need to keep track of what was played
        Card[] nextPlay = new Card[cards.length + 1];
        System.arraycopy(cards, 0, nextPlay, 0, cards.length);

        for (Card play : plays) {
            if (_hands[player].isFull()) {
                System.out.println(String.format("Player %d starting with %s (%s is trump)", player, play, trump));
            }
            
            // Play the selected card, and record what was played.
            _hands[player].playCard(play);
            nextPlay[cards.length] = play;
            
            // If this play means we've completed the round, figure out who plays next.
            if (cards.length + 1 == PinochleGame.NUM_PLAYERS) {
                // We've played out this round, so the next player is based
                // on who won the round.
                int winningCardPos = Card.winningCardPos(trump, nextPlay);
                
                // Mapping from the winning card pos to a player index means
                // calculating the player index of the first card, adding in
                // the pos, and then handling wrap-around.
                int firstCardPlayer = (player + 1) % PinochleGame.NUM_PLAYERS;
                int winningPlayer = (firstCardPlayer + winningCardPos) % PinochleGame.NUM_PLAYERS;
                
                if (_hands[winningPlayer].isEmpty()) {
                    // We're all done, so log what happened in this hand?
                    _handsPlayed += 1;
                    if ((_handsPlayed % 1000000) == 0) {
                        System.out.println("Finished playing hand #" + _handsPlayed);
                    }
                } else {
                    // And now we can have this player pick the card to lead
                    makePlay(trump, depth + 1, winningPlayer);
                }
            } else {
                int nextPlayer = (player + 1) % PinochleGame.NUM_PLAYERS;
                makePlay(trump, depth + 1, nextPlayer, nextPlay);
            }
        
            _hands[player].unplayCard(play);
        }
    }
    
    public void play() {
        _handsPlayed = 0;
        for (int bidder = 0; bidder < PinochleGame.NUM_PLAYERS; bidder++) {
            for (Suit trump : Suit.values()) {
                // Play out the game, assuming <bidder> took the bid and named <trump> as trump.
                System.out.println(String.format("Bidder #%d picked %s for trump", bidder, trump));
                makePlay(trump, 1, bidder);
            }
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        PinochleGame game = new PinochleGame();
        
        game.deal();
        game.play();
    }

}
