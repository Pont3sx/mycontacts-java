package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dao.Conexao;

import java.io.IOException;

public class AgendaApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/tela-principal.fxml")
        );

        Scene scene = new Scene(loader.load(), 750, 500);

        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
        );

        stage.setTitle("MyContacts — Agenda de Contatos");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    @Override
    public void stop() {
        Conexao.fechar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
