package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application  {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistema de compras");
            primaryStage.getIcons().add(new Image("view/resources/main.png"));
            primaryStage.setOnCloseRequest((WindowEvent e) ->
                    Platform.exit()
            );
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
