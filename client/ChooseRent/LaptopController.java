package client.ChooseRent;

import client.MainPage.MainPageController;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LaptopController implements Initializable{

    @FXML
    private Button BackMenu;

    private ObservableList<ObservableList> data;
    @FXML
    private TableView<ObservableList> tblData;

    Connection con;
    PreparedStatement preparedStatement;

    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";
    String ChosenLaptop;

    @FXML
    private ComboBox<String> cpuCombo;

    @FXML
    private ComboBox<String> diskCombo;

    @FXML
    private ComboBox<String> gpuCombo;

    @FXML
    private ComboBox<String>ramCombo;

    @FXML
    private ComboBox<String>ekranCombo;

    @FXML
    private Label loggedUser;

    @FXML
    private ComboBox<String> producentCombo;

    @FXML
    private Button confirm;

    String CurrUSer;

    public void setUser(String login){
        this.CurrUSer = login;
        loggedUser.setText(CurrUSer);

    }

    public LaptopController() {
        con = ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


            producentCombo.getItems().addAll("Asus", "Lenovo", "Acer", "Samsung");
            cpuCombo.getItems().addAll("i5", "i7", "i3", "Ryzen", "Xeon");
            gpuCombo.getItems().addAll("Nvidia", "Radeon", "Intel");
            ramCombo.getItems().addAll("DDR3", "DDR3L", "DDR4");
            diskCombo.getItems().addAll("SSD", "SSHD", "HDD");
            ekranCombo.getItems().addAll("LED", "LCD", "IPS");

    }

    @FXML
    void searchLaptop() {

        boolean status = false;
        tblData.getItems().clear();


        String Producent = null, CPU = null, GPU = null, RAM = null, Disk = null, Ekran = null;

        if (producentCombo.getValue() == null) {
            producentCombo.setStyle(errorStyle);

        } else {
            producentCombo.setStyle(successStyle);
            Producent = producentCombo.getValue();
            status = true;
        }


        if (cpuCombo.getValue() == null) {
            cpuCombo.setStyle(errorStyle);


        } else {
            cpuCombo.setStyle(successStyle);
            CPU = cpuCombo.getValue();
            status = true;
        }


        if (gpuCombo.getValue() == null) {
            gpuCombo.setStyle(errorStyle);

        } else {
            gpuCombo.setStyle(successStyle);
            GPU = gpuCombo.getValue();
            status = true;
        }

        if (ramCombo.getValue() == null) {
            ramCombo.setStyle(errorStyle);


        } else {
            ramCombo.setStyle(successStyle);
            RAM = ramCombo.getValue();
            status = true;
        }

        if (diskCombo.getValue() == null) {
            diskCombo.setStyle(errorStyle);

        } else {
            diskCombo.setStyle(successStyle);
            Disk = diskCombo.getValue();
            status = true;
        }

        if (ekranCombo.getValue() == null) {
            ekranCombo.setStyle(errorStyle);


        } else {
            ekranCombo.setStyle(successStyle);
            Ekran = ekranCombo.getValue();
            status = true;
        }

        if (status) {

            String SQL = "SELECT Producent, Model, CPU, GPU, RAM, Dysk, Ekran FROM stock WHERE Typ = ? AND Producent = ? AND CPU = ? AND GPU = ? AND RAM = ? AND Dysk = ? AND EKRAN = ?";

            try {
                preparedStatement = con.prepareStatement(SQL);
                preparedStatement.setString(1, "Laptop");
                preparedStatement.setString(2, Producent);
                preparedStatement.setString(3, CPU);
                preparedStatement.setString(4, GPU);
                preparedStatement.setString(5, RAM);
                preparedStatement.setString(6, Disk);
                preparedStatement.setString(7, Ekran);
                ResultSet rs = preparedStatement.executeQuery();

                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                    col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>)
                            param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                    tblData.getColumns().addAll(col);
                }

                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);

                }

                tblData.setItems(data);


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void getAll(){

        tblData.getItems().clear();
        data = FXCollections.observableArrayList();

        data.clear();


        String SQL = "SELECT Producent, Model, CPU, GPU, RAM, Dysk, Ekran, SN FROM stock WHERE Typ = ?";

        try{

        preparedStatement = con.prepareStatement(SQL);
        preparedStatement.setString(1, "Laptop" );
        ResultSet rs = preparedStatement.executeQuery();

            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>)
                        param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tblData.getColumns().removeAll();
                tblData.getColumns().addAll(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);

            }
            tblData.setItems(data);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

        @FXML
    void CategoryBack(MouseEvent event) {

        try{

            Node node = (Node) event.getSource();
            Stage stageAccount = (Stage) node.getScene().getWindow();
            stageAccount.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseRent.fxml"));
            Parent root = loader.load();

            ChooseRentController scene2Controller = loader.getController();
            scene2Controller.setUser(CurrUSer);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setTitle("Panel klienta - wybierz kategorię");
            stage.show();



        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }


    @FXML
    void onBackButton() {

        try{

            Stage stageOld = (Stage) BackMenu.getScene().getWindow();
            stageOld.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage/MainPage.fxml"));
            Parent root = loader.load();

            MainPageController scene2Controller = loader.getController();
            scene2Controller.setLogin(CurrUSer);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/images/icons8_file_settings_128px.png"));
            stage.setTitle("Panel klienta - menu główne");
            stage.show();



        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    void confirmRent(){
        ObservableList test = tblData.getSelectionModel().getSelectedItem();

        ChosenLaptop = (String) test.get(7);

        System.out.println("Wybrano laptopa o SN: " + test.get(7) );
    }

}
