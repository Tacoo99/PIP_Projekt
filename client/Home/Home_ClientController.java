package client.Home;

import client.MainPage.MainPageController;
import client.MyAccount.MyAccountController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;


public class Home_ClientController implements Initializable{

    Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public Home_ClientController() {
        con = ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(this.con == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wystąpił błąd!");
            alert.setHeaderText("Brak połączenia z serwerem!");
            alert.showAndWait();
        }
    }

    // Strings which hold css elements to easily re-use in the application
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Label invalidSignupCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField signUpNameTextField;
    @FXML
    private TextField signUpSurnameTextField;
    @FXML
    private TextField loginPasswordPasswordField;
    @FXML
    private TextField signUpUsernameTextField;
    @FXML
    private TextField signUpEmailTextField;
    @FXML
    private TextField signUpPasswordPasswordField;
    @FXML
    private TextField signUpRepeatPasswordPasswordField;

    private String login,password;

    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected boolean onLoginButtonClick(MouseEvent event) {

        boolean status = true;
        if (loginUsernameTextField.getText().isBlank() || loginPasswordPasswordField.getText().isBlank()) {
            invalidLoginCredentials.setText("Podaj nazwę użytkownika!");
            invalidLoginCredentials.setStyle(errorMessage);
            invalidSignupCredentials.setText("");

            if (loginUsernameTextField.getText().isBlank()) {
                loginUsernameTextField.setStyle(errorStyle);
            } else if (loginPasswordPasswordField.getText().isBlank()) {
                loginPasswordPasswordField.setStyle(errorStyle);
            }
        } else {

            String sql = "SELECT * FROM clients Where login = ? and password = ?";
            try {
                login = loginUsernameTextField.getText();
                password = loginPasswordPasswordField.getText();

                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, login );
                preparedStatement.setString(2, password );
                resultSet = preparedStatement.executeQuery();



                if (!resultSet.next()) {
                    invalidLoginCredentials.setText("Niepoprawny login lub hasło!");
                    invalidLoginCredentials.setStyle(errorMessage);
                    status = false;
                }

                else {
                    invalidLoginCredentials.setText("Zalogowano pomyślnie!");
                    invalidLoginCredentials.setStyle(successMessage);
                    loginUsernameTextField.setStyle(successStyle);
                    loginPasswordPasswordField.setStyle(successStyle);
                    invalidSignupCredentials.setText("");

                    try {

                        Node node = (Node) event.getSource();
                        Stage stageHome = (Stage) node.getScene().getWindow();
                        stageHome.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage/MainPage.fxml"));
                        Parent root = loader.load();

                        MainPageController scene2Controller = loader.getController();
                        scene2Controller.setLogin(login);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                        stage.setTitle("SKL - panel klienta");
                        stage.show();

                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }

                }

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = false;
            }
        }

        return status;
    }

    @FXML
    protected void onSignUpButtonClick() {

        boolean status = true;

        if (signUpUsernameTextField.getText().isBlank() || signUpEmailTextField.getText().isBlank() || signUpPasswordPasswordField.getText().isBlank() || signUpRepeatPasswordPasswordField.getText().isBlank()
        || signUpNameTextField.getText().isEmpty() || signUpSurnameTextField.getText().isEmpty() ) {
            InvalidSingupCredentials();

            if (signUpUsernameTextField.getText().isBlank()) {
                signUpUsernameTextField.setStyle(errorStyle);
            } else if (signUpEmailTextField.getText().isBlank()) {
                signUpEmailTextField.setStyle(errorStyle);
            } else if (signUpPasswordPasswordField.getText().isBlank()) {
                signUpPasswordPasswordField.setStyle(errorStyle);
            } else if (signUpRepeatPasswordPasswordField.getText().isBlank()) {
                signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            }
            else if (signUpNameTextField.getText().isBlank()) {
                signUpNameTextField.setStyle(errorStyle);
            }
            else if (signUpSurnameTextField.getText().isBlank()) {
                signUpSurnameTextField.setStyle(errorStyle);
            }

        } else if (signUpRepeatPasswordPasswordField.getText().equals(signUpPasswordPasswordField.getText())) {
            InvalidSingupCredentials2("Hasła zgadzają się!");

            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate ld = LocalDate.now();
            String text = ld.format(formatters);

            String sql2 = "INSERT INTO clients values (?,?,?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setNull(1, Types.NULL);
                preparedStatement.setString(2, signUpUsernameTextField.getText() );
                preparedStatement.setString(3, signUpEmailTextField.getText() );
                preparedStatement.setString(4, signUpPasswordPasswordField.getText() );
                preparedStatement.setString(5, signUpNameTextField.getText() );
                preparedStatement.setString(6, signUpSurnameTextField.getText() );
                preparedStatement.setString(7, text );
                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
               status = false;
            }

            if(status){
                InvalidSingupCredentials2("Konto zostało założone");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rejestracja konta");
                alert.setHeaderText(null);
                alert.setContentText("Konto zostało założone, możesz już się zalogować!");
                alert.showAndWait();

            }

        } else {
            invalidSignupCredentials.setText("Hasła są różne!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        }


    }

    private void InvalidSingupCredentials(){
        invalidSignupCredentials.setText("Uzupełnij wszystkie pola!");
        invalidSignupCredentials.setStyle(errorMessage);
        invalidLoginCredentials.setText("");
    }

    private void InvalidSingupCredentials2(String text){
        invalidSignupCredentials.setText(text);
        invalidSignupCredentials.setStyle(successMessage);
        signUpUsernameTextField.setStyle(successStyle);
        signUpEmailTextField.setStyle(successStyle);
        signUpPasswordPasswordField.setStyle(successStyle);
        signUpRepeatPasswordPasswordField.setStyle(successStyle);
        invalidLoginCredentials.setText("");
    }

}

