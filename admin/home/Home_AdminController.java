package admin.home;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import utils.ConnectionUtil;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;



public class Home_AdminController implements Initializable, Serializable {


    @FXML
    private TableView tblData;

    @FXML
    private Label lblRefresh;


    double x,y;


    @FXML
    public void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);

    }

    Connection con;

    public Home_AdminController() {
        con = ConnectionUtil.conDB();
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
    void tblRefresh() {
        tblData.getItems().clear();
        fetRowList();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        lblRefresh.setTextFill(Color.GREEN);
        lblRefresh.setText("Odświeżono tablę: "+ dtf.format(now));
    }



    public void handleExitAction() {

        System.exit(0);
    }



    public void handleAddDevice() {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../add_device/Add_Device.fxml"));

                Parent root = loader.load();

                loader.getController();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                stage.setTitle("Safety Stock - dodaj urządzenie");
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void handleRent(){

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/rented/Rented.fxml"));

                Parent root = loader.load();

                loader.getController();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
                stage.setTitle("Safety Stock - wypożyczone urządzenia");
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public void handleOverview(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/overview/Overview.fxml"));

            Parent root = loader.load();

            loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setTitle("Safety Stock - wypożyczone urządzenia");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleEdit() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../edit/Edit.fxml"));
            Parent root = loader.load();
            loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setTitle("Safety Stock - edytuj wpis");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetColumnList();
        fetRowList();

    }


    private ObservableList<ObservableList> data;


    String SQL = "SELECT * FROM stock";

    private void fetColumnList() {

        try {
            ResultSet rs = con.createStatement().executeQuery(SQL);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

            }

        } catch (Exception e) {
            System.out.println("Błąd " + e.getMessage());

        }
    }


    private void fetRowList() {
        data = FXCollections.observableArrayList();
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

