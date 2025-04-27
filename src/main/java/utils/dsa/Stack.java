package utils.dsa;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class Stack<T> {
	
	private ArrayList<T> items;
	
	public Stack() {
		
		items = new ArrayList<>();
		
	}
	
	/**
     * Pushes an item onto the top of this stack.
     *
     * @param  item to be pushed onto this stack.
     */
	
	public void push(T item) {
		items.add(item);
	}
	
    /**
     * Removes  object at  top of this stack and returns that
     * object as value of this function.
     *
     * @return   
     * @throws EmptyStackException 
     */
	
	public T pop() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return items.remove(items.size() - 1);
	}
	
    /**
     * Removes all of the elements.
     * .
     */
    public void clear() {
        items.clear();
    }
	
    /**
     * Looks at object at top of this stack without removing it
     *
     * @return 
     * @throws EmptyStackException 
     */
	
	public T peek() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return items.get(items.size() - 1);
	}
	
	
    /**
     * Tests if this stack is empty.
     * 
     *@return
     */
			
    public boolean isEmpty() {
		return items.isEmpty();
	} 			
	
    /**
     * Returns number of items in this stack.
     *
     * @return 
     */
			
    public int size() {
		return items.size();
	}			

    /**
     * Returns a string representation of this stack.
     *
     * @return 
     */
		
  @Override
  
  public String toString() {
		return "Stack{" +
               "items=" + items +
               '}';
	}
}
