import java.io.IOException;

import javax.naming.InvalidNameException;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class RegisterManager {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPass;
    @FXML
    private Button registerButton;
    @FXML
    private Text errorText;

    @FXML
    void submitted(ActionEvent e) {
        boolean missingInfo = false;
        errorText.setText("");
        if(fname.getText().isBlank()) {
            missingInfo = true;
            errorText.setText("Must include first name!");
        }
        if(lname.getText().isBlank()) {
            missingInfo = true;
            errorText.setText(errorText.getText() + "\nMust include last name!");
        }
        if(email.getText().isBlank()) {
            missingInfo = true;
            errorText.setText(errorText.getText() + "\nMust include email!");
        }
        if(password.getText().isBlank()) {
            missingInfo = true;
            errorText.setText(errorText.getText() + "\nMust include password!");
        }
        if(!missingInfo && password.getText().equals(confirmPass.getText())) {
            try {
                System.out.println("Signing user up.");
                GuiMain.account = Account.signup(email.getText(), password.getText(), GuiMain.schedule, fname.getText(), lname.getText());
                if(GuiMain.account != null) {
                    System.out.println("Account made, logging in");
                    GuiMain.loggedIn = true;
                    GuiMain.schedule = Schedule.retrieveSchedule(GuiMain.account);
                    if(GuiMain.schedule.hasSchedule()) {
                        switchToCalender(e);
                    } else {
                        switchToSearch(e);
                    }
                }
                System.out.println("Email already in use.");
                errorText.setText("Email already taken.");
            } catch(InvalidNameException ex) {
                // TODO invalid email. do something here
                errorText.setText(errorText.getText() + "\nInvalid Email");
                System.err.println("Invalid email.");
                // ex.printStackTrace();
            } 
            catch (Exception e1) {
                // TODO Auto-generated catch block
                errorText.setText("Sorry, something went wrong! Please try again later.");
                e1.printStackTrace();
            }
        } else {
            errorText.setText(errorText.getText() + "\nPassswords must match!");
        }
    }

    @FXML
    public void switchToMain(ActionEvent e) throws IOException {
        System.out.println("Switching to main");
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToLogin(ActionEvent e) throws IOException {
        System.out.println("Switching to login");
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToCalender(ActionEvent e) throws IOException {
        System.out.println("Switching to calender");
        root = FXMLLoader.load(getClass().getResource("calender.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSchedule(ActionEvent e) throws IOException {
        System.out.println("Switching to Schedule");
        root = FXMLLoader.load(getClass().getResource("schedule.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSearch(ActionEvent e) throws IOException {
        System.out.println("Switching to Search");
        root = FXMLLoader.load(getClass().getResource("search.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
