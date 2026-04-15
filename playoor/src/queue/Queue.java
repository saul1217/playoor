package queue;

import java.util.NoSuchElementException;

public interface Queue<T> {

    void enqueue(T element);
    T dequeue() throws NoSuchElementException;
    T front() throws NoSuchElementException;

    boolean isEmpty() throws NoSuchElementException;

    boolean isFull() throws NoSuchElementException;

    int size();


}
