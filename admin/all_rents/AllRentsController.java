package admin.all_rents;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;


public class AllRentsController implements Initializable {


    @FXML
    private TextField txtId;

    @FXML
    private TextField txtImie;

    @FXML
    private TextField txtNazwisko;

    @FXML
    private TextField txtModel;

    @FXML
    private JFXTimePicker Godz_wyp;

    @FXML
    private JFXDatePicker Data_wyp;

    @FXML
    private JFXDatePicker Data_odd;

    @FXML
    private JFXTimePicker Godz_odd;

    @FXML
    private TextField txtTyp;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<ObservableList> tblData;

    @FXML
    private Label lbl_close;

    @FXML
    private Label lblRefresh;


    private double x,y;

    PreparedStatement preparedStatement;
    Connection con;

    public AllRentsController() {
        con =  ConnectionUtil.conDB();
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
    public void minimize(MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    public void handleCloseWindow() {
            Stage stage = (Stage) lbl_close.getScene().getWindow();
            stage.close();
    }


    @FXML
    void tblRefresh() {
        tblData.getItems().clear();
        fetRowList();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        lblRefresh.setTextFill(Color.GREEN);
        lblRefresh.setText("Odświeżono tablę: "+ dtf.format(now));
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fetColumnList();
        fetRowList();

    }

    @FXML
    private void HandleEvents() {


        if (txtId.getText().isEmpty() || txtImie.getText().isEmpty() || txtNazwisko.getText().isEmpty() || txtTyp.getText().isEmpty() ||
                txtModel.getText().isEmpty() || Data_wyp.getValue() == null || Godz_wyp.getValue() == null || Data_odd.getValue() == null
        || Godz_odd.getValue() == null)
        {

            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Błąd! : Podaj wszystkie dane");
        }

        else {


            tblData.getItems().clear();
            saveData();
            fetRowList();

        }
    }

    private void clearFields() {
        for (TextField textField : Arrays.asList(txtId, txtImie, txtNazwisko, txtTyp, txtModel)) {
            textField.clear();
        }
    }

    private void saveData() {

        try {
            String st = "INSERT INTO lista ( id, imie, nazwisko, typ, model, data_wyp, godz_wyp, data_odd, godz_odd) VALUES (?,?,?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(st);
            preparedStatement.setString(1, txtId.getText());
            preparedStatement.setString(2, txtImie.getText());
            preparedStatement.setString(3, txtNazwisko.getText());
            preparedStatement.setString(4, txtTyp.getText());
            preparedStatement.setString(5, txtModel.getText());
            preparedStatement.setString(6, Data_wyp.getValue().toString());
            preparedStatement.setString(7, Godz_wyp.getValue().toString());
            preparedStatement.setString(8, Data_odd.getValue().toString());
            preparedStatement.setString(9, Godz_odd.getValue().toString());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Dodano pomyślnie");

            fetRowList();
            //clear fields
            clearFields();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
        }
    }

    String SQL = "SELECT * from lista";

    //only fetch columns
    private void fetColumnList() {

        try {
            ResultSet rs = con.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);


            }

        } catch (Exception e) {
            System.out.println("Błąd " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = con.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);

            }

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}