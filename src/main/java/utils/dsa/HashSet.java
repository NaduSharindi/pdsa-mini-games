package utils.dsa;

import java.util.LinkedList;

/**
 * A simple generic HashSet implementation using separate chaining.
 * Stores unique elements only.
 * @param <T> the type of elements maintained by this set
 */
public class HashSet<T> {
    private static final int DEFAULT_CAPACITY = 16;
    private LinkedList<T>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashSet() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Computes the bucket index for a given element.
     */
    private int getBucketIndex(T key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * @param key element to be added
     * @return true if this set did not already contain the specified element
     */
    public boolean add(T key) {
        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        if (!buckets[index].contains(key)) {
            buckets[index].add(key);
            size++;
            return true;
        }
        return false;
    }

    /**
     * Returns true if this set contains the specified element.
     * @param key element whose presence in this set is to be tested
     */
    public boolean contains(T key) {
        int index = getBucketIndex(key);
        if (buckets[index] == null) return false;
        return buckets[index].contains(key);
    }

    /**
     * Removes the specified element from this set if present.
     * @param key element to be removed
     * @return true if this set contained the specified element
     */
    public boolean remove(T key) {
        int index = getBucketIndex(key);
        if (buckets[index] == null) return false;
        boolean removed = buckets[index].remove(key);
        if (removed) size--;
        return removed;
    }

    /**
     * Removes all of the elements from this set.
     */
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) buckets[i].clear();
        }
        size = 0;
    }

    /**
     * Returns the number of elements in this set.
     */
    public int size() {
        return size;
    }
}
