package player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Reproductor implements Runnable {
    private String archivo;
    private Player player;
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
        hilo = new Thread(this);
        hilo.start();
    }

    public synchronized void pausar() {
        if (reproduciendo && !pausado) {
            pausado = true;
        }
    }

    public synchronized void reanudar() {
        if (reproduciendo && pausado) {
            pausado = false;
            notify();
        }
    }

    public void detener() {
        interrumpidoManual = true; // El usuario lo detuvo, no terminó sola
        reproduciendo = false;
        pausado = false;
        if (player != null) {
            player.close();
        }
    }

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
                            wait();
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