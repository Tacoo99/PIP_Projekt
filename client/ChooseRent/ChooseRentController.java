package client.ChooseRent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class ChooseRentController{

        @FXML
        private Label loggedUser;

        private String user;

        public void setUser(String login){
                user = login;
                loggedUser.setText(user);
        }

        @FXML
        public void onBackButton(MouseEvent event) {

                try {
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.close();

                        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../MainPage/MainPage.fxml"))));
                        stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                        stage.setTitle("SKL - panel klienta");
                        stage.setScene(scene);
                        stage.show();

                } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                }
        }



        @FXML
        public void OpenLaptops(MouseEvent event) {

                try{

                        Node node = (Node) event.getSource();
                        Stage stageAccount = (Stage) node.getScene().getWindow();
                        stageAccount.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Laptop.fxml"));
                        Parent root = loader.load();

                        LaptopController scene2Controller = loader.getController();
                        scene2Controller.setUser(user);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                        stage.setTitle("SKL - wypożycz laptopa");
                        stage.show();



                } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                }

        }


        @FXML
        void OpenPhones() {

        }

        @FXML
        void OpenMonitors() {

        }

        @FXML
        void OpenPC() {

        }

        @FXML
        void OpenKeyboard() {

        }

        @FXML
        void OpenMouse() {

        }


}


