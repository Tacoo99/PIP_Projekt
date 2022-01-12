package client.MyAccount;

import client.MainPage.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;



public class MyAccountController {

        @FXML
        private TextField acc_creatText;

        @FXML
        private TextField discountText;

        @FXML
        private TextField ordersText;

        @FXML
        private TextField emailText;

        @FXML
        private TextField nameSurnameText;

        @FXML
        private TextField usernameText;

        @FXML
        public void onBackButton(MouseEvent event){

                try{

                Node node = (Node) event.getSource();
                Stage stageAccount = (Stage) node.getScene().getWindow();
                stageAccount.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage/MainPage.fxml"));
                Parent root = loader.load();

                MainPageController scene2Controller = loader.getController();
                scene2Controller.setLogin(usernameText.getText());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                stage.setTitle("SKL - panel klienta");
                stage.show();



        } catch (IOException ex) {
                System.err.println(ex.getMessage());
                }

        }

        public void setLogin(String login){
                usernameText.setText(login);
        }

        public void setName(String name, String surname){
                nameSurnameText.setText(name + " " + surname);
        }

        public void setEmail(String email){
                emailText.setText(email);
        }

        public void setAcc_Creation(String acc_creation){
                acc_creatText.setText(acc_creation);
        }

        public void setOrders(int orders){
                ordersText.setText(String.valueOf(orders));
        }

        public void setDiscount(int discount){
                discountText.setText(discount + "%");
        }
}

