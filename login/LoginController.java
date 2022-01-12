package login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ConnectionUtil;


import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class LoginController implements Initializable, Serializable {


    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;


    double x, y;

    public LoginController() {
        con = ConnectionUtil.conDB();
    }


    @FXML
    public void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);

    }


    @FXML
    public void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }


    @FXML
    public void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    public void handleExitAction() {
        System.exit(0);
    }


    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == btnSignin) {
            if (logIn()) {

                try {
                    int secondsToSleep = 3;
                    Thread.sleep(secondsToSleep * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                try {

                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();

                    Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/admin/home/Home_Admin.fxml"))));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                    stage.setTitle("SKL - strona główna");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Błąd łączenia z bazą danych");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Połączono z bazą danych");
        }
    }

    private boolean logIn() {
        boolean status = true;
        String login = txtUsername.getText();
        String password = txtPassword.getText();
        if (login.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Puste pola");
            status = false;
        } else {
            //query
            String sql = "SELECT * FROM admins Where login = ? and haslo = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Niepoprawny login lub hasło!");
                    status = false;
                } else {
                    setLblError(Color.GREEN, "Zalogowano pomyślnie");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = false;
            }
        }

        return status;
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

    public void printAuthors(){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Autorzy projektu");
        alert.setHeaderText(null);
        alert.setContentText("1. Wojciech Kozioł\n" +
                "2. Konrad Jaszczyk\n" +
                "3. Dawid Bujak");





        alert.showAndWait();
    }

    public void printTopic(){


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Temat projektu");
            alert.setHeaderText(null);
            alert.setContentText("System magazynowy Safety Stock" +
                    " z funkcją wypożyczeń przedmiotów");
            alert.showAndWait();
        }


    }

