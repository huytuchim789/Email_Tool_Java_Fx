package sample;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXComboBox<String> choiceSever;
    @FXML
    private AnchorPane loginScene;


    public void login(ActionEvent actionEvent) throws IOException {

        try {
            //check the Authentication Mail .if it failed ,catch exception
            if (validateCredentials(userName.getText(), password.getText())) {
                //set data of sender
                Sender.setUserName(userName.getText());
                Sender.setPassWord(password.getText());
                Sender.setSession(createSmtpSession(userName.getText(), password.getText()));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText("Login Successfully");
                alert.showAndWait();

                Stage stage = (Stage) password.getScene().getWindow();
                stage.hide();

                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_file/SendingStage.fxml"));
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Images/SinnoLogo.png")));
                primaryStage.setTitle("SendingStage");
                primaryStage.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setHeaderText("Login Failed");
                alert.setContentText("Email or password is invalid");
                alert.showAndWait();
                //logging information of session which user is logining
                {
                    log.info("Email or password is invalid");
                }
            }
        } catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Login Failed");
            alert.setContentText("Internet isn't connected");
            alert.showAndWait();
            //logging information of session which user is logining
            {
                log.info("Internet isn't connected");
            }
        }
        //logging information of session which user is logining
        {
            log.info("User's email is {}", Sender.getUserName());
        }
    }

    // make a session
    public Session createSmtpSession(String user, String password) {

        final Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", choiceSever.getValue());
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        return session;
    }

    public boolean validateCredentials(String user, String password) {
        try {
            Session session = createSmtpSession(user, password);
            Transport transport = session.getTransport();
            transport.connect();
            transport.close();
        } catch (AuthenticationFailedException e) {
            return false;
        } catch (MessagingException e) {
            throw new RuntimeException("validate failed", e);
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userName.setText(Sender.getUserName());
        try {
        ObservableList<String> severList = FXCollections.observableArrayList("smtp.gmail.com", "smtp.office365.com");
            choiceSever.setValue("smtp.gmail.com");
            choiceSever.setItems(severList);
        } catch (Exception e) {
            System.out.println("null");
        }
    }
}
