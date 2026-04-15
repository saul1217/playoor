package list;

import nodo.Nodo;

import java.util.NoSuchElementException;

public class DLList <T>{
    //Usamos dos centinelas, uno al inicio y otro al final
    private Nodo<T> head;
    private Nodo<T> tail;
    private long indice;


    //Constructor con un nodo
    public DLList(Nodo<T> nodo){
        this.head = new Nodo<>();
        this.tail = new Nodo<>();
        if (nodo == null) return; // la lista no se modifica
        //se conecta el primer nodo entre la cabeza y la cola
        head.setRight(nodo);
        tail.setLeft(nodo);
        indice++;
    }


    public DLList(){
        this(null);
    }

    //extra para verificar nodos vacios o con " " unicamente
    private void nodoVacio(Nodo<T>nodo){
        if(nodo == null || nodo.getElemento() == null )throw new NoSuchElementException("nodo vacio");
        if (nodo.getElemento() instanceof String) {
            String texto = (String) nodo.getElemento();
            if (texto.trim().isEmpty()) {
                throw new NoSuchElementException("el elemento dentro del nodo es un texto en blanco");
            }
        }
    }

    public boolean isEmpty() throws NoSuchElementException {
        //La lista esta vacia si head no apunta a nada, o si head apunta directo a tail
        if(head.getRight() == null || head.getRight().equals(tail))throw new NoSuchElementException("lista vacia");
        return true;
    }
    /// /
    public Nodo<T> getLastElement() {
        isEmpty();
        return tail.getLeft();
    }


    public Nodo<T> getPrevElement(T elemento) {
        if(elemento == null)throw new NoSuchElementException("elemento nulo");
        Nodo<T> aux = getElement(elemento);
        return getPrevElement(aux);
    }


    //extra
    public Nodo<T> getPrevElement(Nodo<T> nodo) {
        isEmpty();
        if(nodo == getElementAt(0)){
            return nodo;
        }
        if(nodo != null){
            return nodo.getLeft();
        }
        return null;
    }


    public Nodo<T> getElement(T elemento){
        isEmpty();
        if(elemento == null)throw new NoSuchElementException("error elemento vacio");
        Nodo<T> aux = head.getRight();
        return getElement(elemento,aux);
    }

    private Nodo<T> getElement(T elemento, Nodo<T> nodo){
        if(nodo == null)throw new NoSuchElementException("elemento no encontrado");
        if(elemento == nodo.getElemento()){
            return nodo;
        }
        return getElement(elemento, nodo.getRight());
    }

    public Nodo<T> getElementAt(long indice) {
        if( indice > this.indice -1 || indice < 0){
            throw new NoSuchElementException("ingresa un numero valido para el valor del indice, recuerda que el tamaño actual es de: " + this.indice + " nodos dentro la lista \nsi deseas añadir al final el valor debe ser " + this.indice + "\nsi estas buscando un nodo los valores del indice funcionales son desde 0 hasta " + (this.indice > 0 ? this.indice - 1 : 0));        }
        isEmpty();
        Nodo<T> aux = head.getRight();
        return getElementAt(indice, 0, aux);

    }
    private Nodo<T> getElementAt(long indice, int i, Nodo<T> nodo) {
        if(i == indice)return nodo;
        nodo = nodo.getRight();
        return getElementAt(indice, i +1, nodo);
    }

    public boolean isThere(T element) {
        Nodo<T> data = head.getRight();
        nodoVacio(data);
        return isThere(element, data);
    }
    private boolean isThere(T element, Nodo<T> data) {
        if(data != null){
            if(data.getElemento().equals(element))return true;
            data = data.getRight();
            return isThere(element, data);
        }
        return false;
    }

    public boolean isThere(Nodo<T> nodo) {
        nodoVacio(nodo);
        Nodo<T> data = head.getRight();
        return isThere(nodo, data);
    }

    private boolean isThere(Nodo<T> nodo, Nodo<T> data) {
        if(data != null){
            if(nodo == data)return true;
            data = data.getRight();
            return isThere(nodo, data);
        }
        return false;

    }

    //metodos de insercion

    public void addStart(T elemento) {
        if(elemento == null)return;
        Nodo<T> aux = new Nodo<>(elemento);
        nodoVacio(aux);
        //reconexion para no romper la lista
        aux.setRight(head.getRight());
        head.getRight().setLeft(aux);
        head.setRight(aux);
        aux.setLeft(head);
        indice++;
    }

    public void addAfter(T busca, T elemento2) {
        Nodo<T> antes = getElement(busca);
        Nodo<T> aux2 = new Nodo<>(elemento2);
        nodoVacio(antes);
        nodoVacio(aux2);

        aux2.setRight(antes.getRight());
        aux2.setLeft(antes);
        antes.getRight().setLeft(aux2);
        antes.setRight(aux2);
        this.indice++;
    }

    public void add(T elemento) {
        if(elemento == null)return;
        Nodo<T>aux = new Nodo<>(elemento);
        add(aux);
    }

    public void add(Nodo<T> nodo) {
        nodoVacio(nodo);
        isThere(nodo);
        if(this.indice == 0){
            //si es el primer elemento de todos se conecta a head y tail
            head.setRight(nodo);
            nodo.setLeft(head);
            nodo.setRight(tail);
            tail.setLeft(nodo);
        }
        else{
            if(isThere(nodo))throw new NoSuchElementException("nodo ya en la lista");
            Nodo<T> aux = getLastElement();
            aux.setRight(nodo);
            nodo.setLeft(aux);
            nodo.setRight(tail);
            tail.setLeft(nodo);
        }
        indice++;
    }

    public void add(T elemento, int i) {
        if(elemento == null)throw new NoSuchElementException("elemento vacio");
        Nodo<T> aux = new Nodo<>(elemento);
        add(aux, i);
    }

    public void add(Nodo<T> nodo, int i) {
        nodoVacio(nodo);
        if(isThere(nodo))throw new NoSuchElementException("nodo ya en la lista");
        if(i == 0){
            nodo.setRight(head.getRight());
            head.getRight().setLeft(nodo);
            nodo.setLeft(head);
            head.setRight(nodo);
        }else {
            Nodo<T> aux = getElementAt(i - 1);
            nodo.setRight(aux.getRight());
            aux.getRight().setLeft(nodo);
            aux.setRight(nodo);

            nodo.setLeft(aux);
        }
        this.indice++;
    }

    //metodos de eliminacion

    public void remove(T elemento) {
        if(elemento == null)throw new NoSuchElementException("elemento nulo");
        Nodo<T> aux = getElement(elemento);
        remove(aux);
    }

    public void remove(Nodo<T> nodo) {
        isEmpty();
        nodoVacio(nodo);
        if(nodo == getElementAt(0)){head.setRight(head.getRight().getRight());head.getRight().setLeft(head);indice--;return;}
        Nodo<T> aux = getPrevElement(nodo);
        //saltamos el nodo hacia adelante y hacia atras
        aux.setRight(aux.getRight().getRight());
        aux.getRight().setLeft(aux);
        this.indice--;
    }

    public void remove(Nodo<T> nodo, int i) {
        isEmpty();
        nodoVacio(nodo);
        if(i > this.indice || i < 0)throw new NoSuchElementException("el indice no es un numero valido recuerda que la lista tiene " + this.indice + " nodos por ende sus valores validos son de 0 hasta  " + (this.indice > 0 ? this.indice - 1 : 0));
        if(nodo != getElementAt(i))throw new NoSuchElementException("El nodo que deseas borrar no esta en ese indice");
        remove(nodo);
    }

    public void removeAfter(T elemento) {
        isEmpty();
        if(elemento == null)throw new NoSuchElementException("elemento nulo");
        Nodo<T> aux = getElement(elemento);
        nodoVacio(aux);
        if(aux == getElementAt(0))throw new NoSuchElementException("el elemento que deseas borrar es el primero y no tiene nada detras");
        aux= aux.getLeft();
        remove(aux);
    }

    public void removeAll(T elemento) {
        isEmpty();
        if (elemento == null) throw new NoSuchElementException("elemento nulo");
        removeAll(elemento, head.getRight());
    }

    private void removeAll(T elemento, Nodo<T> aux) {
        if (aux == tail) {
            return;
        }
        Nodo<T> siguiente = aux.getRight();
        if (aux.getElemento().equals(elemento)) {
            remove(aux);
            remove(aux);
        }
        removeAll(elemento, siguiente);
    }

    public void removeindice(int indice) {
        if( indice < 0|| indice > this.indice -1 ) throw new NoSuchElementException("error al intentar borrar con el indice, los valores validos para el indice son del 0 al " + (this.indice > 0 ? this.indice - 1 : 0));
        isEmpty();
        if(indice > 0 && indice < this.indice){
            Nodo<T> auxleft = getElementAt(indice-1);
            auxleft.getRight().getRight().setLeft(auxleft);
            auxleft.setRight(auxleft.getRight().getRight());
        }
        else if(indice == 0){
            Nodo<T> auxleft = head;
            auxleft.getRight().getRight().setLeft(auxleft);
            auxleft.setRight(auxleft.getRight().getRight());

        }

        this.indice--;

    }

    public long length() {
        return this.indice;
    }

    @Override
    public String toString() {
        isEmpty();
        return "[" + toString(head.getRight()) + "]";
    }

    private String toString(Nodo<T> nodo) {
        //la condicion de paro es cuando topamos con el centinela tail
        if (nodo.getRight() == tail) {
            return String.valueOf(nodo.getElemento());
        }
        return nodo.getElemento() + ", " + toString(nodo.getRight());
    }
}
