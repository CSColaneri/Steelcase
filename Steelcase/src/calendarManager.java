
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class calendarManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private VBox friday;

    @FXML
    private VBox monday;

    @FXML
    private VBox thursday;

    @FXML
    private VBox tuesday;

    @FXML
    private VBox wednesday;

    @FXML
    private Button login;

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

        Collections.sort(GuiMain.schedule.getSchedule(), calendarManager.courseComparator);
        for(Course c : GuiMain.schedule.getSchedule()){
            System.out.println(c.getIntStartTime());
        }

        for(Course c:GuiMain.schedule.getSchedule()) {
            if(c.getDay().contains("M")) {
                Text t = new Text(c.getLong_title() + "\n" + c.getBegin_time() + "-" + c.getEnd_time() + "\n" + c.getProfessor() +"\nRoom: " + c.getRoom());
                t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                monday.getChildren().add(t);
            }
            if(c.getDay().contains("T")) {
                Text t = new Text(c.getLong_title() + "\n" + c.getBegin_time() + "-" + c.getEnd_time() + "\n" + c.getProfessor() +"\nRoom: " + c.getRoom());
                t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                tuesday.getChildren().add(t);
            }
            if(c.getDay().contains("W")) {
                Text t = new Text(c.getLong_title() + "\n" + c.getBegin_time() + "-" + c.getEnd_time() + "\n" + c.getProfessor() +"\nRoom: " + c.getRoom());
                t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                wednesday.getChildren().add(t);
            }
            if(c.getDay().contains("R")) {
                Text t = new Text(c.getLong_title() + "\n" + c.getBegin_time() + "-" + c.getEnd_time() + "\n" + c.getProfessor() +"\nRoom: " + c.getRoom());
                t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                thursday.getChildren().add(t);
            }
            if(c.getDay().contains("F")) {
                Text t = new Text(c.getLong_title() + "\n" + c.getBegin_time() + "-" + c.getEnd_time() + "\n" + c.getProfessor() +"\nRoom: " + c.getRoom());
                t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                friday.getChildren().add(t);
            }
        }
    }

    public static Comparator<Course> courseComparator = new Comparator<Course>() {
        public int compare(Course s1, Course s2)
        {

            int c1 = s1.getIntStartTime();
            int c2 = s2.getIntStartTime();


            // For descending order
            return c2-c1;
        }
    };
}
