package swagrid.math;

import java.math.BigInteger;
import static java.math.BigInteger.*;

/**
 * Basic combinatoric functions using BigInteger.
 * 
 * @author Alec Dorrington
 */
public class Combinatorics {
    
    /**
     * @param n the number of unique items from which to select with replacement.
     * @param k the number of items which are chosen without order.
     * @return the number of multisets.
     */
    public static BigInteger multisets(BigInteger n, BigInteger k) {
        return combinations(n.add(k).subtract(ONE), k);
    }
    
    /**
     * @param n the number of unique items from which to select without replacement.
     * @param k the number of items which are chosen without order.
     * @return the number of combinations.
     */
    public static BigInteger combinations(BigInteger n, BigInteger k) {
        return permutations(n, k).divide(factorial(k));
    }
    
    /**
     * @param n the number of unique items from which to select without replacement.
     * @param k the number of items which are chosen with order.
     * @return the number of permutations.
     */
    public static BigInteger permutations(BigInteger n, BigInteger k) {
        BigInteger c = ONE;
        for(BigInteger i=n.subtract(k).add(ONE);
                i.compareTo(n)<=0; i=i.add(ONE)) {
            c = c.multiply(i);
        }
        return c;
    }
    
    /**
     * @return n! = n * (n-1) * ... * 2 * 1
     */
    public static BigInteger factorial(BigInteger n) {
        BigInteger f = ONE;
        for(BigInteger i=ONE; i.compareTo(n)<=0; i=i.add(ONE)) {
            f = f.multiply(i);
        }
        return f;
    }
}
