package sample;

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
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import com.jfoenix.controls.*;
import net.htmlparser.jericho.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendStage implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private AnchorPane sendPane;
    @FXML
    private JFXButton backBtn;
    @FXML
    private JFXButton attach;
    @FXML
    private JFXComboBox<String> typeSend;
    @FXML
    private JFXTextField from;
    @FXML
    private JFXTextField to;
    @FXML
    private JFXTextField subject;
    @FXML
    private HTMLEditor sendText;

    private static LinkedList<String> groupMails;

    //Tro lai man hinh login
    public void logOut (ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("../../resources/fxml_file/Login.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Images/SinnoLogo.png")));
        primaryStage.setTitle("EmailApp");
        primaryStage.show();
    }

    //chon file chua dia chi emails
    public void fileChooser (ActionEvent actionEvent){
        System.out.println(Sender.getUserName());

        Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        configuringFileChooser(fileChooser);

        File path = fileChooser.showOpenDialog(stage);

        System.out.println(path.getAbsolutePath());
        try {
            //xoa du lieu groupmails cu
            to.setText("");
            if (groupMails != null) groupMails.clear();
            groupMails = Content.readFile(path.getAbsolutePath());
            System.out.println(this.groupMails);
            to.setText(groupMails.toString());
            //logging information of maillist of user
            {
                log.info("maillist of user is {} ", groupMails.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("JUST CAN NOT READ EXEL FILE");
            alert.showAndWait();
        }
        stage.show();
        stage.close();
    }

    private void setUpToSend(String recipients, Message.RecipientType type) throws MessagingException{
        Message message = new MimeMessage(Sender.getSession());// create session
        // compose message
        message.setRecipients(type, InternetAddress.parse(recipients));
        message.setSubject(subject.getText()); // input subject
        message.setFrom(new InternetAddress(Sender.getUserName())); // must set from username~~
        Source source = new Source(sendText.getHtmlText());
        message.setText(source.getRenderer().toString());// use jericho api xu ly html
        // send message
        Transport.send(message);
    }
    
    public void sendMailsTypeTo(){

        if(groupMails.isEmpty()) {
            Alert alert=new Alert(Alert.AlertType.ERROR);   //alert for failure
            alert.setTitle("ERROR");
            alert.setContentText("Mails of recipients are empty!!!");
            alert.showAndWait();
        }
        else {
            try {
                for (String recipients :
                        groupMails) {
                    if (typeSend.getValue().contentEquals("To")) setUpToSend(recipients, Message.RecipientType.TO);
                    else if (typeSend.getValue().contentEquals("Bcc"))
                        setUpToSend(recipients, Message.RecipientType.BCC);
                    else setUpToSend(recipients, Message.RecipientType.CC);
                }
                //show thong bao gui thanh cong
                Alert alert = new Alert(Alert.AlertType.INFORMATION);  //alert for success
                alert.setTitle("INFORMATION");
                alert.setContentText("Send successfully");
                alert.showAndWait();
                //show thong bao loi neu khong gui thanh cong
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);   //alert for failure
                alert.setTitle("ERROR");
                alert.setContentText("Failed to send mail");
                alert.showAndWait();
            }
        }
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("EmailList");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files(*.*)", "*.*"),
                new FileChooser.ExtensionFilter("Exel (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("Exel (*.xls)", "*.xls"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      //  stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Images/SinnoLogo.png")));

        from.setText(Sender.getUserName());
        try {
            ObservableList<String> severList = FXCollections.observableArrayList("To", "Bcc", "Cc");
            typeSend.setValue("To");
            typeSend.setItems(severList);
        } catch (Exception e) {
            System.out.println("null");
        }
    }
}
