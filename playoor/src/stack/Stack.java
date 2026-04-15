package stack;

import java.util.NoSuchElementException;

public interface Stack<T> {
    boolean isEmpty() throws NoSuchElementException;

    boolean isFull() throws IllegalStateException;

    //push almacena y pop saca

    void push(T item);//save

    T pop() throws NoSuchElementException; //remove

    T peek() throws NoSuchElementException;

}
