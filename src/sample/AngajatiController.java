package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
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

public class AngajatiController implements Initializable{
    @FXML
    private Button backButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField checkField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button addButton;
    @FXML
    private TextField numeField;
    @FXML
    private TextField prenumeField;
    @FXML
    private TextField cnpField;
    @FXML
    private TextField nastereField;
    @FXML
    private TextField angajareField;
    @FXML
    private TextField telefonField;
    @FXML
    private TextField salariuField;
    @FXML
    private TextField fabricaField;
    @FXML
    private ChoiceBox<String > sexChoiceBox;

    ObservableList<String> choiceBoxList= FXCollections.observableArrayList("F","M");


    // TABELUL
    @FXML
    private TableView<Persoana> angajatiTable;

    @FXML
    private TableColumn<Persoana, Integer> accidenteColumn;

    @FXML
    private TableColumn<Persoana, String> angajareColumn;

    @FXML
    private TableColumn<Persoana, String> cnpColumn;

    @FXML
    private TableColumn<Persoana, Integer> intretinutiColumn;

    @FXML
    private TableColumn<Persoana, String> nastereColumn;

    @FXML
    private TableColumn<Persoana, String> numeColumn;

    @FXML
    private TableColumn<Persoana, String> prenumeColumn;

    @FXML
    private TableColumn<Persoana, Integer> salariuColumn;

    @FXML
    private TableColumn<Persoana, String> sexColumn;

    @FXML
    private TableColumn<Persoana, String> telefonColumn;

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
        ObservableList<Persoana> oblist = FXCollections.observableArrayList();
        sexChoiceBox.setItems(choiceBoxList);

        angajatiTable.getItems().clear();

        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Angajati");
            while(rs.next()){

                // rs.getInt("AngajatID")
                String query1 = " select count(AngajatID) as \"nr\" from AngajatAccident where AngajatID="+rs.getInt("AngajatID");
                ResultSet rs2 = connectDB.createStatement().executeQuery(query1);
                rs2.next();
                int nrAccidente=rs2.getInt(1);

                String query2 = " SELECT count(A.AngajatID)  FROM Angajati A INNER JOIN Intretinuti I ON A.AngajatID=I.AngajatID Where A.AngajatID="
                        +
                        rs.getInt("AngajatID");
                ResultSet rs3 = connectDB.createStatement().executeQuery(query2);
                rs3.next();
                int nrIntretinuti=rs3.getInt(1);

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

                if(rs.getString("NumarTelefon")!=null){
                    telefon=rs.getString("NumarTelefon");
                }
                else telefon="";

                if( rs.getInt("Salariu")!=0){
                    salariu= rs.getInt("Salariu");
                }
                else salariu=0;

                if(rs.getDate("DataNasterii")!=null){
                    nastere=rs.getDate("DataNasterii").toString();
                }
                else nastere="";


                if(rs.getDate("DataAngajarii")!=null){
                    angajare= rs.getDate("DataAngajarii").toString();
                }
                else angajare="";

                oblist.add(new Persoana(rs.getString("Nume"), rs.getString("Prenume"), cnp, sex,telefon,
                        salariu, nrIntretinuti, nrAccidente, nastere, angajare));
            }


        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }


        numeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nume"));
        prenumeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("prenume"));
        cnpColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("cnp"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("telefon"));

        sexColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("sex"));

        salariuColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Integer>("salariu"));
        intretinutiColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Integer>("intretinuti"));
        accidenteColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Integer>("accidente"));

        nastereColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nastere"));
        angajareColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("angajare"));


      //  angajatiTable.setItems(oblist);
        angajatiTable.getItems().clear();
        angajatiTable.getItems().addAll(oblist);
    }
    //Final tabel



    @FXML
    public void addButtonAction(ActionEvent event) throws IOException, SQLException {
        if(!Objects.equals(checkField.getText(), "ADD")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia ADD");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();

        String nume, prenume, cnp, telefon, sex, nastere, angajare, fabrica, salariu;

        if(Objects.equals(numeField.getText(), "")){
            messageLabel.setText("Campul Nume este gol");
            return;
        }
        else
            nume = numeField.getText();

        if(Objects.equals(prenumeField.getText(), "")){
            messageLabel.setText("Campul Prenume este gol");
            return;
        }
        else
            prenume = prenumeField.getText();


        if(!Objects.equals(fabricaField.getText(), "")) {
            String query1 = "select FabricaID from Fabrici where Nume='" + fabricaField.getText() + "' ";
            ResultSet rs = conn.createStatement().executeQuery(query1);
            rs.next();
            fabrica = rs.getString(1);
        }
        else fabrica="";

        if(cnpField.getText()!="")
            cnp=cnpField.getText();
        else cnp="";

        if(sexChoiceBox.getValue()!=null)
            sex=sexChoiceBox.getValue();
        else sex="";

        if(nastereField.getText()!="") {
            nastere = nastereField.getText();
        }
        else nastere="";

        if(telefonField.getText()!="")
            telefon=telefonField.getText();
        else telefon="";

        if(angajareField.getText()!="") {
            angajare = nastereField.getText();
        }
        else angajare="";

        if(salariuField.getText()!="")
            salariu= salariuField.getText();
        else salariu="";



        String sql = "insert into Angajati (Nume, Prenume ";
        if(fabrica!="") sql=sql+ ", FabricaID ";
        if(cnp!="") sql=sql+ ", CNP ";
        if(sex!="") sql=sql+ ", Sex ";
        if(nastere!="") sql=sql+ ", DataNasterii ";
        if(telefon!="") sql=sql+ ", NumarTelefon ";
        if(angajare!="") sql=sql+ ", DataAngajarii ";
        if(salariu!="") sql=sql+ ", Salariu ";
        sql=sql+") values('"+nume+"','"+prenume+"'";

        if(fabrica!="") sql=sql+", "+fabrica+" ";
        if(cnp!="") sql=sql+", '"+cnp+"' ";
        if(sex!="") sql=sql+", '"+sex+"' ";
        if(nastere!="") sql=sql+",convert(date,'"+nastere+"',5) ";
        if(telefon!="") sql=sql+", '"+telefon+"' ";
        if(angajare!="") sql=sql+", convert(date, '"+angajare+"',5) ";
        if(salariu!="") sql=sql+", "+salariu+" ";
        sql=sql+")";


        //convert(date,'18-06-12',5),
       // System.out.println(sql);
        int gol= conn.createStatement().executeUpdate(sql);

        if(gol==1) {
            messageLabel.setText("Adaugarea a fost efectuata cu succes");
            //RefreshTable();
            search_user();
        }
        else
            messageLabel.setText("Adaugarea nu s-a putut efectua");
    }


    //////// methode select users ///////
    @FXML
    void getSelected (MouseEvent event) throws SQLException {
        index = angajatiTable.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }

        String cnp, telefon, nastere, angajare, sex;
        String salariu;
        if(cnpColumn.getCellData(index)!=null){
            cnp=cnpColumn.getCellData(index);
        }
        else cnp="";

        if(telefonColumn.getCellData(index)!=null){
            telefon=telefonColumn.getCellData(index);
        }
        else telefon="";

        if(salariuColumn.getCellData(index)!=null){
            salariu=salariuColumn.getCellData(index).toString();
        }
        else salariu="";
        if(nastereColumn.getCellData(index)!=null){
            nastere=nastereColumn.getCellData(index).toString();
        }
        else nastere="";
        if(angajareColumn.getCellData(index)!=null){
            angajare=angajareColumn.getCellData(index).toString();
        }
        else angajare="";
        if(sexColumn.getCellData(index)!=null){
            sex=sexColumn.getCellData(index);
        }
        else sex="";


        numeField.setText(numeColumn.getCellData(index).toString());
        prenumeField.setText(prenumeColumn.getCellData(index).toString());

        String fabrica = null;
        DatabaseConnection connectNow= new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();



        String query="select Nume from Fabrici where FabricaID=( select FabricaID from Angajati \n" +
                "\t\twhere Nume='" + numeColumn.getCellData(index).toString() +
                "' and Prenume='" + prenumeColumn.getCellData(index).toString()+"' ";
                if (!Objects.equals(cnp, "")) query=query+ " and CNP='" + cnp+ "' ";
                else query=query+ " and CNP is null ";

                if(!nastere.equals("")) query=query+" and DataNasterii='"+nastere+"' ";
                else query=query+ " and DataNasterii is null ";

                if(!angajare.equals("")) query=query+" and DataAngajarii='"+angajare+"' ";
                else query=query+ " and DataAngajarii is null ";

                if (!Objects.equals(telefon, "")) query=query+" and NumarTelefon='"+ telefon+"' ";
                else query=query+ " and NumarTelefon is null ";

                if (!salariu.equals("")) query=query+" and Salariu="+ salariu;
                else query=query+ " and Salariu is null ";
                 query=query+ ")";

                //  System.out.println("query");
                // System.out.println(query);


        ResultSet rs = connectDB.createStatement().executeQuery(query);
        if(rs.next())
            fabrica=rs.getString(1);
        else
            fabrica="";
        fabricaField.setText(fabrica);


        cnpField.setText(cnp);
        sexChoiceBox.setValue(sex);
        nastereField.setText(nastere);
        telefonField.setText(telefon);
        angajareField.setText(angajare);
        salariuField.setText(salariu);

        String query1;
             query1 = "select AngajatID from Angajati where Nume='" + numeColumn.getCellData(index).toString() +
                    "' and Prenume='" + prenumeColumn.getCellData(index).toString() +"' ";
                if (!Objects.equals(cnp, "")) query=query+ " and CNP='" + cnp+ "' ";
                else query=query+ " and CNP is null ";

                if(!nastere.equals("")) query=query+" and DataNasterii='"+nastere+"' ";
                else query=query+ " and DataNasterii is null ";

                if(!angajare.equals("")) query=query+" and DataAngajarii='"+angajare+"' ";
                else query=query+ " and DataAngajarii is null ";

                if (!Objects.equals(telefon, "")) query=query+" and NumarTelefon='"+ telefon+"' ";
                else query=query+ " and NumarTelefon is null ";

                if (!salariu.equals("")) query=query+" and Salariu="+ salariu;
                else query=query+ " and Salariu is null ";
                query=query+ ")";

       // System.out.println("query1");
       // System.out.println(query1);
        ResultSet rs2 = connectDB.createStatement().executeQuery(query1);
        rs2.next();
        id=rs2.getInt(1);
        messageLabel.setText(String.valueOf(id));
    }


    @FXML
    public void updateButtonAction(ActionEvent event) throws IOException, SQLException {
        if (!Objects.equals(checkField.getText(), "UP")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia UPDATE [UP]");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();

        String nume, prenume, cnp, telefon, sex, nastere, angajare, fabrica, salariu;

        if(numeField.getText()==""){
            messageLabel.setText("Campul Nume este gol");
            return;
        }
        else
            nume = numeField.getText();

        if(prenumeField.getText()==""){
            messageLabel.setText("Campul Prenume este gol");
            return;
        }
        else
            prenume = prenumeField.getText();


        if(fabricaField.getText()!="") {
            String query1 = "select FabricaID from Fabrici where Nume='" + fabricaField.getText() + "' ";
            ResultSet rs = conn.createStatement().executeQuery(query1);
            rs.next();
            fabrica = rs.getString(1);
        }
        else fabrica="";

        if(cnpField.getText()!="")
            cnp=cnpField.getText();
        else cnp="";

        if(sexChoiceBox.getValue()!=null)
            sex=sexChoiceBox.getValue();
        else sex="";

        if(nastereField.getText()!="") {
            nastere = nastereField.getText();
        }
        else nastere="";

        if(telefonField.getText()!="")
            telefon=telefonField.getText();
        else telefon="";

        if(angajareField.getText()!="") {
            angajare = nastereField.getText();
        }
        else angajare="";

        if(salariuField.getText()!="")
            salariu= salariuField.getText();
        else salariu="";

        String sql= "UPDATE Angajati SET Nume= '"+nume+"', Prenume='"+prenume+"' ";
        if(fabrica!="") sql=sql+ ", FabricaID="+fabrica+" ";
        if(cnp!="")     sql=sql+ ", CNP='"+cnp+"' ";
        if(sex!="")     sql=sql+", Sex='"+sex+"' ";
        if(nastere!="") sql=sql+", DataNasterii='"+nastere+"' ";
        if(telefon!="") sql=sql+", NumarTelefon='"+telefon+"' ";
        if(angajare!="")sql=sql+", DataAngajarii='"+angajare+"' ";
        if(salariu!="") sql=sql+", Salariu="+salariu+" ";
        sql=sql+" where AngajatID="+id;

        System.out.println(sql);

        //convert(date,'18-06-12',5),
        // System.out.println(sql);
        int gol= conn.createStatement().executeUpdate(sql);

        if(gol==1) {
            messageLabel.setText("Modificarea a fost efectuata cu succes");
            //RefreshTable();
            search_user();
        }
        else
            messageLabel.setText("Modificarea nu s-a putut efectua");
    }


    @FXML
    public void deleteButtonAction(ActionEvent event) throws IOException, SQLException {
        if (!Objects.equals(checkField.getText(), "DEL")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia Delete [DEL]");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();
        String sql;

        String query1 = " select count(AngajatID) as \"nr\" from AngajatAccident where AngajatID="+id;
        ResultSet rs2 = conn.createStatement().executeQuery(query1);
        rs2.next();
        int nrAccidente=rs2.getInt(1);

        if(nrAccidente==0) {
            sql = "delete from Angajati where AngajatID=" + id;
        }
        else {
            sql="UPDATE Angajati SET Salariu=-1 where AngajatID="+id;
        }

        int gol= conn.createStatement().executeUpdate(sql);


        if(gol==1) {
            messageLabel.setText("Stergerea a fost efectuata cu succes");
           // RefreshTable();
            search_user();
        }
        else
            messageLabel.setText("Stergerea nu s-a putut efectua");
    }


    @FXML
    void search_user() {

        ObservableList<Persoana> dataList=FXCollections.observableArrayList();
        //angajatiTable.getItems().clear();

        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Angajati");
            while(rs.next()){

                // rs.getInt("AngajatID")
                String query1 = " select count(AngajatID) as \"nr\" from AngajatAccident where AngajatID="+rs.getInt("AngajatID");
                ResultSet rs2 = connectDB.createStatement().executeQuery(query1);
                rs2.next();
                int nrAccidente=rs2.getInt(1);

                String query2 = " SELECT count(A.AngajatID)  FROM Angajati A INNER JOIN Intretinuti I ON A.AngajatID=I.AngajatID Where A.AngajatID="
                        +
                        rs.getInt("AngajatID");
                ResultSet rs3 = connectDB.createStatement().executeQuery(query2);
                rs3.next();
                int nrIntretinuti=rs3.getInt(1);

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

                if(rs.getString("NumarTelefon")!=null){
                    telefon=rs.getString("NumarTelefon");
                }
                else telefon="";

                if( rs.getInt("Salariu")!=0){
                    salariu= rs.getInt("Salariu");
                }
                else salariu=0;

                if(rs.getDate("DataNasterii")!=null){
                    nastere=rs.getDate("DataNasterii").toString();
                }
                else nastere="";


                if(rs.getDate("DataAngajarii")!=null){
                    angajare= rs.getDate("DataAngajarii").toString();
                }
                else angajare="";
                //Nume, Prenume, FabricaID, CNP, Sex, DataNasterii, NumarTelefon, DataAngajarii, Salariu
                // obj are String nume, String prenume, String cnp,
                //                    char sex, String telefon, int salariu, int intretinuti,
                //                    int accidente, LocalDate nastere, LocalDate angajare

                dataList.add(new Persoana(rs.getString("Nume"), rs.getString("Prenume"), cnp, sex,telefon,
                        salariu, nrIntretinuti, nrAccidente, nastere, angajare));
            }


        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }


        numeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nume"));
        prenumeColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("prenume"));
        cnpColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("cnp"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("telefon"));

        sexColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("sex"));

        salariuColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Integer>("salariu"));
        intretinutiColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Integer>("intretinuti"));
        accidenteColumn.setCellValueFactory(new PropertyValueFactory<Persoana, Integer>("accidente"));

        nastereColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("nastere"));
        angajareColumn.setCellValueFactory(new PropertyValueFactory<Persoana, String>("angajare"));


        //angajatiTable.setItems(dataList);
        //angajatiTable.getItems().clear();
       // angajatiTable.getItems().addAll(dataList);

        FilteredList<Persoana> filteredData = new FilteredList<>(dataList, b -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getNume().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches nume
                } else if (person.getPrenume().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches prenume
                } else if(person.getCnp().toLowerCase().indexOf(lowerCaseFilter)!= -1){
                    return true; // Filter matches cnp
                } else if (person.getSex().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches sex
                }else if (person.getTelefon().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches tel
                }else if (String.valueOf(person.getSalariu()).toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches salariu
                }else if (String.valueOf(person.getIntretinuti()).toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches Intretinuti
                }else if (String.valueOf(person.getAccidente()).toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches accidente
                }else if (person.getNastere().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches nastere
                }else if (person.getAngajare().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches Angajare
                }
                else
                    return false; // Does not match.
            });
        });


        SortedList<Persoana> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(angajatiTable.comparatorProperty());
        angajatiTable.setItems(sortedData);
       // angajatiTable.getItems().clear();
        //angajatiTable.getItems().addAll(sortedData);
    }



    @FXML
        public void backButtonAction(ActionEvent event) throws IOException {

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
