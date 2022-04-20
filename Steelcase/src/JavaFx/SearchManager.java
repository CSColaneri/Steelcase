package JavaFx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SearchManager {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField cidOne;
    @FXML
    private TextField cidTwo;
    @FXML
    private TextField cidThree;
    @FXML
    private TextField cidFour;
    @FXML
    private TextField cidFive;
    @FXML
    private TextField cidSix;
    @FXML
    private Text successText;

    ArrayList<Integer> submittedInfo = new ArrayList<>();

    public void submitted(ActionEvent e){
        try {
            submittedInfo.add(Integer.parseInt(cidOne.getText()));
            submittedInfo.add(Integer.parseInt(cidTwo.getText()));
            submittedInfo.add(Integer.parseInt(cidThree.getText()));
            submittedInfo.add(Integer.parseInt(cidFour.getText()));
            submittedInfo.add(Integer.parseInt(cidFive.getText()));
            submittedInfo.add(Integer.parseInt(cidSix.getText()));
        } catch (NumberFormatException exception){
            successText.setText("Please enter numbers");
        } catch (Exception exception){
            exception.getCause();
        }
        for(Integer c : submittedInfo){
            System.out.println(c);
        }

        //Setting the text back to empty
        cidOne.setText("");
        cidTwo.setText("");
        cidThree.setText("");
        cidFour.setText("");
        cidFive.setText("");
        cidSix.setText("");

        //check success, maybe some bool?

    }

    @FXML
    public void switchToMain(ActionEvent e)throws IOException {
        System.out.println("Switching to main");
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToClassSearch(ActionEvent e)throws IOException {
        System.out.println("Switching to main");
        root = FXMLLoader.load(getClass().getResource("searchFilter.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToLogin(ActionEvent e)throws IOException{
        System.out.println("Switching to login");
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToCalender(ActionEvent e)throws IOException{
        System.out.println("Switching to calender");
        root = FXMLLoader.load(getClass().getResource("calender.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSchedule(ActionEvent e)throws IOException{
        System.out.println("Switching to Schedule");
        root = FXMLLoader.load(getClass().getResource("schedule.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSearch(ActionEvent e)throws IOException{
        System.out.println("Switching to Search");
        root = FXMLLoader.load(getClass().getResource("search.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToRegister(ActionEvent e) throws IOException{
        System.out.println("Switching to Register");
        root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
