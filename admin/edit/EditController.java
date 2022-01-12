package admin.edit;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class EditController implements Initializable{


    @FXML
    private ComboBox comboDel;

    @FXML
    Label lbl_status;

    @FXML
    Label lbl_close;

    ResultSet resultSet = null;

    @FXML
    private TextField idTxt;

    @FXML
    private TextField dostTxt;

    @FXML
    private TextField modelTxt;

    @FXML
    private TextField producentTxt;

    @FXML
    private TextField snTxt;

    @FXML
    private TextField typTxt;

    final String query = "SELECT id from stock";
    double x,y;
    PreparedStatement preparedStatement;

    @FXML
    public void handleExitAction() {
        Stage stage = (Stage) lbl_close.getScene().getWindow();
        stage.close();
    }

    Connection con;

    public EditController() {
        con = ConnectionUtil.conDB();
    }

    @FXML
    void handleDelete(){

        boolean status;
        String id = comboDel.getSelectionModel().getSelectedItem().toString();
        String sql = "DELETE FROM stock WHERE ID = ?";


        try{
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            status = true;

        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        }

        Alert alert;
        if(status){

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText("Urządzenie o ID = " + id + " zostało usunięte z bazy");

        }
        else{

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wystąpił błąd!");
            alert.setHeaderText("Urządzenie o ID = " + id + " nie zostało usunięte z bazy!");

        }
        alert.showAndWait();

    }
    @FXML
    void HandleEdit() {

        String id = comboDel.getSelectionModel().getSelectedItem().toString();

        boolean status;

        try {
            String sql = "SELECT * FROM stock WHERE ID = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idTxt.setText(resultSet.getString("id"));
                snTxt.setText(resultSet.getString("SN"));
                producentTxt.setText(resultSet.getString("Producent"));
                modelTxt.setText(resultSet.getString("Model"));
                typTxt.setText(resultSet.getString("Typ"));
                dostTxt.setText(resultSet.getString("Dostepny"));
            }

            status = true;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            status = false;

        }


        if(status){

            setlbl("Znaleziono taki przedmiot!",Color.GREEN,lbl_status);

        }
        else{
            setlbl("Nie znaleziono takiego przedmiotu!",Color.RED,lbl_status);
        }
    }

    @FXML
    void saveChanges(){

        String id,sn,producent,model,typ,dostepnosc;
        id = idTxt.getText();
        sn = snTxt.getText();
        producent = producentTxt.getText();
        model = modelTxt.getText();
        typ = typTxt.getText();
        dostepnosc = dostTxt.getText();

        try {
            String st = "UPDATE stock set SN = ?, Producent = ?, Model = ?, Typ = ?, Dostepny = ? WHERE id = ?";
            preparedStatement = con.prepareStatement(st);
            preparedStatement.setString(1, sn);
            preparedStatement.setString(2, producent);
            preparedStatement.setString(3, model);
            preparedStatement.setString(4, typ);
            preparedStatement.setString(5, dostepnosc);
            preparedStatement.setString(6, id);

            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText("Wpis został zmodyfikowany");
            alert.showAndWait();


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd modyfikowania wpisu!");
            alert.showAndWait();
        }
    }


    void setlbl(String text, Color color, Label label)
    {
        label.setTextFill(color);
        label.setText(text);
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

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

            try{

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);


                while(rs.next()){
                    comboDel.getItems().add(rs.getInt("id"));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

}

