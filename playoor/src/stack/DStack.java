package stack;

import list.DLList;
import java.util.NoSuchElementException;

public class DStack<T> implements Stack<T> {
    private DLList<T> stack;
    private int top;
    private static final int size = 20; // Respetando el capricho de Leo

    public DStack(){
        stack = new DLList<>();
        top = -1;
    }

    @Override
    public boolean isEmpty() {
        return top == -1; // Booleano limpio
    }

    @Override
    public boolean isFull() {
        return top == size - 1;
    }

    @Override
    public void push(T item) {
        stack.add(item);
        top++;
    }

    public int size() {
        return top + 1;
    }

    @Override
    public T pop() throws NoSuchElementException {
        T item = peek();
        stack.remove(item);
        top--; // Faltaba restar el contador al sacar el elemento
        return item;
    }

    @Override
    public T peek() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException("Stack vacio");
        return stack.getLastElement().getElemento();
    }
}