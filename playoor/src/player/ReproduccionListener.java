package player;
//Interfaz que implementa el patron de diseño observador
//sirve como un puente de comunicacion entre el modo de audio y la logica de las listas
public interface ReproduccionListener {
    //Este metodo se dispara automaticamente en el momento exacto
    //en que el archivo mp3 termina de reproducirse
    void onCancionTerminada();
}
