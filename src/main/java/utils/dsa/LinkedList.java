package utils.dsa;


public class LinkedList<T> {
	
	private Node<T> head;
	private int size;
	
	private static class Node<T> {
		
		T data;
		Node<T> next;
		
		Node(T data) {
			
			this.data = data;
			this.next = null;
		}
	}
	
	public LinkedList() {
		this.head = null;
		this.size = 0;
	}
	
    /**
     * Adds an element to end of the list.
     *
     * @param data - element to add
     */
	
    public	void add(T data) {
    	
    	Node<T> newNode = new Node<>(data);
    	if(head == null) {
    		head = newNode;
    	} else {
    		Node<T> current = head;
    		while(current.next != null) {
    			
    			current = current.next;
    		}
    		
    		current.next = newNode;
    	}
    	
    	size++;
    	
    }
    
    /**
     * Adds an element to the beginning of the list.
     *
     * @param data - element to add
     */
   
    public void addFirst(T data) {
    	Node<T> newNode = new Node<>(data);
    	newNode.next = head;
    	head = newNode;
    	size ++;
    }
    
    /**
     * Adds an element at a specific index in the list.
     *
     * @param index - index at which to insert the element
     * @param data - element to add
     * @throws IndexOutOfBoundsException 
     */
    
    public void add(int index, T data) {
    	
    	if(index < 0 || index > size) {
    		
    		throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    	}
    	
    	if(index == 0) {
    		
    		addFirst(data);
    		return;
    	}
    	
    	Node<T> newNode = new Node<>(data);
    	Node<T> current = head;
    	
    	for(int i = 0; i < index - 1; i++) {
    		
    		current = current.next;
    	}
    	
    	newNode.next = current.next;
    	current.next = newNode;
    	size++;
    }
    
    /**
     * Retrieves element at the specified index.
     *
     * @param index - index of element to retrieve
     * @return  element at specified index
     * @throws IndexOutOfBoundsException
     */
    
    public T get(int index) {
    	if(index < 0 || index >= size) {
    		
    		throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    	}
    	
    	Node<T> current = head;
    	
    	for(int i = 0; i < index; i++) {
    		
    		current = current.next;
    	}
    	
    	return current.data;
    }
    
    /**
     * Removes first occurrence of specified element from the list.
     *
     * @param data - element to remove
     * @return true - if  element was found and removed, false otherwise.
     */
    
    public boolean remove(T data) {
    	
    	if(head == null) {
    		
    		return false;
    	}
    	
    	if(head.data.equals(data)) {
    		head = head.next;
    		size--;
    		return true;
    	}
    	
    	Node<T> current = head;
    	while(current.next != null && !current.next.data.equals(data)) {
    		
    		current = current.next;
    	}
    	
    	if(current.next != null) {
    		
    		current.next = current.next.next;
    		size--;
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Removes element at the specified index.
     *
     * @param index - index of element to remove
     * @return removed element.
     * @throws IndexOutOfBoundsException
     */
    
    public T remove(int index) {
    	if(index < 0 || index >= size) {
    		
    		throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);    		
    	}
    	
    	if(index == 0) {
    		
    		T removedData = head.data;
    		head = head.next;
    		size--;
    		return removedData;
    	}
    	
    	Node<T> current = head;
    	
    	for(int i = 0; i < index - 1; i++) {
    		
    		current = current.next;
    	}
    	
    	T removedData = current.next.data;
    	current.next = current.next.next;
    	size--;
    	return removedData;
    }
    
    /**
     * Returns number of elements in the list.
     *
     * @return 
     */
    
    public int size() {
    	
    	return size;
    }
    
    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    
    public boolean isEmpty() {
    	
    	return size == 0;
    }
    
    /**
     * Returns a string representation of the list.
     *
     * @return
     */
    
    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder("[");
    	Node<T> current = head;
    	
    	while(current != null) {
    		
    		sb.append(current.data);
    		
    		if(current.next != null) {
    			
    			sb.append(", ");
    		}
    		
    		current = current.next;
    	}
    	
    	sb.append("]");
    	return sb.toString();
    }
    
    
}
