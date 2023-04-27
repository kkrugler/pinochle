package org.krugler.pinochle;

public class Rank {
    
    public static final int NUM_RANKS = 6;
    
    public static final int NINE    = 0x001; 
    public static final int JACK    = 0x002;
    public static final int QUEEN   = 0x004;
    public static final int KING    = 0x008;
    public static final int TEN     = 0x010;
    public static final int ACE     = 0x020;

    public static final int FIRST   = NINE;
    public static final int LAST    = ACE;
    
    public static final int[] RANKS = {ACE, TEN, KING, QUEEN, JACK, NINE};
    
    public static int getRankFromCard(int card) {
        int shiftAmount = BitUtil.ntz(card);
        return 1 << (shiftAmount % NUM_RANKS);
    }
    
    public static String toString(int rank) {
        if (rank == NINE) {
            return "NINE";
        } else if (rank == JACK) {
            return "JACK";
        } else if (rank == QUEEN) {
            return "QUEEN";
        } else if (rank == KING) {
            return "KING";
        } else if (rank == TEN) {
            return "TEN";
        } else if (rank == ACE) {
            return "ACE";
        } else {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
    }
    
    public static String toShortString(int rank) {
        if (rank == NINE) {
            return "9";
        } else if (rank == JACK) {
            return "J";
        } else if (rank == QUEEN) {
            return "Q";
        } else if (rank == KING) {
            return "K";
        } else if (rank == TEN) {
            return "T";
        } else if (rank == ACE) {
            return "A";
        } else {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
    }

}
