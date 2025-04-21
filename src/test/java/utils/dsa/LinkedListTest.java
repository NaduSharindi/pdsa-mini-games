package utils.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the custom LinkedList data structure.
 */
public class LinkedListTest {

    @Test
    void testAddAndGet() {
        // Test adding elements and retrieving them by index
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty(), "List should be empty initially");
        list.add("A");
        list.add("B");
        assertEquals(2, list.size(), "List size should be 2 after adding two elements");
        assertEquals("A", list.get(0), "First element should be 'A'");
        assertEquals("B", list.get(1), "Second element should be 'B'");
    }

    @Test
    void testRemove() {
        // Test removing an element by index
        LinkedList<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        list.remove(0);
        assertEquals(1, list.size(), "List size should be 1 after removal");
        assertEquals("B", list.get(0), "Remaining element should be 'B'");
    }
}


