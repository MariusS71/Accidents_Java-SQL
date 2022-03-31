package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntretinutiController implements Initializable {

    @FXML
    private TableView<Persoana> angajatiTable;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Persoana, String> cnpColumn;

    @FXML
    private TableColumn<Persoana, String> nastereColumn;

    @FXML
    private TableColumn<Persoana, String> numeColumn;

    @FXML
    private TableColumn<Persoana, String> prenumeColumn;

    @FXML
    private TableColumn<Persoana, String> sexColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Persoana> oblist = FXCollections.observableArrayList();

        angajatiTable.getItems().clear();

        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Intretinuti");
            while(rs.next()){


                String cnp, sex, telefon, nastere, angajare;
                int salariu;

                if(rs.getString("CNP")!=null){
                    cnp=rs.getString("CNP");
                }
                else cnp="";

                if(   rs.getString("Sex")!=null){
                    sex=rs.getString("Sex");
                }
                else sex="";

                if(rs.getDate("DataNasterii")!=null){
                    nastere=rs.getDate("DataNasterii").toString();
                }
                else nastere="";

                oblist.add(new Persoana(rs.getString("Nume"), rs.getString("Prenume"), cnp, sex,"",
                        0, 0, 0, nastere, ""));
            }


        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }


        numeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nume"));
        prenumeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("prenume"));
        cnpColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("cnp"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("sex"));


        nastereColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nastere"));


        //  angajatiTable.setItems(oblist);
        angajatiTable.getItems().clear();
        angajatiTable.getItems().addAll(oblist);
    }




    @FXML
    void backButtonAction(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

}