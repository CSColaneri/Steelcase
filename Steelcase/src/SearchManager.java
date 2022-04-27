import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button addButton;
    @FXML
    private TextField code1;
    @FXML
    private TextField code2;
    @FXML
    private TextField code3;
    @FXML
    private TextField code4;
    @FXML
    private TextField code5;
    @FXML
    private TextField code6;
    @FXML
    private Button courseSearchBtn;
    @FXML
    private TextField dep1;
    @FXML
    private TextField dep2;
    @FXML
    private TextField dep3;
    @FXML
    private TextField dep4;
    @FXML
    private TextField dep5;
    @FXML
    private TextField dep6;
    @FXML
    private Button login;
    @FXML
    private TextField sec1;
    @FXML
    private TextField sec2;
    @FXML
    private TextField sec3;
    @FXML
    private TextField sec4;
    @FXML
    private TextField sec5;
    @FXML
    private TextField sec6;
    @FXML
    private Text successText;

    ArrayList<Integer> submittedInfo = new ArrayList<>();

    public void submitted(ActionEvent e) {
        successText.setText("");
        ArrayList<String[]> courses = new ArrayList<>();
        ArrayList<Boolean> successes = new ArrayList<>(6);
        successes.forEach(b -> {
            b = true;
        });
        String[] course1 = new String[3];
        String[] course2 = new String[3];
        String[] course3 = new String[3];
        String[] course4 = new String[3];
        String[] course5 = new String[3];
        String[] course6 = new String[3];
        if(!dep1.getText().isBlank() && !code1.getText().isBlank() && !sec1.getText().isBlank()) {
            course1[0] = dep1.getText();
            course1[1] = code1.getText();
            course1[2] = sec1.getText();
            courses.add(course1);
        }
        if(!dep2.getText().isBlank() && !code2.getText().isBlank() && !sec2.getText().isBlank()) {
            course2[0] =  dep2.getText();
            course2[1] = code2.getText();
            course2[2] =  sec2.getText();
            courses.add(course2);
        }
        if(!dep3.getText().isBlank() && !code3.getText().isBlank() && !sec3.getText().isBlank()) {
            course3[0] =  dep3.getText();
            course3[1] = code3.getText();
            course3[2] =  sec3.getText();
            courses.add(course3);
        }
        if(!dep4.getText().isBlank() && !code4.getText().isBlank() && !sec4.getText().isBlank()) {
            course4[0] =  dep4.getText();
            course4[1] = code4.getText();
            course4[2] =  sec4.getText();
            courses.add(course4);
        }
        if(!dep5.getText().isBlank() && !code5.getText().isBlank() && !sec5.getText().isBlank()) {
            course5[0] =  dep5.getText();
            course5[1] = code5.getText();
            course5[2] =  sec5.getText();
            courses.add(course5);
        }
        if(!dep6.getText().isBlank() && !code6.getText().isBlank() && !sec6.getText().isBlank()) {
            course6[0] =  dep6.getText();
            course6[1] = code6.getText();
            course6[2] =  sec6.getText();
            courses.add(course6);
        }
        
        
        for(String[] c : courses) {
            successes.add(GuiMain.addByDepCodeSec(c));
        }

        //Setting the text back to empty
        dep1.setText("");
        code1.setText("");
        sec1.setText("");
        dep2.setText("");
        code2.setText("");
        sec2.setText("");
        dep3.setText("");
        code3.setText("");
        sec3.setText("");
        dep4.setText("");
        code4.setText("");
        sec4.setText("");
        dep5.setText("");
        code5.setText("");
        sec5.setText("");
        dep6.setText("");
        code6.setText("");
        sec6.setText("");
        //check success, maybe some bool?
        for(int i = 0; i < successes.size(); ++i) {
            Boolean b = successes.get(i);
            if(!b) {
                successText.setText(
                    successText.getText()
                    +String.format("\nFailed to add %s %s %s",courses.get(i)[0],courses.get(i)[1],courses.get(i)[2]));
            }
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (GuiMain.loggedIn) {
            try {
                login.setText("Account");
            }catch (Exception e){
                e.getCause();
            }
            login.setOnAction(actionEvent -> {
                System.out.println("Switching to Register");
                try {
                    root = FXMLLoader.load(getClass().getResource("account.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            });
        }
    }
}
