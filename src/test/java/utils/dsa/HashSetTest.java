package utils.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the custom HashSet implementation.
 */
public class HashSetTest {

    /**
     * Test adding elements and checking containment.
     */
    @Test
    public void testAddAndContains() {
        HashSet<String> set = new HashSet<>();
        assertTrue(set.add("apple"));
        assertTrue(set.add("banana"));
        assertTrue(set.contains("apple"));
        assertFalse(set.contains("cherry"));
    }

    /**
     * Test that adding duplicates does not increase size.
     */
    @Test
    public void testNoDuplicates() {
        HashSet<Integer> set = new HashSet<>();
        assertTrue(set.add(1));
        assertFalse(set.add(1)); // Duplicate
        assertEquals(1, set.size());
    }

    /**
     * Test removing elements.
     */
    @Test
    public void testRemove() {
        HashSet<String> set = new HashSet<>();
        set.add("cat");
        set.add("dog");
        assertTrue(set.remove("cat"));
        assertFalse(set.contains("cat"));
        assertEquals(1, set.size());
    }

    /**
     * Test clearing the set.
     */
    @Test
    public void testClear() {
        HashSet<Integer> set = new HashSet<>();
        set.add(5);
        set.add(10);
        set.clear();
        assertEquals(0, set.size());
        assertFalse(set.contains(5));
    }

    /**
     * Test size after adding elements.
     */
    @Test
    public void testSize() {
        HashSet<String> set = new HashSet<>();
        set.add("x");
        set.add("y");
        set.add("z");
        assertEquals(3, set.size());
    }
}
