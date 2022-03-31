package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class FabriciController implements Initializable {

    @FXML
    private TableColumn<Fabrica, Integer> accidenteColumn;
    @FXML
    private Button addButton;
    @FXML
    private TableColumn<Fabrica, String> administratorColumn;
    @FXML
    private TextField administratorField;
    @FXML
    private TableColumn<Fabrica, Integer> angajatiColumn;
    @FXML
    private TableView<Fabrica> fabriciTable;
    @FXML
    private Button backButton;
    @FXML
    private TextField checkField;
    @FXML
    private Button deleteButton;
    @FXML
    private TableColumn<Fabrica, Integer> intretinutiColumn;
    @FXML
    private TableColumn<Fabrica, String> judetColumn;
    @FXML
    private TextField judetField;
    @FXML
    private Label messageLabel;
    @FXML
    private TableColumn<Fabrica, String> numeColumn;
    @FXML
    private TextField numeField;
    @FXML
    private TableColumn<Fabrica, String> orasColumn;
    @FXML
    private TextField orasField;
    @FXML
    private TextField searchField;
    @FXML
    private Button updateButton;

    int id=-1;
    int index=-1;
    Connection conn =null;
    ResultSet rs = null;
    PreparedStatement pst = null;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RefreshTable();
        search_user();
    }


    public void RefreshTable(){
        ObservableList<Fabrica> oblist = FXCollections.observableArrayList();
        fabriciTable.getItems().clear();

        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Fabrici");
            while(rs.next()){

                // rs.getInt("FabricaID")
                String administrator, judet, oras;
                int angajati, intretinuti, accidente ;

                if(rs.getInt("AdministratorID")!=0){
                String q1 ="select A.Nume, A.Prenume\n" +
                        "from Angajati A inner join Fabrici F on A.AngajatID=F.AdministratorID where F.FabricaID="+ rs.getInt("FabricaID");
                ResultSet rs1 = connectDB.createStatement().executeQuery(q1);
                rs1.next();
                administrator=rs1.getString(1)+" "+rs1.getString(2);
                }
                else administrator="";

                if(rs.getString("JudetID")!=null) {
                    String q2 = "select Nume from Judete where JudetID=" + rs.getString("JudetID");
                    ResultSet rs2 = connectDB.createStatement().executeQuery(q2);
                    rs2.next();
                    judet = rs2.getString(1);
                }
                else judet="";


                String q3="select count(AngajatID) from Angajati  where FabricaID="+rs.getString("FabricaID");
                ResultSet rs3 = connectDB.createStatement().executeQuery(q3);
                rs3.next();
                angajati=rs3.getInt(1);



                String q4="select count(I.IntretinutID)\n" +
                        "from Angajati A inner join Intretinuti I on I.AngajatID=A.AngajatID\n" +
                        "\tinner join Fabrici F on F.FabricaID=A.FabricaID\n" +
                        "\t\twhere F.FabricaID="+rs.getString("FabricaID");
                ResultSet rs4 = connectDB.createStatement().executeQuery(q4);
                rs4.next();
                intretinuti=rs4.getInt(1);

                String q5="select count(DISTINCT Ac.NumarAccident)\n" +
                        "from Angajati A inner join AngajatAccident Ac on Ac.AngajatID=A.AngajatID\n" +
                        "\t\tinner join Fabrici F on F.FabricaID=A.FabricaID\n" +
                        "\t\twhere F.FabricaID="+rs.getString("FabricaID");
                ResultSet rs5 = connectDB.createStatement().executeQuery(q5);
                rs5.next();
                accidente=rs5.getInt(1);

                if(rs.getString("NumeOras")!=null) oras=rs.getString("NumeOras");
                else oras="";

                oblist.add(new Fabrica(rs.getString("Nume"), administrator, judet, oras,
                        angajati, intretinuti, accidente));
            }


        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }



        numeColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("nume"));
        administratorColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("administrator"));
        judetColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("judet"));
        orasColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("oras"));

        angajatiColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, Integer>("angajati"));

        accidenteColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, Integer>("accidente"));
        intretinutiColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, Integer>("intretinuti"));

       // fabriciTable.setItems(oblist);
        fabriciTable.getItems().addAll(oblist);
    }





    @FXML
    void addButtonAction(ActionEvent event) throws SQLException {
        if(!Objects.equals(checkField.getText(), "ADD")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia ADD");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();


        if(numeField.getText()==""){
            messageLabel.setText("Campul Nume este gol");
            return;
        }
        String nume = numeField.getText();
        String judet= judetField.getText();
        String administrator= administratorField.getText();
        String oras= orasField.getText();

        String query = "insert into Fabrici (Nume";
        if(judet!="") query=query+ ", JudetID";
        if(administrator!="") query=query+ ", AdministratorID";
        if(oras!="") query=query+ ", NumeOras";
        query=query+ ") values('"+nume+"' ";
        if(judet!=""){
            String query2="select JudetID from Judete where Nume='"+ judet +"'";
            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            rs2.next();
            query=query+", "+rs2.getString(1)+" ";
        }
        if(administrator!=""){
            String[] splited = administrator.split(" ");
            String query2="select AngajatID \n" +
                    "from Angajati " +
                    "where Nume='"+splited[0]+"' and Prenume='"+splited[1]+"'";


            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            rs2.next();
            query=query+ " , "+rs2.getInt(1)+" ";
        }
        if(oras!="") query=query+", '"+oras+"' ";
        query=query+")";

        int gol= conn.createStatement().executeUpdate(query);
        if(gol==1) {
            messageLabel.setText("Adaugarea a fost efectuata cu succes");
            //RefreshTable();
            search_user();
        }
        else
            messageLabel.setText("Adaugarea nu s-a putut efectua");

    }

    @FXML
    void backButtonAction(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void deleteButtonAction(ActionEvent event) throws SQLException {
        if (!Objects.equals(checkField.getText(), "DEL")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia Delete [DEL]");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();

        String sql="delete from Fabrici where FabricaID="+id;
        int gol= conn.createStatement().executeUpdate(sql);


        if(gol==1) {
            messageLabel.setText("Stergerea a fost efectuata cu succes");
            //RefreshTable();
            search_user();
        }
        else
            messageLabel.setText("Stergerea nu s-a putut efectua");
    }

    @FXML
    void getSelected(MouseEvent event) throws SQLException {
        index = fabriciTable.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }

        String nume;
        if(numeColumn.getCellData(index)==null)
            nume="";
        else
            nume=numeColumn.getCellData(index);

        String judet;
        if(judetColumn.getCellData(index)==null)
            judet="";
        else
            judet=judetColumn.getCellData(index);

        String administrator;
        if(administratorColumn.getCellData(index)==null)
            administrator="";
        else
            administrator=administratorColumn.getCellData(index);

        String oras;
        if(orasColumn.getCellData(index)==null)
            oras="";
        else
            oras=orasColumn.getCellData(index);

        numeField.setText(nume);
        judetField.setText(judet);
        administratorField.setText(administrator);
        orasField.setText(oras);

        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();


        String query="select FabricaID from Fabrici where ";
        if(!Objects.equals(nume, ""))    query=query+"Nume='"+nume+"' ";
        else query=query+" and Nume is null";

        if(!Objects.equals(judet, ""))    {
            String query2="select JudetID from Judete where Nume='"+ judet +"'";
            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            rs2.next();
            query=query+" and JudetID="+rs2.getInt(1)+" ";
            }else query=query+" and JudetID is null";

        if(!Objects.equals(administrator, "")) {
            String[] splited = administrator.split(" ");
            String query2="select A.AngajatID \n" +
                    "from Angajati A inner join Fabrici F on A.AngajatID=F.AdministratorID\n" +
                    "where A.Nume='"+splited[0]+"' and A.Prenume='"+splited[1]+"'";
            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            rs2.next();
            query=query+ " and AdministratorID="+rs2.getInt(1)+" ";
        } else query=query+" and AdministratorID is null ";

        if(!Objects.equals(oras, ""))    query=query+" and NumeOras='"+oras+"' ";
        else query=query+" and NumeOras is null ";

        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        id=rs.getInt(1);
        messageLabel.setText(String.valueOf(id));

    }

    @FXML
    void updateButtonAction(ActionEvent event) throws SQLException {
        if(!Objects.equals(checkField.getText(), "UP")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia Update [UP]");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();


        if(numeField.getText()==""){
            messageLabel.setText("Campul Nume este gol");
            return;
        }
        String nume = numeField.getText();
        String judet= judetField.getText();
        String administrator= administratorField.getText();
        String oras= orasField.getText();

        String query = "UPDATE Fabrici SET nume='"+nume+"' ";
        if(judet!=""){
            String query2="select JudetID from Judete where Nume='"+ judet +"'";
            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            rs2.next();
            query=query+", JudetID="+rs2.getString(1)+" ";
        }
        if(administrator!=""){
            String[] splited = administrator.split(" ");
            String query2="select AngajatID \n" +
                    "from Angajati " +
                    "where Nume='"+splited[0]+"' and Prenume='"+splited[1]+"'";


            ResultSet rs2 = conn.createStatement().executeQuery(query2);
            rs2.next();
            query=query+ " , AdministratorID="+rs2.getInt(1)+" ";
        }
        if(oras!="") query=query+", NumeOras='"+oras+"' ";
        query=query+" where FabricaID="+id;

        int gol= conn.createStatement().executeUpdate(query);
        if(gol==1) {
            messageLabel.setText("Modificarea a fost efectuata cu succes");
            //RefreshTable();
            search_user();
        }
        else
            messageLabel.setText("Modificarea nu s-a putut efectua");

    }

    @FXML
    void search_user() {
        ObservableList<Fabrica> dataList=FXCollections.observableArrayList();
        //fabriciTable.getItems().clear();

        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Fabrici");
            while(rs.next()){

                // rs.getInt("FabricaID")
                String administrator, judet, oras;
                int angajati, intretinuti, accidente ;

                if(rs.getInt("AdministratorID")!=0){
                    String q1 ="select A.Nume, A.Prenume\n" +
                            "from Angajati A inner join Fabrici F on A.AngajatID=F.AdministratorID where F.FabricaID="+ rs.getInt("FabricaID");
                    ResultSet rs1 = connectDB.createStatement().executeQuery(q1);
                    rs1.next();
                    administrator=rs1.getString(1)+" "+rs1.getString(2);
                }
                else administrator="";

                if(rs.getString("JudetID")!=null) {
                    String q2 = "select Nume from Judete where JudetID=" + rs.getString("JudetID");
                    ResultSet rs2 = connectDB.createStatement().executeQuery(q2);
                    rs2.next();
                    judet = rs2.getString(1);
                }
                else judet="";


                String q3="select count(AngajatID) from Angajati  where FabricaID="+rs.getString("FabricaID");
                ResultSet rs3 = connectDB.createStatement().executeQuery(q3);
                rs3.next();
                angajati=rs3.getInt(1);



                String q4="select count(I.IntretinutID)\n" +
                        "from Angajati A inner join Intretinuti I on I.AngajatID=A.AngajatID\n" +
                        "\tinner join Fabrici F on F.FabricaID=A.FabricaID\n" +
                        "\t\twhere F.FabricaID="+rs.getString("FabricaID");
                ResultSet rs4 = connectDB.createStatement().executeQuery(q4);
                rs4.next();
                intretinuti=rs4.getInt(1);

                String q5="select count(DISTINCT Ac.NumarAccident)\n" +
                        "from Angajati A inner join AngajatAccident Ac on Ac.AngajatID=A.AngajatID\n" +
                        "\t\tinner join Fabrici F on F.FabricaID=A.FabricaID\n" +
                        "\t\twhere F.FabricaID="+rs.getString("FabricaID");
                ResultSet rs5 = connectDB.createStatement().executeQuery(q5);
                rs5.next();
                accidente=rs5.getInt(1);

                if(rs.getString("NumeOras")!=null) oras=rs.getString("NumeOras");
                else oras="";

                dataList.add(new Fabrica(rs.getString("Nume"), administrator, judet, oras,
                        angajati, intretinuti, accidente));
            }


        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }



        numeColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("nume"));
        administratorColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("administrator"));
        judetColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("judet"));
        orasColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, String>("oras"));

        angajatiColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, Integer>("angajati"));

        accidenteColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, Integer>("accidente"));
        intretinutiColumn.setCellValueFactory(new PropertyValueFactory<Fabrica, Integer>("intretinuti"));

        //fabriciTable.setItems(dataList);


        FilteredList<Fabrica> filteredData = new FilteredList<>(dataList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getNume().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches nume
                }
                else if(person.getAdministrator().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true; // Filter matches administrator
                else if(person.getJudet().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true; // Filter matches judet
                else if(person.getOras().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true; // Filter matches oras
                else if (String.valueOf(person.getAngajati()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;// Filter matches angajati
                    }
                else if (String.valueOf(person.getIntretinuti()).toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches Intretinuti
                }
                else if (String.valueOf(person.getAccidente()).toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches accidente
                }
                else return false; // Does not match.
            });
        });

        SortedList<Fabrica> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(fabriciTable.comparatorProperty());
        fabriciTable.setItems(sortedData);
       // fabriciTable.getItems().addAll(sortedData);
    }
}