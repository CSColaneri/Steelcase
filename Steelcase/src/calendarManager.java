
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
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
    private Button cAdd;

    @FXML
    private Button export;

    @FXML
    private GridPane gridPaneCalendar;

    @FXML
    private Button login;

    @FXML
    private Button save;


    //ERIC right here
    public void export(ActionEvent e){
        System.out.println("exporting");
        GuiMain.schedule.export("scheduler.txt");
    }

    //ERIC right here
    public void addNewCustomCourse(ActionEvent e){
        Popup popup = new Popup();
        StackPane sp = new StackPane();
        TextField Description = new TextField();
        TextField Days = new TextField();
        TextField Title = new TextField();
        TextField bTime = new TextField();
        TextField eTime = new TextField();
        Button accept = new Button("Add");
        Button cancel = new Button("Cancel");
        Rectangle rect = new Rectangle(400, 400);
        rect.setFill(Color.WHITE);

        Description.setPromptText("Description");
        Days.setPromptText("MTWRF");
        Title.setPromptText("Name");
        bTime.setPromptText("Start Time (8:00:00)");
        eTime.setPromptText("End Time (8:00:00)");

        accept.setTranslateX(350);
        accept.setTranslateY(355);
        cancel.setTranslateX(280);
        cancel.setTranslateY(355);

        Title.setTranslateX(25);
        Title.setTranslateY(50);

        Description.setTranslateX(25);
        Description.setTranslateY(110);

        Days.setTranslateX(25);
        Days.setTranslateY(170);

        bTime.setTranslateX(25);
        bTime.setTranslateY(230);

        eTime.setTranslateX(25);
        eTime.setTranslateY(290);


        popup.getContent().addAll(sp, rect,Description, Days, Title, bTime, eTime, accept, cancel);
        Stage stage = (Stage) ((Node) e.getSource()).getParent().getParent().getScene().getWindow();
        popup.show(stage);

        cancel.setOnAction(actionEvent -> {
            popup.hide();
        });

        //put adding new course code here Eric!
        accept.setOnAction(actionEvent -> {
            String title = Title.getText();
            String des = Description.getText();
            String btime = bTime.getText();
            String etime = eTime.getText();
            String days = Days.getText();
            GuiMain.schedule.addCustom(title, des, days, btime, etime);
            System.out.println(GuiMain.schedule.toString());

            popup.hide();
            try {
                switchToCalender(e);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        });


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
        if(!GuiMain.loggedIn){
            save.setDisable(true);
        }

        //placing all the times in the grid pane
        for(int i = 0; i <= 11; i++){
            String time =  i > 4 ? ((8 + i) % 12) + " PM" : ((8 + i) % 12) + " AM";
            if(time.equals("0 AM")){
                time = "12 PM";
            }
            gridPaneCalendar.add(new Text(time), 0, (i+1));
        }

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

        int index = 1;
        //placing the classes in the right designated tiles
        for(Course c : GuiMain.schedule.getSchedule()){
            int startTime = -1;
            if(c.getBegin_time() != null) {
                startTime = Integer.parseInt(c.getBegin_time().split(":")[0]);
            }
            if(c.getDay().contains("M")){
                Text text = new Text();
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
                gridPaneCalendar.add(text, 1, mappingToGridPane(startTime));
            }
            if(c.getDay().contains("T")){
                Text text = new Text();
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
                gridPaneCalendar.add(text, 2, mappingToGridPane(startTime));
            }
            if(c.getDay().contains("W")){
                Text text = new Text();
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
                gridPaneCalendar.add(text, 3, mappingToGridPane(startTime));
            }
            if(c.getDay().contains("R")){
                Text text = new Text();
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
                gridPaneCalendar.add(text, 4, mappingToGridPane(startTime));
            }
            if(c.getDay().contains("F")){
                Text text = new Text();
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
                gridPaneCalendar.add(text, 5, mappingToGridPane(startTime));
            }
            if(c.getDay().equals(null) || c.getDay().equals("")){
                Text text = new Text();
                setText(text, c.getLong_title(), c.getBegin_time(), c.getEnd_time(), c.getProfessor(), c.getRoom());
                gridPaneCalendar.add(text, 6, index++);
            }
        }

    }

    public int mappingToGridPane(int startTime) {
        int returnTime;

        switch (startTime) {
            case 8:
                returnTime = 1;
                break;
            case 9:
                returnTime = 2;
                break;
            case 10:
                returnTime = 3;
                break;
            case 11:
                returnTime = 4;
                break;
            case 12:
                returnTime = 5;
                break;
            case 13:
                returnTime = 6;
                break;
            case 14:
                returnTime = 7;
                break;
            case 15:
                returnTime = 8;
                break;
            case 16:
                returnTime = 9;
                break;
            case 17:
                returnTime = 11;
                break;
            default:
                returnTime = 12;
        }
        return returnTime;
    }

    public void setText(Text t, String longTitle, String beginTime, String endTime, String professor, String room){
        t.setText(longTitle + "\n" + professor +"\nRoom: " + room);
        t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 10));
    }

    public void saveShed(ActionEvent e){
        if(GuiMain.schedule.saveSchedule(GuiMain.account)){
            System.out.println("Showing saved popup");
            Popup popup = new Popup();
            StackPane sp = new StackPane();
            Button btn = new Button("X");
            Text text = new Text("Saved!");
            Rectangle rect = new Rectangle(400, 200);
            btn.setTranslateX(180);
            btn.setTranslateY(-80);
            rect.setFill(Color.WHITE);
            sp.getChildren().addAll(rect, text,btn);

            popup.getContent().add(sp);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            popup.show(stage);

            btn.setOnAction(actionEvent -> {
               popup.hide();
            });
        }else{
            System.out.println("Showing saved popup");
            Popup popup = new Popup();
            StackPane sp = new StackPane();
            Button btn = new Button("X");
            Text text = new Text("Failed, do you have an internet connection?");
            Rectangle rect = new Rectangle(400, 200);
            btn.setTranslateX(180);
            btn.setTranslateY(-80);
            rect.setFill(Color.WHITE);
            sp.getChildren().addAll(rect, text,btn);

            popup.getContent().add(sp);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            popup.show(stage);

            btn.setOnAction(actionEvent -> {
                popup.hide();
            });
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
