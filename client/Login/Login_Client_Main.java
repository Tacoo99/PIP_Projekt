package client.Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;


    public class Login_Client_Main extends Application {
        public static void main(String[] args) {
            launch();
        }

        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Login_Client_Main.class.getResource("../Home/Home_Client.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            stage.setTitle("Logowanie lub rejestracja!");
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setScene(scene);
            stage.show();
        }
    }
