package utils.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.dsa.Stack;

public class StackTest {
    @Test
    void testPushPopPeek() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.peek());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.peek());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPopEmpty() {
        Stack<Integer> stack = new Stack<>();
        assertThrows(Exception.class, stack::pop); // adjust if your Stack throws a specific exception
    }
}
