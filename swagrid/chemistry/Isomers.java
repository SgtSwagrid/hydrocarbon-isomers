package swagrid.chemistry;

import java.util.Arrays;
import java.math.BigInteger;
import static java.math.BigInteger.*;

import swagrid.math.Partitioner;
import swagrid.math.Partitioner.Partition;
import static swagrid.math.Combinatorics.*;

/**
 * Hydrocarbon isomer counter.
 * @author Alec Dorrington
 */
public class Isomers {
    
    /**
     * Determines the number of unrooted trees of a given size.
     * @param vertices the total number of vertices.
     * @param degree the maximum degree for each vertex.
     * @return the number of permutations for this tree.
     */
    public static BigInteger treePermutations(int vertices, int degree) {
        
        //Cache for storing repeated sub-solutions.
        BigInteger[] cache = new BigInteger[vertices/2+1];
        Arrays.fill(cache, ZERO);
        BigInteger permutations = ZERO;
        
        //For each partition where no part exceeds half the total number of vertices.
        //(This condition ensures completeness and uniqueness.)
        for(Partition partition : new Partitioner(vertices-1, degree, (vertices-1)/2)) {
            
            BigInteger p = ONE;
            //For each size of subtree in this partition.
            for(int[] val : partition) {
                
                //Get the number of permutations for this size subtree.
                BigInteger r = rootedTrees(cache, val[0], degree-1);
                //Account for multiplicity using multiset combinations.
                p = p.multiply(multisets(r, valueOf(val[1])));
            }
            //Permutations of each partition sum to total permutations.
            permutations = permutations.add(p);
        }
        
        //Account for trees composed of two equal-sized subtrees with linked roots.
        if(vertices % 2 == 0) {
            BigInteger r = rootedTrees(cache, vertices/2, degree-1);
            permutations = permutations.add(multisets(r, valueOf(2)));
        }
        return permutations;
    }
    
    /**
     * Determines the number of rooted trees of a given size.
     * @param cache storage for repeated sub-solutions.
     * @param vertices the number of vertices in this subtree.
     * @param branching the maximum branching factor for each vertex.
     * @return the number of permutations for this tree.
     */
    public static BigInteger rootedTrees(BigInteger[] cache, int vertices, int branching) {
        
        if(vertices <= 2) return ONE;
        //Check if this value has already been computed.
        if(cache[vertices].compareTo(ZERO)>0) return cache[vertices];
        BigInteger permutations = ZERO;
        
        //For each possible partition of the remaining vertices.
        for(Partition partition : new Partitioner(vertices-1, branching)) {
            
            BigInteger p = ONE;
            //For each size of subtree in this partition.
            for(int[] val : partition) {
                
                //Get the number of permutations for this size subtree.
                BigInteger r = rootedTrees(cache, val[0], branching);
                //Account for multiplicity using multiset combinations.
                p = p.multiply(multisets(r, valueOf(val[1])));
            }
            //Permutations of each partition sum to total permutations.
            permutations = permutations.add(p);
        }
        cache[vertices] = permutations;
        return permutations;
    }
}
