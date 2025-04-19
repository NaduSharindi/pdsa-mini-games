package utils.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.dsa.LinkedList;

public class LinkedListTest {
    @Test
    void testAddAndGet() {
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty());
        list.add("A");
        list.add("B");
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    void testRemove() {
        LinkedList<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        list.remove(0);
        assertEquals(1, list.size());
        assertEquals("B", list.get(0));
    }
}

