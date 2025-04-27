package utils.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the custom Stack data structure.
 */
public class StackTest {

    @Test
    void testPushPopPeek() {
        // Create a new stack and test push/pop/peek operations
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.isEmpty(), "Stack should be empty initially");
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.peek(), "Peek should return last pushed value");
        assertEquals(20, stack.pop(), "Pop should return last pushed value");
        assertEquals(10, stack.peek(), "Peek should return the remaining value");
        assertEquals(10, stack.pop(), "Pop should return the remaining value");
        assertTrue(stack.isEmpty(), "Stack should be empty after popping all elements");
    }

    @Test
    void testPopEmpty() {
        // Test popping from an empty stack throws an exception
        Stack<Integer> stack = new Stack<>();
        assertThrows(Exception.class, stack::pop, "Pop on empty stack should throw exception");
    }
}

