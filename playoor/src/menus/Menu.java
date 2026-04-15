package menus;

import utilis.Utils;

import java.io.IO;

public class Menu {

    //Metodo principal del menu que controla el catalogo general
    public void iniciar(Utils utilis) {
        boolean ejecutando = true;

        while (ejecutando) {
            System.out.println("\n=== MENÚ PLAYOOR ===");
            System.out.println("CATÁLOGO: list | play | pause | stop | next | back | seek <num> | shuffle | repeat");
            System.out.println("PLAYLISTS: playlists");
            System.out.println("====================");
            System.out.print("> ");
            //Lee la entrada del usuario, quita espacios extra y la convierte a minusculas
            String entrada = IO.readln().trim().toLowerCase();
            if (entrada.isEmpty()) continue;
            //Divide la entrada en dos partes por si el usuario escribe comandos compuestos
            String[] partes = entrada.split("\\s+", 2);
            String comando = partes[0]; //La primera palabra es la accion principal

            switch (comando) {
                case "exit": case "salir":
                    utilis.detenerReproduccion();
                    System.out.println("¡Hasta luego!");
                    ejecutando = false; break;
                case "list": utilis.listarCanciones(); break;
                case "play": utilis.seekAndPlay(0); break; //Empieza desde la primera
                case "pause": utilis.pausarOReanudar(); break;
                case "stop": utilis.detenerReproduccion(); break;
                case "next": utilis.siguienteCancion(); break;
                case "back": case "prev": utilis.cancionAnterior(); break;
                case "shuffle": utilis.toggleShuffle(); break;
                case "repeat": utilis.toggleRepeat(); break;
                case "seek":
                    if (partes.length > 1) {
                        try {
                            int num = Integer.parseInt(partes[1]) - 1; //Ajuste para usuario (1-based a 0-based)
                            utilis.seekAndPlay(num);
                        } catch (Exception e) { System.out.println("Ejemplo: seek 3"); }
                    } else { System.out.println("Falta el número."); }
                    break;
                    //Abre el submenu para gestionar las colas y pilas
                case "playlists":
                case "playlist":
                    gestionarPlaylists(utilis);
                    break;
                default: System.out.println("Comando no reconocido."); break;
            }
        }
    }
    //Submenu dedicado a la creacion y reproduccion de las queues/stacks
    private void gestionarPlaylists(Utils utilis) {
        boolean enMenu = true;
        while (enMenu) {
            System.out.println("\n--- STACK DE PLAYLISTS ---");
            System.out.println("Comandos: create (Nueva Cola) | playall (reproducir playlists) | back (Regresar)");
            System.out.print("(Playlists) > ");

            String entrada = IO.readln().trim().toLowerCase();
            if (entrada.isEmpty()) continue;

            switch (entrada) {
                case "back": case "volver": enMenu = false; break;
                //Reproduce auttomaticamente todas las listas usando el stack
                case "playall": utilis.iniciarReproduccionStack(); break;
                //Editar lista, crea una nueva lista de reproduccion
                case "create":
                    utilis.listarCanciones(); //Muestra el catalogo para que el usuario elija
                    System.out.println("Escribe los números de las canciones separados por espacios (Ej: 1 3 4)");
                    String nums = IO.readln().trim();
                    String[] numStr = nums.split("\\s+"); //Separa los numeros tecleados
                    int[] indices = new int[numStr.length];
                    try {
                        //Convierte los textos a numeros y ajusta el indice para la estructura de datos
                        for(int i = 0; i < numStr.length; i++) {
                            indices[i] = Integer.parseInt(numStr[i]) - 1;
                        }
                        //manda el arreglo a utils para generar y apilar la nueva cola
                        utilis.crearPlaylist(indices);
                    } catch(Exception e) {
                        System.out.println("Error al leer números.");
                    }
                    break;
                case "next": utilis.siguienteCancion(); break;
                case "pause": utilis.pausarOReanudar(); break;
                case "stop": utilis.detenerReproduccion(); break;
                default: System.out.println("Comando inválido aquí."); break;
            }
        }
    }
}