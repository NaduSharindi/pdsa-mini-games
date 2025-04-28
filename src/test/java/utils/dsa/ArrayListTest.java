package utils.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the custom ArrayList implementation.
 */
public class ArrayListTest {

    /**
     * Test adding elements and getting them by index.
     */
    @Test
    public void testAddAndGet() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    /**
     * Test removing elements by index.
     */
    @Test
    public void testRemove() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        int removed = list.remove(1);
        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    /**
     * Test that getting an invalid index throws an exception.
     */
    @Test
    public void testGetInvalidIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("X");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
    }

    /**
     * Test that removing an invalid index throws an exception.
     */
    @Test
    public void testRemoveInvalidIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Y");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    /**
     * Test isEmpty and size methods.
     */
    @Test
    public void testIsEmptyAndSize() {
        ArrayList<Double> list = new ArrayList<>();
        assertTrue(list.isEmpty());
        list.add(1.1);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    /**
     * Test toString output.
     */
    @Test
    public void testToString() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        assertEquals("[1, 2]", list.toString());
    }
}
