/* Entrega 3 Semana 5
 * Juego de parejas
 * Participantes
 * Jean Moreno
 * José Eucardo Montaño Valencia
 * José Augusto Dimate Martinez
 * Moncayo Salazar Saul Eliud
 */



import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class PPB03G04Entrega3 extends Application {
    private static final int FILAS = 4;
    private static final int COLUMNAS = 4;
    private static final int CARTAS_POR_PAR = (FILAS * COLUMNAS) / 2;

    private char[][] tablero = new char[FILAS][COLUMNAS];
    private Button[][] botones = new Button[FILAS][COLUMNAS];
    private Set<Button> seleccionados = new HashSet<>();
    private int paresEncontrados = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        generarTablero();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25));

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Button button = new Button();
                button.setMinSize(50, 50);
                button.setOnAction(e -> seleccionarCarta(button));

                botones[i][j] = button;
                gridPane.add(button, j, i);
            }
        }

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Juego de Encontrar Parejas");
        primaryStage.show();
    }

    private void generarTablero() {
        List<Character> simbolos = new ArrayList<>();
        for (int i = 0; i < CARTAS_POR_PAR; i++) {
            char simbolo = (char) ('A' + i);
            simbolos.add(simbolo);
            simbolos.add(simbolo);
        }

        Collections.shuffle(simbolos);

        int index = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = simbolos.get(index++);
            }
        }
    }

    private void seleccionarCarta(Button button) {
        if (seleccionados.contains(button)) {
            return; // Evita que la misma carta sea seleccionada dos veces
        }

        int fila = -1;
        int columna = -1;

        // Encuentra la posición del botón seleccionado en el tablero
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (botones[i][j] == button) {
                    fila = i;
                    columna = j;
                    break;
                }
            }
        }

        if (fila == -1 || columna == -1) {
            return; // El botón seleccionado no se encontró en el tablero
        }

        // Establece el texto del botón con el símbolo de la carta correspondiente
        button.setText(String.valueOf(tablero[fila][columna]));

        // Añade el botón seleccionado al conjunto de cartas seleccionadas
        seleccionados.add(button);

        // Verifica si se encontró un par de cartas
        if (seleccionados.size() == 2) {
            Button[] cartas = seleccionados.toArray(new Button[0]);
            char carta1 = cartas[0].getText().charAt(0);
            char carta2 = cartas[1].getText().charAt(0);

            if (carta1 == carta2) {
                paresEncontrados++;
                if (paresEncontrados == CARTAS_POR_PAR) {
                    mostrarMensaje("¡Felicidades! Has encontrado todos los pares.");
                }
            } else {
                // Si no hay coincidencia, oculta las cartas después de un breve retraso
                TimerTask ocultarCartasTask = new TimerTask() {
                    public void run() {
                        Platform.runLater(() -> {
                            for (Button carta : cartas) {
                                carta.setText("");
                            }
                        });
                    }
                };

                Timer timer = new Timer();
                timer.schedule(ocultarCartasTask, 1000);
            }

            // Limpia el conjunto de cartas seleccionadas
            seleccionados.clear();
        }
    }

    private void mostrarMensaje(String mensaje) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Mensaje");
            stage.setMinWidth(200);
            stage.setMinHeight(100);

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(10);
            vbox.setPadding(new Insets(10));
            vbox.getChildren().add(new Button(mensaje));

            Scene scene = new Scene(vbox);
            stage.setScene(scene);
            stage.show();
        });
    }
}
