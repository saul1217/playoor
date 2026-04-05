package list;

import nodo.Nodo;

import java.util.NoSuchElementException;

public class CDLlist <T> {

    //Sentinela mantiene la estructura circular fija, el inicio y el final
    //cursor actua como la cabeza de la reproduccion para saber que cancion suena
    Nodo<T> sentinela,cursor;
    private long indice;
    private boolean flag; //Bandera para la funcionalidad de repeat
    public CDLlist(){
        this.sentinela = new Nodo<>();
        //Al inicio el anillo esta vacio, asi que el centinela apunta a si mismo
        this.sentinela.setRight(sentinela);
        this.sentinela.setLeft(sentinela);
        this.indice = 0;
        this.cursor = sentinela; //El cursor empieza en el sentinela
        this.flag = false;
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
        if(indice == 0){
            throw new NoSuchElementException("lista vacia");
        }
        return true;
    }


    //Obtiene la ultima cancion de la lista
    public Nodo<T> getLastElement() {
        isEmpty();
       return sentinela.getLeft();
    }

    public Nodo<T> getPrevElement(T elemento) {
        if(elemento != null){
            return getPrevElement(sentinela, elemento);
        }
        return null;
    }

    private Nodo<T> getPrevElement(Nodo<T> node, T elemento) {
        if(node.getRight() == sentinela){
            return null;
        }
        if(node.getRight().getElemento().equals(elemento)){
            return node;
        }
        return getPrevElement(node.getRight(), elemento);
    }

    public Nodo<T> getPrevElement(Nodo<T> nodo) {
        if (nodo == null || nodo.getLeft() == sentinela) throw new NoSuchElementException("el elemento es el primero de la lista");
        return nodo.getLeft();
    }
    //Busca una cancion en especifico dentro del catalogo
    public Nodo<T> getElement(T elemento){
        isEmpty();
        if(elemento == null)throw new NoSuchElementException("error elemento vacio");
        Nodo<T> aux = sentinela.getRight();
        return getElement(elemento,aux);
    }

    private Nodo<T> getElement(T elemento, Nodo<T> nodo){
        if(nodo == sentinela)throw new NoSuchElementException("elemento no encontrado");
        if(elemento.equals(nodo.getElemento())) return nodo;
        return getElement(elemento, nodo.getRight());
    }

    //Busca una cancion basandose en su posicion
    public Nodo<T> getElementAt(long indice) {
        if(indice > this.indice -1 || indice < 0 ) throw new NoSuchElementException("error con el indice");
        Nodo<T> aux = sentinela.getRight();
        for (int i = 0; i < indice ; i++) {
            aux =  aux.getRight();
        }
        return aux;
    }

    public boolean isThere(T element) {
        if(sentinela.getRight() == null){
            System.out.println("lista vacia");
            return false;
        }
        Nodo<T> data = sentinela.getRight();
        return isThere(element, data);
    }

    private boolean isThere(T element, Nodo<T> data) {
        if(data != sentinela){
            if(data.getElemento().equals(element))return true;
            return isThere(element, data.getRight());
        }
        return false;
    }

    public boolean isThere(Nodo<T> nodo) {
        if(nodo == null)return false;
        try{
            isEmpty();
            Nodo<T> data = sentinela.getRight();
            return isThere(nodo, data);
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean isThere(Nodo<T> nodo, Nodo<T> data) {
        if(data == sentinela)return false;
        if(data == nodo) return true;
        return isThere(nodo, data.getRight());

    }

    //Metodos de insercion

    public void addStart(T elemento) {
        if(elemento == null)return;
        Nodo<T> aux = new Nodo<>(elemento);
        nodoVacio(aux);
        //Conectamos la nueva cancion justo despues del sentinela
        aux.setRight(sentinela.getRight());
        sentinela.getRight().setLeft(aux);
        sentinela.setRight(aux);
        aux.setLeft(sentinela);
        indice++;


    }

    public void addAfter(T elemento, T elemento2) {
        isEmpty();
        if(elemento == null)throw new NoSuchElementException("elemento vacio");
        Nodo<T> objetivo = getElement(elemento); //Cancion actual de referencia
        Nodo<T> nuevo = new Nodo<>(elemento2);//Cancion nueva a insertar
        nodoVacio(nuevo);
        //Conexiones cuadruples para meter la cancion en medio sin romper el ciclo
        nuevo.setRight(objetivo.getRight());
        nuevo.setLeft(objetivo);
        objetivo.getRight().setLeft(nuevo);
        objetivo.setRight(nuevo);
        this.indice++;
    }


    public void add(T elemento) {
        if(elemento == null){return;}
        Nodo<T>aux = new Nodo<>(elemento);
        add(aux);
    }

    //Agrega una cancion al final del catalogo principal
    public void add(Nodo<T> nodo) {
        nodoVacio(nodo);
        Nodo<T> primero = sentinela.getLeft();
        Nodo<T> ultimo = sentinela.getLeft(); //Ubicamos la ultima pista actual
        if (indice == 0){
            //Si no hay canciones, el nuevo nodo se conecta con el sentinela
            nodo.setRight(sentinela);
            nodo.setLeft(sentinela);
            sentinela.setRight(nodo);
            sentinela.setLeft(nodo);
        }else{
            //Se engancha la nueva cancion antes del sentinela
            nodo.setRight(sentinela);
            sentinela.setLeft(nodo);
            nodo.setLeft(ultimo);
            ultimo.setRight(nodo);
//
//           nuevo.setRight(primero);
//           primero.setLeft(nuevo);
//           nuevo.setLeft(sentinela);
//           sentinela.setRight(nuevo);

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
        if( i < 0 || i > this.indice)throw new NoSuchElementException("error con el indice");
        if(i == 0 ){
            Nodo<T> aux = sentinela.getRight();
            sentinela.setRight(nodo);
            nodo.setLeft(sentinela);
            nodo.setRight(aux);
            aux.setLeft(nodo);
            this.indice++;
            return;
        }
        if(i == this.indice){
            Nodo<T> aux = sentinela.getLeft();
            sentinela.setLeft(nodo);
            nodo.setRight(sentinela);
            nodo.setLeft(aux);
            aux.setRight(nodo);
            this.indice++;
            return;
        }
        Nodo<T> derecha = getElementAt(i);
        Nodo<T> izquierda = derecha.getLeft();

        derecha.setLeft(nodo);
        nodo.setRight(derecha);
        nodo.setLeft(izquierda);
        izquierda.setRight(nodo);
        this.indice++;
    }

    //Metodos de eliminacion

    public void remove(T elemento) {
        if(elemento == null)throw new NoSuchElementException("elemento vacio");
        Nodo<T> aux = getElement(elemento);
        remove(aux);
    }

    public void remove(Nodo<T> nodo) {
        isEmpty();
        if(nodo == null || nodo == sentinela)return;
        //Para borrar la cancion, simplemente conectamos su pista anterior con la siguiente
        Nodo<T> derecha = nodo.getRight();
        Nodo<T> izquierda = nodo.getLeft();
        derecha.setLeft(izquierda);
        izquierda.setRight(derecha);
        indice--;
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
        if(aux == sentinela.getRight())throw new NoSuchElementException("el elemento que deseas borrar es el primero y no tiene nada detras");
        aux= aux.getLeft();
        remove(aux);
    }

    public void removeAll(T elemento) {
        isEmpty();
        if (elemento == null) throw new NoSuchElementException("elemento nulo");
        removeAll(elemento, sentinela.getRight());
    }

    private void removeAll(T elemento, Nodo<T> aux) {
        if (aux == sentinela) return;
        Nodo<T> siguiente = aux.getRight();
        if (aux.getElemento().equals(elemento)) {
            remove(aux);
        }
        removeAll(elemento, siguiente);
    }

    public void removeindice(int indice) {
        if( indice < 0|| indice > this.indice -1 ) throw new NoSuchElementException("error al intentar borrar con el indice, los valores validos para el indice son del 0 al " + (this.indice > 0 ? this.indice - 1 : 0));
        isEmpty();
        Nodo<T> aux = getElementAt(indice);
        remove(aux);
    }


    public long length() {
        return this.indice;
    }

    @Override
    public String toString() {
        isEmpty();
        return "[" + toString(sentinela.getRight()) + "]";
    }

    private String toString(Nodo<T> nodo) {
        if (nodo.getRight() == sentinela) {
            return String.valueOf(nodo.getElemento());
        }
        return nodo.getElemento() + ", " + toString(nodo.getRight());
    }
    // Función para el botón Repeat (Repetir Lista/Canción)
    public boolean repeat(){
        flag = !flag;//Intercabia el estado de la bandera
        return flag;
    }
    // Función SEEK AND PLAY: Mueve el cursor a una posición específica y retorna la canción
    public T apuntarACancion(int indiceX) {
        isEmpty();
        this.cursor = getElementAt(indiceX);
        return this.cursor.getElemento();
    }
    // Función GET CURRENT: Retorna la canción en la que se encuentra el cursor actualmente
    public T getCancionActual() {
        if (cursor == sentinela || cursor == null) {
            return null;
        }
        return cursor.getElemento();
    }
}
