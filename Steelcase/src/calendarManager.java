
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

        //adding empty text boxes that will be filled to each day 7 for mwf and 5 for
        for(int i = 0; i < 10; i++){
            monday.getChildren().add(new Text("\n"));
            friday.getChildren().add(new Text("\n"));
            wednesday.getChildren().add(new Text("\n"));
        }

        for(int i = 0; i < 10; i++){
            tuesday.getChildren().add(new Text("\n"));
            thursday.getChildren().add(new Text("\n"));
        }

        for(Course c:GuiMain.schedule.getSchedule()) {
            if(c.getDay().contains("M")) {
               //get the time
                int index = mapIntStartTime(Integer.parseInt(c.getBegin_time().split(":")[0]));
                Text text = (Text) monday.getChildren().get(index + 1);
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
            }
            if(c.getDay().contains("T")) {
                int index = mapIntStartTime(Integer.parseInt(c.getBegin_time().split(":")[0]));
                Text text = (Text) tuesday.getChildren().get(index + 1);
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
            }
            if(c.getDay().contains("W")) {
                //get the time
                int index = mapIntStartTime(Integer.parseInt(c.getBegin_time().split(":")[0]));
                Text text = (Text) wednesday.getChildren().get(index + 1);
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
            }
            if(c.getDay().contains("R")) {
                //get the time
                int index = mapIntStartTime(Integer.parseInt(c.getBegin_time().split(":")[0]));
                Text text = (Text) thursday.getChildren().get(index + 1);
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
            }
            if(c.getDay().contains("F")) {
                //get the time
                int index = mapIntStartTime(Integer.parseInt(c.getBegin_time().split(":")[0]));
                Text text = (Text) friday.getChildren().get(index + 1);
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
            }
        }
    }

    public void setText(Text t, String longTitle, String beginTime, String endTime, String professor, String room){
        t.setText(longTitle + "\n" + beginTime + "-" + endTime + "\n" + professor +"\nRoom: " + room);
        t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 10));
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

    public int mapIntStartTime(int time){
        int ret = 0;
        switch (time) {
            case 8:
                ret = 0;
                break;
            case 9:
                ret = 1;
                break;
            case 10:
                ret = 2;
                break;
            case 11:
                ret = 3;
                break;
            case 12:
                ret = 4;
                break;
            case 13:
                ret = 5;
                break;
            case 14:
                ret = 6;
                break;
            case 15:
                ret = 7;
                break;
            case 16:
                ret = 8;
                break;
            case 0:
                ret = 9;
                break;
            default:
                ret = 9;
        }
        System.out.println("Returning: "  + ret);
        return ret;
    }
}
