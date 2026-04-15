package utilis;

import player.Reproductor;
import player.ReproduccionListener;
import list.CDLlist;
import queue.DQueue;
import stack.DStack;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Random;

public class Utils implements ReproduccionListener {

    private final String CARPETA_MUSICA = "musica";
    private Reproductor reproductor;

    //Catalogo principal (Lista circular)
    private CDLlist<File> catalogoCircular;
    //Historial/gestor de playlist (Pila que guarda colas)
    private DStack<DQueue<File>> stackPlaylists;
    private boolean enModoPlaylists = false;//indica si estamos leyendo del stack o del catalogo
    private DQueue<File> colaActual = null; //la playlist que esta sonando en este momento
    //private Queue<File> colaActual = null; //la playlist que esta sonando en este momento         --------------------------fila editada

    private int indiceActualCatalogo = 0;
    private boolean modoRepeat = false;
    private boolean modoShuffle = false;
    private Random random = new Random();

    public Utils() {
        catalogoCircular = new CDLlist<>();
        stackPlaylists = new DStack<>();
        //stackPlaylists = new Stack<>(); -------------------------linea cambiada
        cargarCatalogoCircular();
    }
    //Lee los archivos MP3 de la carpeta y los guarda en la lista circular
    private void cargarCatalogoCircular() {
        File carpeta = new File(CARPETA_MUSICA);
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (archivos != null) {

                for (File a : archivos) {catalogoCircular.add(a);
                System.out.println(a);}
            }
        }
    }
    //imprime el catalogo recorriendo la lista circular
    public void listarCanciones() {
        if (catalogoCircular.length() == 0) {
            System.out.println("Carpeta vacía o inexistente."); return;
        }
        System.out.println("\n=== CATÁLOGO DE CANCIONES (Lista Circular) ===");
        for (int i = 0; i < catalogoCircular.length(); i++) {
            System.out.println((i + 1) + ". " + catalogoCircular.getElementAt(i).getElemento().getName());
        }
    }
    //controles del reproductor
    private void iniciarReproductor(File cancion) {
        detenerReproduccion();
        reproductor = new Reproductor(cancion.getAbsolutePath(), this);
        System.out.println("\n▶ Reproduciendo: " + cancion.getName());
        reproductor.reproducir();
    }

    //pausa o reanuda
    public void pausarOReanudar() {
        if (reproductor != null) {
            if (reproductor.estaPausado()) {
                reproductor.reanudar();
                System.out.println("|| Reproducción reanudada.");
            } else if (reproductor.estaReproduciendo()) {
                reproductor.pausar();
                System.out.println("|| Reproducción pausada.");
            }
        }
    }

    public void detenerReproduccion() {
        if (reproductor != null) reproductor.detener();
    }
    @Override
    public void onCancionTerminada() {
        siguienteCancion();
    }
    //Función SEEK AND PLAY: Salta directo a una canción específica del catálogo
    public void seekAndPlay(int index) {
        if (catalogoCircular.length() == 0) return;
        enModoPlaylists = false; //Forzamos salir del modo playlist

        if(index < 0 || index >= catalogoCircular.length()) {
            System.out.println("Índice fuera de rango."); return;
        }

        indiceActualCatalogo = index;
        //Obtenemos el archivo desde nuestra Lista Circular y le damos Play
        iniciarReproductor(catalogoCircular.getElementAt(indiceActualCatalogo).getElemento());
    }

    public void toggleRepeat() { modoRepeat = !modoRepeat; System.out.println("Repeat: " + modoRepeat); }
    public void toggleShuffle() { modoShuffle = !modoShuffle; System.out.println("Shuffle: " + modoShuffle); }

    //controles de navegacion
    public void siguienteCancion() {
        if (enModoPlaylists) {
            //Si estamos en modo playlist, extraemos de la cola
            avanzarEnPilaDeColas();
        } else {
            if (catalogoCircular.length() == 0) return;
            //logica para modo catalogo
            if (modoRepeat) {
                seekAndPlay(indiceActualCatalogo); // Repite la misma
            } else if (modoShuffle) {
                int nuevoIndice;
                do {
                    nuevoIndice = random.nextInt((int) catalogoCircular.length());
                } while (nuevoIndice == indiceActualCatalogo && catalogoCircular.length() > 1);
                seekAndPlay(nuevoIndice); //cancion aleatoria
            } else {
                //siguiente cancion normal
                int sig = (int) ((indiceActualCatalogo + 1) % catalogoCircular.length());
                seekAndPlay(sig);
            }
        }
    }

    public void cancionAnterior() {
        if (enModoPlaylists) {
            System.out.println("Las Colas (Playlists) son FIFO. No puedes regresar (Back) en una Cola.");
        } else {
            if (catalogoCircular.length() == 0) return;
            int prev = (int) ((indiceActualCatalogo - 1 + catalogoCircular.length()) % catalogoCircular.length());
            seekAndPlay(prev);
        }
    }

    //creacion de playlists
    //crea una cola a partir de los indicess que elige el usuario
    public void crearPlaylist(int[] indicesSeleccionados) {
        // linea cambiada Queue<File> nuevaCola = new LinkedList<>();            ---------------------------------------------------------------------
        DQueue<File> nuevaCola = new DQueue<>(); // Usamos TU Cola
        for (int idx : indicesSeleccionados) {
            if (idx >= 0 && idx < catalogoCircular.length()) {
                nuevaCola.enqueue(catalogoCircular.getElementAt(idx).getElemento());
            }
        }
        if (!nuevaCola.isEmpty()) {
            stackPlaylists.push(nuevaCola);
            System.out.println("Playlist (Cola) de " + nuevaCola.size() + " canciones agregada al Stack.");
            System.out.println("El Stack tiene ahora " + stackPlaylists.size() + " playlists listas.");
        }
    }

    public void iniciarReproduccionStack() {
        if (stackPlaylists.isEmpty()) {
            System.out.println("El Stack de Playlists está vacío. Crea algunas primero.");
            return;
        }
        enModoPlaylists = true;
        colaActual = stackPlaylists.pop();
        System.out.println(" Iniciando Playlist del Stack... Quedan " + stackPlaylists.size() + " en espera.");
        avanzarEnPilaDeColas();
    }

    private void avanzarEnPilaDeColas() {
        if (colaActual != null && !colaActual.isEmpty()) {
            File cancion = colaActual.dequeue();
            iniciarReproductor(cancion);
        } else {
            if (!stackPlaylists.isEmpty()) {
                colaActual = stackPlaylists.pop();
                System.out.println(" Playlist terminada. Arrancando la siguiente Playlist del Stack...");
                avanzarEnPilaDeColas();
            } else {
                System.out.println(" Todas las playlists en el Stack han terminado.");
                enModoPlaylists = false;

                detenerReproduccion();
            }
        }
    }
}