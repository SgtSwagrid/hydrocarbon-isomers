package swagrid.math;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import swagrid.math.Partitioner.Partition;

/**
 * Tool for enumerating integer partitions.<br>
 * Generates every possible partition exactly once.<br>
 * Partition generation is unordered. To be used in a for-each loop.<br>
 * <br>
 * An integer partition of X is a multiset of positive integers which sum to X.
 * 
 * @author Alec Dorrington
 */
public class Partitioner implements Iterable<Partition> {
    
    private int sum;
    /** The value to which the parts must sum. */
    public int getSum() { return sum; }
    
    private int bins;
    /** The maximum number of parts. */
    public int getBins() { return bins; }
    
    private int max;
    /** The maximum part value. */
    public int getMax() { return max; }
    
    /**
     * @param sum the value to which the parts must sum.
     * @param bins the maximum number of parts.
     * @param max the maximum part value.
     */
    public Partitioner(int sum, int bins, int max) {
        this.sum = sum;
        this.bins = bins < sum ? bins : sum;
        this.max = max < sum ? max : sum;
    }
    
    /**
     * Default: no maximum value size.
     * @param sum the value to which the parts must sum.
     * @param bins the maximum number of parts.
     */
    public Partitioner(int sum, int bins) { this(sum, bins, sum); }
    
    /**
     * Default: no maximum number of values or value size.
     * @param sum the value to which the parts must sum.
     */
    public Partitioner(int sum) { this(sum, sum, sum); }
    
    /**
     * An alternative to iterator() for use when re-use of Partition
     * instances is not acceptable, particularly during parallelization.
     * @return a list of all possible partitions.
     */
    public List<Partition> toList() {
        
        //Iterate over and copy each partition into a list.
        List<Partition> partitions = new LinkedList<>();
        for(Partition p : this) {
            partitions.add(p.clone());
        }
        return Collections.unmodifiableList(partitions);
    }
    
    /**
     * Provides an iterator over all possible partitions.<br>
     * Note: partition instances are re-used throughout the iteration.
     */
    @Override
    public Iterator<Partition> iterator() {
        return new PartitionIterator(new Partition());
    }
    
    /**
     * Representation of a single integer partition.<br>
     * Values are given in increasing order. To be used in a for-each loop.<br>
     * Each value in a partition is given as {value, multiplicity}.<br>
     * The same instance is re-used for multiple partitions throughout an iteration.
     */
    public class Partition implements Iterable<int[]> {
        
        /** The list of values and multiplicities for a partition. */
        private LinkedList<int[]> values = new LinkedList<>();
        
        private int size = 0;
        /** The number of values in this partition, with multiplicity. */
        public int getSize() { return size; }
        
        /**
         * Advance to the next possible partition.
         * @return whether another partition exists.
         */
        private boolean increment() {
            
            //The total value removed from the partition, to be re-added.
            int sum = 0;
            //The maximum value which may be taken by any of the re-added parts.
            int max = 1;
            //Whether all parts of the tied lowest value are to be removed on the next iteration.
            //True if max bins is exceeded and removing one such part didn't fix it.
            boolean removeLowest = values.getFirst()[0] == 1;
            
            do {
                
                //Remove all parts tied for lowest value and add them to the sum.
                if(removeLowest) {
                    sum += values.getFirst()[0] * values.getFirst()[1];
                    size -= values.getFirst()[1];
                    values.removeFirst();
                }
                
                //No further partitions exist.
                if(values.isEmpty()) return false;
                
                //Remove one part of the next lowest value and add it to the sum.
                sum += values.getFirst()[0];
                //All re-added parts must be smaller than this one.
                max = values.getFirst()[0] - 1;
                //If any equal parts remain, remove all of them on the next iteration.
                removeLowest = --values.getFirst()[1] > 0;
                if(!removeLowest) values.removeFirst();
                size--;
                
            //While sum couldn't possibly fit in the remaining bins.
            } while((sum-1)/max+1 > bins - size);
            
            //Replace removed parts using the largest possible values.
            distribute(sum, max);
            return true;
        }
        
        /**
         * Append elements to start of list using largest possible values.
         * @param sum the total value to be added.
         * @param max the maximum value size.
         */
        private void distribute(int sum, int max) {
            
            //While there remains value to be added.
            while(sum > 0) {
                if(sum / max > 0) {
                    //Add as many parts of this size as possible.
                    values.addFirst(new int[] {max, sum / max});
                    size += sum / max;
                }
                sum %= max--;
            }
        }
        
        /** The partitioner from which this partition was generated. */
        public Partitioner getPartitioner() { return Partitioner.this; }
        
        /**
         * Provides an iterator over the values in this partition.<br>
         * Each value is given as an array containing {value, multiplicity}.
         */
        @Override
        public Iterator<int[]> iterator() { return values.iterator(); }
        
        @Override
        public Partition clone() {
            //Deep copy this partition.
            Partition p = new Partition();
            values.stream().map(int[]::clone).forEach(p.values::add);
            p.size = size;
            return p;
        }
        
        @Override
        public String toString() {
            return values.stream().map(Arrays::toString)
                    .collect(Collectors.joining(", "));
        }
    }
    
    private class PartitionIterator implements Iterator<Partition> {
        
        /** The partition instance used for iterating. */
        private Partition partition;
        
        /** Whether any subsequent partitions exist. */
        private boolean hasNext = sum <= bins * max;
        
        /** Whether the partition has yet been incremented on this iteration. */
        private boolean incremented = true;
        
        /**
         * @param partition the partition instance used for iterating.
         */
        private PartitionIterator(Partition partition) {
            this.partition = partition;
            //Create initial partition.
            partition.distribute(sum, max);
        }

        @Override
        public boolean hasNext() {
            if(!incremented) {
                //Attempt to increment the partition.
                hasNext = partition.increment();
                incremented = true;
            }
            return hasNext;
        }

        @Override
        public Partition next() {
            if(!hasNext()) throw new NoSuchElementException();
            incremented = false;
            return partition;
        }
    }
}
