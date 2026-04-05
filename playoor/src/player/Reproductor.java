package player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//Implementa Runnable para ejecutarse en un Hilo (Thread) separado,
//evitando que la reproducción de audio congele el menú de la consola.
public class Reproductor implements Runnable {
    private String archivo;
    private Player player; //Instancia de la libreria JLayer para mp3
    private Thread hilo;
    private boolean reproduciendo;
    private boolean pausado;
    private boolean interrumpidoManual; // Para saber si el usuario le dio stop/next
    private ReproduccionListener listener;

    public Reproductor(String archivo, ReproduccionListener listener) {
        this.archivo = archivo;
        this.listener = listener;
        this.reproduciendo = false;
        this.pausado = false;
        this.interrumpidoManual = false;
    }

    public void reproducir() {
        hilo = new Thread(this); //crea un nuevo hilo de ejecucion en segundo plano
        hilo.start();//dispara el metodo run()
    }
    //synchronized evita colisiones en la memoria cuando el usuario teclea comandos
    public synchronized void pausar() {
        if (reproduciendo && !pausado) {
            pausado = true;
        }
    }

    public synchronized void reanudar() {
        if (reproduciendo && pausado) {
            pausado = false;
            notify();//Despierta al hilo que estaba "dormido" por el wait()
        }
    }

    public void detener() {
        interrumpidoManual = true; //El usuario lo detuvo, no terminó sola
        reproduciendo = false;
        pausado = false;
        if (player != null) {
            player.close(); //Libera los recursos de audio de la tarjeta madre
        }
    }
    //Este es el motor principal que corre en segundo plano
    @Override
    public void run() {
        try {
            FileInputStream fis = new FileInputStream(archivo);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            reproduciendo = true;

            // Reproduce frame por frame para soportar pausa
            while (reproduciendo && player.play(1)) {
                if (pausado) {
                    synchronized (this) {
                        while (pausado) {
                            wait();//pone el hilo a dormir sin gastar CPU hasta recibir el notify()
                        }
                    }
                }
            }
        } catch (FileNotFoundException | JavaLayerException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            reproduciendo = false;
            // Si la canción terminó sola (no le dieron next/stop), avisamos a Utils
            if (!interrumpidoManual && listener != null) {
                listener.onCancionTerminada();
            }
        }
    }

    public boolean estaReproduciendo() { return reproduciendo; }
    public boolean estaPausado() { return pausado; }
}