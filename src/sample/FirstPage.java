package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class FirstPage implements Initializable {

    @FXML
    private Button accidenteButton;

    @FXML
    private Button intretinutiButton;

    @FXML
    private Button angajatiButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button fabriciButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button testButton;

    @FXML
    private ImageView imageTwo;

    @FXML
    private Label textLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile= new File("C:/Users/saioc/Desktop/Bd 2.0/Fabrici/images/300.jpg");
        Image brandingImage=new Image(brandingFile.toURI().toString());
        imageTwo.setImage(brandingImage);

    }

    @FXML
    public void intretinutiButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("intretinuti.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        Stage loginStage = new Stage();
        loginStage.setTitle("Intretinuti");
        loginStage.setScene(scene);
        loginStage.show();
    }


    @FXML
    public void accidenteButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("accidente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 550);
        Stage loginStage = new Stage();
        loginStage.setTitle("Accidente");
        loginStage.setScene(scene);
        loginStage.show();
    }

    @FXML
    public void angajatiButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("angajati.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        Stage loginStage = new Stage();
        loginStage.setTitle("Angajati");
        loginStage.setScene(scene);
        loginStage.show();
    }

    @FXML
    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void fabriciButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("fabrici.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 550);
        Stage loginStage = new Stage();
        loginStage.setTitle("Fabrici");
        loginStage.setScene(scene);
        loginStage.show();
    }

    @FXML
    public void logoutButtonAction(ActionEvent event) throws IOException {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 307);
        Stage loginStage = new Stage();
        loginStage.setTitle("Baza de date accidente Login");
        loginStage.setScene(scene);
        loginStage.show();
    }

    @FXML
    public void testButtonAction(ActionEvent event) throws SQLException {
        String query= "select count(F.FabricaID)\n" +
                "FROM Fabrici F\n" +
                "WHERE F.FabricaID in (select A.FabricaID from Angajati A \n" +
                "\t\t\t\t\t\twhere A.Salariu<0)";

        DatabaseConnection connectNow= new DatabaseConnection();
        Connection conn = connectNow.getConnection();
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        int nr=rs.getInt(1);
        textLabel.setText(String.valueOf(nr));
    }

}
