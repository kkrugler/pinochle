package org.krugler.pinochle;


/**
 * TODO create alpha-beta pruning version (see https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)
 *  Requires keeping track of game score (pointers taken).
 *  Use a 12 entry array of "current hand" objects that's preallocated. Index by hand depth. Has cards played, array of long CardBits,
 *      who's winning(?), counters played. So make it fast to figure out valid plays, and score a hand.
 *
 */
public class PinochleGame {

    public static final int NUM_PLAYERS = 4;

    private static final long PRINT_HANDS_EVERY = 10000000L;
    
    private PinochleDeck _deck;
    private PinochleHand _hands[];
    private int[][] _plays;
    private long _handsPlayed;
    private int _trumpSuit = -1;
    private int[] _playedCards;
    
    public PinochleGame(long randSeed) {
        _deck = new PinochleDeck(randSeed);
        
        // for each play (depth in one round) we have up to 12 cards that could be played. We
        // need to keep track of these for each depth.
        _plays = new int[PinochleDeck.CARDS_IN_DECK][];
        for (int depth = 0; depth < PinochleDeck.CARDS_IN_DECK; depth++) {
            _plays[depth] = new int[PinochleHand.CARDS_IN_HAND];
        }
        
        // Keep track of cards played.
        _playedCards = new int[PinochleDeck.CARDS_IN_DECK];
    }
    
    public void deal() {
        _deck.shuffle();
        
        _hands = new PinochleHand[PinochleGame.NUM_PLAYERS];
        for (int i = 0; i < PinochleGame.NUM_PLAYERS; i++) {
            _hands[i] = new PinochleHand(_deck, i);
        }
    }
    
    public void setTrumpSuit(int trumpSuit) {
        _trumpSuit = trumpSuit;
    }
    
    public int getTrumpSuit() {
        return _trumpSuit;
    }
    
    public PinochleHand getHand(int player) {
        return _hands[player];
    }
    
    private int makePlay(int depth, int player, int alpha, int beta, int handOffset, int handCardsPlayed, int evenScore) {
        int[] plays = _plays[depth];
        int numPlays = _hands[player].getPlays(_trumpSuit, _playedCards, handOffset, handCardsPlayed, plays);
        boolean isEvenPlayer = (player % 2) == 0;
        int v = isEvenPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        for (int i = 0; i < numPlays; i++) {
            int play = plays[i];
            if (depth == 0) {
                System.out.println(String.format("Player %d starting with %s", player, Card.toString(play)));
            }
            
            // Play the selected card, and record what was played.
            _hands[player].playCard(play);
            _playedCards[handOffset + handCardsPlayed] = play;
            
            int nextPlayer;
            int handScore = 0;
            
            // If this play means we've completed the round, figure out who plays next.
            if (handCardsPlayed + 1 == PinochleGame.NUM_PLAYERS) {
                // We've played out this round, so the next player is based
                // on who won the round.
                int winningCardPos = Card.winningCardPos(_trumpSuit, _playedCards, handOffset, PinochleGame.NUM_PLAYERS);
                
                // Mapping from the winning card pos to a player index means
                // calculating the player index of the first card, adding in
                // the pos, and then handling wrap-around.
                int firstCardPlayer = (player + 1) % PinochleGame.NUM_PLAYERS;
                int winningPlayer = (firstCardPlayer + winningCardPos) % PinochleGame.NUM_PLAYERS;
                
                boolean evenTeamWon = (winningPlayer % 2) == 0;
                if (evenTeamWon) {
                    handScore = Card.numCounters(_playedCards, handOffset, PinochleGame.NUM_PLAYERS);
                    evenScore += handScore;
                }
                
                if (_hands[winningPlayer].isEmpty()) {
                    // We're all done, so log what happened in this hand?
                    if (evenTeamWon) {
                        // last trick bonus
                        evenScore += 1;
                    }
                    
                    _handsPlayed += 1;
                    
                    if ((_handsPlayed % PRINT_HANDS_EVERY) == 0) {
                        System.out.format("Finished playing hand #%d, even team score of %d\n", _handsPlayed, evenScore + handScore);
                    }
                    
                    // We have a final hand score.
                    return evenScore;
                } else {
                    nextPlayer = winningPlayer;
                    
                    // Set things up so the next play has the right values
                    handOffset += PinochleGame.NUM_PLAYERS;
                    handCardsPlayed = 0;
                }
            } else {
                nextPlayer = (player + 1) % PinochleGame.NUM_PLAYERS;
                handCardsPlayed += 1;
            }
        
            // See what happens playing this card.
            int result = makePlay(depth + 1, nextPlayer, alpha, beta, handOffset, handCardsPlayed, evenScore);
            if (isEvenPlayer) {
                v = Math.max(v, result);
                alpha = Math.max(alpha, v);
            } else {
                v = Math.min(v, result);
                beta = Math.min(beta, v);
            }
            
            if (beta <= alpha) {
                break;
            }

            // Undo the results of this play, for the next loop.
            _hands[player].unplayCard(play);
            
            evenScore -= handScore;
            handCardsPlayed -= 1;
            if (handCardsPlayed < 0) {
                handCardsPlayed = 0;
                handOffset -= PinochleGame.NUM_PLAYERS;
            }
        }
        
        return v;
    }
    
    public void play() {
        _handsPlayed = 0;
        final int alpha = Integer.MIN_VALUE;
        final int beta = Integer.MAX_VALUE;
        final int depth = 0;
        final int handOffset = 0;
        final int handCardsPlayed = 0;
        
        for (int bidder = 0; bidder < PinochleGame.NUM_PLAYERS; bidder++) {
            for (int trumpSuit : Suit.SUITS) {
                setTrumpSuit(trumpSuit);
                // Play out the game, assuming <bidder> took the bid and named <trump> as trump.
                System.out.format("Bidder #%d picked %s for trump\n", bidder, Suit.toString(trumpSuit));
                int minMaxScore = makePlay(depth, bidder, alpha, beta, handOffset, handCardsPlayed, 0);
                System.out.format("Best score = %d\n", minMaxScore);
            }
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        PinochleGame game = new PinochleGame(666L);
        
        game.deal();
        game.play();
    }

}
