package admin.approve_rent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class ApproveRentController implements Initializable{


    @FXML
    private ComboBox comboDel;

    @FXML
    Label lbl_close;

    final String query = "SELECT id from lista";
    double x,y;
    PreparedStatement preparedStatement;

    @FXML
    public void handleExitAction() {
        Stage stage = (Stage) lbl_close.getScene().getWindow();
        stage.close();
    }

    Connection con;

    public ApproveRentController() {
        con = ConnectionUtil.conDB();
    }

    @FXML
    void HandleEdit() {

        String id = comboDel.getSelectionModel().getSelectedItem().toString();
        System.out.println(id);

        String sql = "DELETE FROM lista WHERE id = ?";
        boolean status;

        try {
            sql = "DELETE FROM lista WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            status = false;

        }

        status = true;

        if(status){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText(null);
            alert.setContentText("Wpis został usunięty pomyślnie");

            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd usuwania wpisu");
            alert.setHeaderText(null);
            alert.setContentText("Wpis nie został usunięty");

            alert.showAndWait();
        }
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

