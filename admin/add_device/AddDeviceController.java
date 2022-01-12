package admin.add_device;

import product.productClass;

import java.sql.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;


public class AddDeviceController implements Initializable {


    @FXML
    private TextField txtId;

    @FXML
    private Label lblRefresh;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lbl_close;

    @FXML
    private TableView<ObservableList> tblData;

    @FXML
    private TextField txtProducent;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtSN;

    @FXML
    private ComboBox<String> txtTyp;


    private double x,y;

    PreparedStatement preparedStatement;
    Connection con;

    public AddDeviceController() {
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

        txtTyp.getItems().addAll(
                "Laptop",
                "Komputer",
                "Tablet",
                "Telefon"
        );
        fetColumnList();
        fetRowList();

    }

    @FXML
    private void HandleEvents() {


        //Nie sprawdzam czy txtID jest puste ponieważ magazyn ma funkcję autoinkrementacji
        if (txtId.getText().isEmpty() || txtSN.getText().isEmpty() || txtModel.getText().isEmpty() || txtProducent.getText().isEmpty() || txtTyp.getValue() == null)
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
        for (TextField textField : Arrays.asList(txtId, txtSN, txtModel, txtProducent)) {
            textField.clear();
        }
        txtTyp.setSelectionModel(null);
    }

    private void saveData() {

            productClass obj = new productClass();

            obj.id = txtId.getText();
            obj.sn = txtSN.getText();
            obj.producent = txtProducent.getText();
            obj.model = txtModel.getText();
            obj.typ = txtTyp.getValue();
            obj.isAvialable = true;

        try {
            String st = "INSERT INTO stock ( ID, SN, Producent, Model, Typ, Dostepny) VALUES (?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(st);
            preparedStatement.setString(1, obj.id);
            preparedStatement.setString(2, obj.sn);
            preparedStatement.setString(3, obj.producent);
            preparedStatement.setString(4, obj.model);
            preparedStatement.setString(5, obj.typ);
            preparedStatement.setBoolean(6,obj.isAvialable);

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Dodano pomyślnie");

            fetRowList();
            clearFields();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
        }
    }

    String SQL = "SELECT * from stock";

    //only fetch columns
    private void fetColumnList() {

        try {
            ResultSet rs = con.createStatement().executeQuery(SQL);


            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

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