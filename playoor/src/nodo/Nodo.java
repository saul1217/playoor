package nodo;
//TDA definicion de un nodo
public class Nodo<T> {
    //elemento guardara un objeto file
    private T elemento;
    //apuntadores bidireccionales para los botones next y back
    private Nodo<T> right;
    private Nodo<T> left;
    //constructor principal
    public Nodo(T elemento){
        this.elemento = elemento;
        right = null;
        left = null;
    }
    //constructor vacio
    public Nodo(){
        this(null);
    };
    //getters setters
    public Nodo<T> getLeft() {
        return left;
    }

    public void setLeft(Nodo<T> left) {
        this.left = left;
    }

    public T getElemento() {
        return elemento;
    }

    public void setElemento(T elemento) {
        this.elemento = elemento;
    }

    public Nodo<T> getRight() {
        return right;
    }

    public void setRight(Nodo<T> right) {
        this.right = right;
    }



}
