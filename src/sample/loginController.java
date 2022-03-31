package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;



public class loginController implements  Initializable{
    @FXML
    private Button cancelButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField enterPasswordField;


    @FXML
    private ImageView imageOne;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile= new File("C:/Users/saioc/Desktop/Bd 2.0/Fabrici/images/1.png");
        Image brandingImage=new Image(brandingFile.toURI().toString());
        imageOne.setImage(brandingImage);


    }

    @FXML
    public void loginButtonOnAction(ActionEvent event){
        loginMessageLabel.setText("You try to login");
        if(!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()){
            validateLogin();
        }
        else {
            loginMessageLabel.setText("Empty fields");
        }
    }



    @FXML
    protected void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(){
        DatabaseConnection connectNow= new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin= "select count(1) from user_account where username='"+usernameTextField.getText()+ "' AND password='" + enterPasswordField.getText() +" ' ";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1){
                    loginMessageLabel.setText("Connected ");
                    getFistPage();
                }
                else{
                    loginMessageLabel.setText("Incorrect username or password");
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void getFistPage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("firstPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 401);
        Stage FirstPageStage = new Stage();
        FirstPageStage.setTitle("Baza de date accidente");
        FirstPageStage.setScene(scene);
        FirstPageStage.show();

    }

}