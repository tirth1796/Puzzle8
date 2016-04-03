package com.google.engedu.puzzle8;

import java.util.Comparator;

/**
 * Created by Tirth Shah on 27-03-2016.
 */
public class PuzzleBoardComparator implements Comparator<PuzzleBoard> {
    /**
     * Compares the two specified objects to determine their relative ordering. The ordering
     * implied by the return value of this method for all possible pairs of
     * {@code (lhs, rhs)} should form an <i>equivalence relation</i>.
     * This means that
     * <ul>
     * <li>{@code compare(a,a)} returns zero for all {@code a}</li>
     * <li>the sign of {@code compare(a,b)} must be the opposite of the sign of {@code
     * compare(b,a)} for all pairs of (a,b)</li>
     * <li>From {@code compare(a,b) > 0} and {@code compare(b,c) > 0} it must
     * follow {@code compare(a,c) > 0} for all possible combinations of {@code
     * (a,b,c)}</li>
     * </ul>
     *
     * @param lhs an {@code Object}.
     * @param rhs a second {@code Object} to compare with {@code lhs}.
     * @return an integer < 0 if {@code lhs} is less than {@code rhs}, 0 if they are
     * equal, and > 0 if {@code lhs} is greater than {@code rhs}.
     * @throws ClassCastException if objects are not of the correct type.
     */
    @Override
    public int compare(PuzzleBoard lhs, PuzzleBoard rhs) {
        int lhsp=lhs.priority(),rhsp=rhs.priority();
        if(lhsp>rhsp){
            return 1;
        }else if(lhsp==rhsp){
            return 0;
        }else{
            return -1;
        }
    }
}
