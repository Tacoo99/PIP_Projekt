package client.MainPage;

import client.ChooseRent.ChooseRentController;
import client.MyAccount.MyAccountController;
import client.OrderHistory.OrderHistoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class MainPageController{

    private String email, name, surname, acc_creat,logged;

    @FXML
    private Label loggedUser;


    Connection con;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    int orders = 0;
    int sum,discount;

    public MainPageController() {
        con = ConnectionUtil.conDB();
    }

        @FXML
    void OpenMyAccount(MouseEvent event) {


        String CurrUser = loggedUser.getText();
        String sql = "SELECT email, name, surname, acc_creation FROM clients WHERE login = ?";

        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, CurrUser );
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {

                email = resultSet.getString("email");
                name = resultSet.getString("name");
                surname = resultSet.getString("surname");
                acc_creat = resultSet.getString("acc_creation");
            }

            String sql2 = "SELECT * from orders WHERE clientName = ?";
            preparedStatement = con.prepareStatement(sql2);
            preparedStatement.setString(1, CurrUser );
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                orders++;
                sum = sum + resultSet.getInt("cost");
            }

            if(sum > 2000){
                discount = 5;
            }
            if(sum > 5000){
                discount = 10;
            }
            if(sum > 10000){
                discount = 20;
            }
            if(sum > 50000){
                discount = 30;
            }

            Node node = (Node) event.getSource();
            Stage stageMain = (Stage) node.getScene().getWindow();
            stageMain.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../MyAccount/MyAccount.fxml"));
            Parent root = loader.load();

            MyAccountController scene2Controller = loader.getController();
            scene2Controller.setLogin(loggedUser.getText());
            scene2Controller.setName(name,surname);
            scene2Controller.setEmail(email);
            scene2Controller.setAcc_Creation(acc_creat);
            scene2Controller.setOrders(orders);
            scene2Controller.setDiscount(discount);


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setTitle("Panel klienta - dodaj zamówienie");
            stage.show();


        } catch (IOException | SQLException ex ) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    void OpenRent(MouseEvent event) {

            try {

                Node node = (Node) event.getSource();
                Stage stageMain = (Stage) node.getScene().getWindow();
                stageMain.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../ChooseRent/ChooseRent.fxml"));
                Parent root = loader.load();

                ChooseRentController scene2Controller = loader.getController();
                scene2Controller.setUser(logged);


                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                stage.setTitle("Panel klienta - dodaj zamówienie");
                stage.show();


            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }


    }

    @FXML
    void OpenHistory(MouseEvent event) {

        try {

            String sql3 = "SELECT * from orders WHERE clientName = ?";
            preparedStatement = con.prepareStatement(sql3);
            preparedStatement.setString(1, loggedUser.getText() );
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                orders++;
                sum = sum + resultSet.getInt("cost");
            }

            Node node = (Node) event.getSource();
            Stage stageMain = (Stage) node.getScene().getWindow();
            stageMain.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../OrderHistory/OrderHistory.fxml"));
            Parent root = loader.load();

            OrderHistoryController scene2Controller = loader.getController();
            scene2Controller.setSum(sum);
            scene2Controller.setOrders(orders);
            scene2Controller.setLogin(loggedUser.getText());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setTitle("Panel klienta - dodaj zamówienie");
            stage.show();


        } catch (IOException | SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    public void setLogin(String login){
        logged = login;
        loggedUser.setText(logged);
    }
}

