package swagrid.test;

import java.math.BigInteger;
import static java.math.BigInteger.*;

import static swagrid.chemistry.Isomers.*;

public class Test {
    
    /** Number of carbon atoms. */
    public static final int VERTICES = 20;
    
    /** Maximum number of bonds per carbon. */
    public static final int MAX_DEGREE = 4;
    
    /** Number of times to repeat the calculation. */
    public static final int TRIALS = 100000;
    
    public static void main(String[] args) {
        
        BigInteger permutations = ZERO;
        
        long t0 = System.nanoTime();
        for(int i = 0; i < TRIALS; i++) {
            permutations = treePermutations(VERTICES, MAX_DEGREE);
        }
        long dt = (System.nanoTime() - t0) / TRIALS / 1000;
        
        System.out.println(permutations + " Permutations (" + dt + "us)");
    }
}
