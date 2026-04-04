package utilis;

import player.Reproductor;
import player.ReproduccionListener;
import list.CDLlist;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Random;

public class Utils implements ReproduccionListener {

    private final String CARPETA_MUSICA = "playoor/musica";
    private Reproductor reproductor;

    private CDLlist<File> catalogoCircular;
    private Stack<Queue<File>> stackPlaylists;

    private boolean enModoPlaylists = false;
    private Queue<File> colaActual = null;

    private int indiceActualCatalogo = 0;
    private boolean modoRepeat = false;
    private boolean modoShuffle = false;
    private Random random = new Random();

    public Utils() {
        catalogoCircular = new CDLlist<>();
        stackPlaylists = new Stack<>();
        cargarCatalogoCircular();
    }

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

    public void listarCanciones() {
        if (catalogoCircular.length() == 0) {
            System.out.println("Carpeta vacía o inexistente."); return;
        }
        System.out.println("\n=== CATÁLOGO DE CANCIONES (Lista Circular) ===");
        for (int i = 0; i < catalogoCircular.length(); i++) {
            System.out.println((i + 1) + ". " + catalogoCircular.getElementAt(i).getElemento().getName());
        }
    }

    private void iniciarReproductor(File cancion) {
        detenerReproduccion();
        reproductor = new Reproductor(cancion.getAbsolutePath(), this);
        System.out.println("\n▶ Reproduciendo: " + cancion.getName());
        reproductor.reproducir();
    }

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

    public void seekAndPlay(int index) {
        if (catalogoCircular.length() == 0) return;
        enModoPlaylists = false;

        if(index < 0 || index >= catalogoCircular.length()) {
            System.out.println("Índice fuera de rango."); return;
        }

        indiceActualCatalogo = index;
        iniciarReproductor(catalogoCircular.getElementAt(indiceActualCatalogo).getElemento());
    }

    public void toggleRepeat() { modoRepeat = !modoRepeat; System.out.println("Repeat: " + modoRepeat); }
    public void toggleShuffle() { modoShuffle = !modoShuffle; System.out.println("Shuffle: " + modoShuffle); }

    public void siguienteCancion() {
        if (enModoPlaylists) {
            avanzarEnPilaDeColas();
        } else {
            if (catalogoCircular.length() == 0) return;
            if (modoRepeat) {
                seekAndPlay(indiceActualCatalogo); // Repite la misma
            } else if (modoShuffle) {
                int nuevoIndice;
                do {
                    nuevoIndice = random.nextInt((int) catalogoCircular.length());
                } while (nuevoIndice == indiceActualCatalogo && catalogoCircular.length() > 1);
                seekAndPlay(nuevoIndice);
            } else {
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


    public void crearPlaylist(int[] indicesSeleccionados) {
        Queue<File> nuevaCola = new LinkedList<>();
        for (int idx : indicesSeleccionados) {
            if (idx >= 0 && idx < catalogoCircular.length()) {
                nuevaCola.add(catalogoCircular.getElementAt(idx).getElemento());
            }
        }
        if (!nuevaCola.isEmpty()) {
            stackPlaylists.push(nuevaCola);
            System.out.println("✅ Playlist (Cola) de " + nuevaCola.size() + " canciones agregada al Stack.");
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
        System.out.println("\n🚀 Iniciando Playlist del Stack... Quedan " + stackPlaylists.size() + " en espera.");
        avanzarEnPilaDeColas();
    }

    private void avanzarEnPilaDeColas() {
        if (colaActual != null && !colaActual.isEmpty()) {
            File cancion = colaActual.poll();
            iniciarReproductor(cancion);
        } else {
            if (!stackPlaylists.isEmpty()) {
                colaActual = stackPlaylists.pop();
                System.out.println("\n🔄 Playlist terminada. Arrancando la siguiente Playlist del Stack...");
                avanzarEnPilaDeColas();
            } else {
                System.out.println("\n🛑 Todas las playlists en el Stack han terminado.");
                enModoPlaylists = false;

                detenerReproduccion();
            }
        }
    }
}