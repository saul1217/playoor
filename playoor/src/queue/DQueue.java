package queue;

import list.CDLlist;
import list.DLList;

import java.util.NoSuchElementException;

public class DQueue <T> implements Queue<T> {

    private DLList<T> queue;
    private int size;

    public DQueue(){
        this.queue = new DLList<>();
        this.size = 0;
    }

    @Override
    public void enqueue(T element) {
        queue.add(element);
        size++;
    }

    @Override
    public T dequeue() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException("La cola está vacía");
        T element = front();
        queue.remove(element); // Quitamos el elemento de la lista
        size--;
        return element;
    }

    @Override
    public T front() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException("La cola está vacía");
        return queue.getElementAt(0).getElemento();
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Debe retornar un booleano real, no lanzar excepción
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int size() {
        return size; // Ahora sí retorna el tamaño real
    }
}