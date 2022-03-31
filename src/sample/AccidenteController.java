package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccidenteController implements Initializable {

    @FXML
    private TableView<Accident> accidenteTable;

    @FXML
    private Button addAngajatButton;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Accident, String> administratorColumn;

    @FXML
    private TableColumn<Accident, String> angajatiColumn;

    @FXML
    private ComboBox<String> angajatiComboBox;

    @FXML
    private Button backButton;

    @FXML
    private TextField checkField;

    @FXML
    private TableColumn<Accident, String> dataColumn;

    @FXML
    private TextField dataField;

    @FXML
    private TableColumn<Accident, String> descriereColumn;

    @FXML
    private TextArea descriereArea;

    @FXML
    private TableColumn<Accident, String> locatieColumn;

    @FXML
    private Label messageLabel;

    @FXML
    private TableColumn<Accident, String> pagubeColumn;

    @FXML
    private TextField pagubeField;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> tipChoiceBox;

    @FXML
    private TableColumn<Accident, String> tipColumn;

    @FXML
    private Button updateButton;

    int index;
    int id=-1;
    Connection conn =null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> choiceBoxList= FXCollections.observableArrayList("Usor","Moderat","Grav");
        tipChoiceBox.setItems(choiceBoxList);
        RefreshTable();
        search_user();
    }


    public void RefreshTable(){
        ObservableList<Accident> oblist = FXCollections.observableArrayList();
        accidenteTable.getItems().clear();



        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Accidente");
            while (rs.next()) {

                String tip, data, pagube, angajati, locatie, administrator, descriere;

                if(rs.getString("TipAccident")!=null) {
                    tip=rs.getString("TipAccident");
                }
                else tip="";

                if(rs.getString("Data")!=null) {
                    data=rs.getString("Data");
                }
                else data="";

                if(rs.getString("Valoare_Pagube")!=null) {
                    pagube=rs.getString("Valoare_Pagube");
                }
                else pagube="";

                String query1;
                query1="select count(AngajatID) from AngajatAccident where NumarAccident="+rs.getInt("NumarAccident");
                ResultSet rs1 = connectDB.createStatement().executeQuery(query1);
                rs1.next();
                angajati=rs1.getString(1);

                if(!Objects.equals(angajati, "0")){
                    String query2="select F.Nume \n" +
                            "from Angajati A inner join Fabrici F on A.FabricaID=F.FabricaID\n" +
                            "where A.AngajatID=(select max(AngajatID) from AngajatAccident" +
                                                " where NumarAccident="+ rs.getInt("NumarAccident") +")";
                    ResultSet rs2 = connectDB.createStatement().executeQuery(query2);
                    rs2.next();
                    locatie=rs2.getString(1);
                }
                else locatie="";

                if(locatie!=""){
                    String query2="select Adm.Nume, Adm.Prenume\n" +
                            "from Fabrici F inner join Angajati A  on A.FabricaID=F.FabricaID\n" +
                            "\t\t\t\tinner join Angajati Adm on F.AdministratorID=Adm.AngajatID\n" +
                            "where A.AngajatID=(select max(AngajatID) from AngajatAccident" +
                                              " where NumarAccident="+ rs.getInt("NumarAccident") +")";
                    ResultSet rs2 = connectDB.createStatement().executeQuery(query2);
                    rs2.next();
                    administrator=rs2.getString(1)+" "+rs2.getString(2);
                }
                else administrator="";


                if(rs.getString("Descriere")!=null) {
                    descriere=rs.getString("Descriere");
                }
                else descriere="";

                oblist.add(new Accident(tip, data, pagube, angajati, locatie, administrator, descriere));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tipColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("tip"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("data"));
        pagubeColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("pagube"));
        angajatiColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("angajati"));
        locatieColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("locatie"));
        administratorColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("administrator"));
        descriereColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("descriere"));

        // fabriciTable.setItems(oblist);
        accidenteTable.getItems().addAll(oblist);
       // accidenteTable.setColumnResizePolicy(("descriere"));
    }





    @FXML
    void addAngajatButtonAction(ActionEvent event) throws SQLException {
        if(!Objects.equals(checkField.getText(), "ADD")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia ADD");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();

        if(angajatiComboBox.getValue()==""){
            messageLabel.setText("Completati cu numele si prenumele angajatului");
            return;
        }

        String[] aux = angajatiComboBox.getValue().split(" ");
        if(aux.length==1){
            messageLabel.setText("Completati cu numele si prenumele angajatului");
            return;
        }

        String query=" select count (AngajatID) from Angajati where Nume='"+aux[0]+"' and Prenume='"+aux[1]+"' ";

       // System.out.println(query);

        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();

        if(rs.getInt(1)==0){
            messageLabel.setText("Numele introdus nu exista in baza de date");
            return;
        }
        else if(rs.getInt(1)==1){

            String query1="select AngajatID from Angajati where Nume='"+aux[0]+"' and Prenume='"+aux[1]+"' ";
            ResultSet rs1 = conn.createStatement().executeQuery(query1);
            rs1.next();
            String angajat = rs1.getString(1);

            String query2= "SET IDENTITY_INSERT AngajatAccident ON " +
                    "insert into AngajatAccident (NumarAccident, AngajatID) \n" +
                    "values("+id +","+angajat+")";



            int gol= conn.createStatement().executeUpdate(query2);
            if(gol==1) {
                messageLabel.setText("Adaugarea a fost efectuata cu succes");
                //RefreshTable();
                search_user();
            }
            else
                messageLabel.setText("Adaugarea nu s-a putut efectua");
        }
        else if(rs.getInt(1)>1){

            String [] aux2 = angajatiComboBox.getValue().split(" ");
            if(aux2.length==2){
                messageLabel.setText("Exista mai multe persoane cu acest nume. Introduceti nume prenume cnp");
                return;
            }
            else{
                String query1="select AngajatID from Angajati where Nume='"+aux2[0]+"' and Prenume='"+aux2[1]+"'and CNP='"+aux[2]+"'";
                ResultSet rs1 = conn.createStatement().executeQuery(query1);
                if(!rs1.next()){
                    messageLabel.setText("CNP invalid");
                    return;
                }
                String angajat = rs1.getString(1);

                String query2= "SET IDENTITY_INSERT AngajatAccident ON " +
                        "insert into AngajatAccident (NumarAccident, AngajatID) \n" +
                        "values("+id +","+angajat+")";

                int gol= conn.createStatement().executeUpdate(query2);
                if(gol==1) {
                    messageLabel.setText("Adaugarea a fost efectuata cu succes");
                    //RefreshTable();
                    search_user();
                }
                else
                    messageLabel.setText("Adaugarea nu s-a putut efectua");
            }
        }
    }

    @FXML
    void addButtonAction(ActionEvent event) throws SQLException {
        if(!Objects.equals(checkField.getText(), "ADD")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia ADD");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();


        if(descriereArea.getText()==""){
            messageLabel.setText("Campul Descriere este gol");
            return;
        }
        if(tipChoiceBox.getValue()==""){
            messageLabel.setText("Alegeti tipul accidentului");
            return;
        }
        String tip = tipChoiceBox.getValue();
        String data= dataField.getText();
        String pagube= pagubeField.getText();
        String descriere= descriereArea.getText();

        String query = "insert into Accidente (";
        if(tip!="") query=query+"TipAccident, ";
        if(data!="")    query=query+"Data, ";
        if(pagube!="")    query=query+"Valoare_Pagube, ";
        query=query+" Descriere) values (";

        if(tip!="") query=query + "'"+tip +"', ";
        if(data!="")    query=query + "convert(date,'"+data+"',5), ";
        if(pagube!="")    query=query + pagube +", ";
        query=query+"'"+descriere+"')";

        System.out.println(query);

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
    void getSelected(MouseEvent event) throws SQLException {
        DatabaseConnection connectNow= new DatabaseConnection();
        Connection conn = connectNow.getConnection();
        index = accidenteTable.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }

        String tip;
        if(tipColumn.getCellData(index)==null)
            tip="";
        else
            tip= tipColumn.getCellData(index);

        String data;
        if(dataColumn.getCellData(index)==null)
            data="";
        else
            data=dataColumn.getCellData(index);

        String pagube;
        if(pagubeColumn.getCellData(index)==null)
            pagube="";
        else
            pagube=pagubeColumn.getCellData(index);

        String descriere;
        if(descriereColumn.getCellData(index)==null)
            descriere="";
        else
            descriere=descriereColumn.getCellData(index);


        ObservableList<String> angajati =FXCollections.observableArrayList("");
        if(angajatiColumn.getCellData(index)==null || angajatiColumn.getCellData(index)=="0" )
            angajati.add("");
        else{
            String query= "select A.Nume, A.Prenume, A.CNP \n" +
                    "from AngajatAccident AC inner join Angajati A on A.AngajatID=AC.AngajatID\n" +
                    "\tinner join Accidente acc on acc.NumarAccident=AC.NumarAccident\n" +
                    "\twhere acc.Descriere='"+descriere+"'";
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                String aux="";
                aux=rs.getString("Nume")+" "+rs.getString("Prenume")+" "+rs.getString("CNP");
                angajati.add(aux);
            }
        }

        tipChoiceBox.setValue(tip);
        dataField.setText(data);
        pagubeField.setText(pagube);
        descriereArea.setWrapText(true);
        descriereArea.setText(descriere);
        angajatiComboBox.setItems(angajati);
        angajatiComboBox.setPromptText("Angajati");
        angajatiComboBox.setEditable(true);


        String query;
        query="select NumarAccident from Accidente where Descriere='"+descriere+"'";
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        id=rs.getInt(1);
        messageLabel.setText(String.valueOf(id));
    }

    @FXML
    void updateButtonAction(ActionEvent event) throws SQLException {
        if(!Objects.equals(checkField.getText(), "UP")) {
            messageLabel.setText("Asigurati-va ca ati confirmat operatia Update(UP)");
            return;
        }
        DatabaseConnection connectNow= new DatabaseConnection();
        conn = connectNow.getConnection();


        if(descriereArea.getText()==""){
            messageLabel.setText("Campul Descriere este gol");
            return;
        }
        if(tipChoiceBox.getValue()==""){
            messageLabel.setText("Alegeti tipul accidentului");
            return;
        }
        String tip = tipChoiceBox.getValue();
        String data= dataField.getText();
        String pagube= pagubeField.getText();
        String descriere= descriereArea.getText();

        String query = "UPDATE Accidente SET ";
        if(tip!="") query=query+ "TipAccident ='"+tip+"', ";
        if(data!="") query=query+"Data='"+data+"', ";
        if(pagube!="") query=query+"Valoare_Pagube="+pagube+", ";
        query=query+"Descriere='"+descriere+"' ";
        query=query+" where NumarAccident="+id;

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
        ObservableList<Accident> dataList = FXCollections.observableArrayList();
        //accidenteTable.getItems().clear();



        DatabaseConnection connectNow= new DatabaseConnection();
        try {
            Connection connectDB = connectNow.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("select * FROM Accidente");
            while (rs.next()) {

                String tip, data, pagube, angajati, locatie, administrator, descriere;

                if(rs.getString("TipAccident")!=null) {
                    tip=rs.getString("TipAccident");
                }
                else tip="";

                if(rs.getString("Data")!=null) {
                    data=rs.getString("Data");
                }
                else data="";

                if(rs.getString("Valoare_Pagube")!=null) {
                    pagube=rs.getString("Valoare_Pagube");
                }
                else pagube="";

                String query1;
                query1="select count(AngajatID) from AngajatAccident where NumarAccident="+rs.getInt("NumarAccident");
                ResultSet rs1 = connectDB.createStatement().executeQuery(query1);
                rs1.next();
                angajati=rs1.getString(1);

                if(!Objects.equals(angajati, "0")){
                    String query2="select F.Nume \n" +
                            "from Angajati A inner join Fabrici F on A.FabricaID=F.FabricaID\n" +
                            "where A.AngajatID=(select max(AngajatID) from AngajatAccident" +
                            " where NumarAccident="+ rs.getInt("NumarAccident") +")";
                    ResultSet rs2 = connectDB.createStatement().executeQuery(query2);
                    rs2.next();
                    locatie=rs2.getString(1);
                }
                else locatie="";

                if(locatie!=""){
                    String query2="select Adm.Nume, Adm.Prenume\n" +
                            "from Fabrici F inner join Angajati A  on A.FabricaID=F.FabricaID\n" +
                            "\t\t\t\tinner join Angajati Adm on F.AdministratorID=Adm.AngajatID\n" +
                            "where A.AngajatID=(select max(AngajatID) from AngajatAccident" +
                            " where NumarAccident="+ rs.getInt("NumarAccident") +")";
                    ResultSet rs2 = connectDB.createStatement().executeQuery(query2);
                    rs2.next();
                    administrator=rs2.getString(1)+" "+rs2.getString(2);
                }
                else administrator="";


                if(rs.getString("Descriere")!=null) {
                    descriere=rs.getString("Descriere");
                }
                else descriere="";

                dataList.add(new Accident(tip, data, pagube, angajati, locatie, administrator, descriere));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(AngajatiController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tipColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("tip"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("data"));
        pagubeColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("pagube"));
        angajatiColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("angajati"));
        locatieColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("locatie"));
        administratorColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("administrator"));
        descriereColumn.setCellValueFactory(new PropertyValueFactory<Accident, String>("descriere"));

        // fabriciTable.setItems(oblist);
        //accidenteTable.getItems().addAll(dataList);
        // accidenteTable.setColumnResizePolicy(("descriere"));

        FilteredList<Accident> filteredData = new FilteredList<>(dataList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getTip().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches tip
                }
                else if(person.getData().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches data
                else if(person.getPagube().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches pagube
                else if(person.getAngajati().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches angajati
                else if (person.getLocatie().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;// Filter matches locaitie
                }
                else if (person.getAdministrator().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches administrator
                }
                else if (person.getDescriere().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;// Filter matches descriere
                }
                else return false; // Does not match.
            });
        });

        SortedList<Accident> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(accidenteTable.comparatorProperty());
        //  fabriciTable.setItems(sortedData);
        accidenteTable.setItems(sortedData);
       //accidenteTable.getItems().addAll(sortedData);
    }

}
