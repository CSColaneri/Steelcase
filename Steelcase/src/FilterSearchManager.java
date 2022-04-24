import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class FilterSearchManager implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    /*View Schedule ids*/
    @FXML
    private TableColumn<Course, Integer> cid;
    @FXML
    private TableColumn<Course, Integer> locationRoom;
    @FXML
    private TableColumn<Course, String> time;
    @FXML
    private TableColumn<Course, String> title;
    @FXML
    private TableColumn<Course, String> courseAdd;
    @FXML
    private TableView<Course> viewShed;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextField searchText;

    private ArrayList<Course> allCourses;

    EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode().equals(KeyCode.ENTER)){
                search();
            }
        }
    };

    //populating choice box
    private String[] choices = {"Professor", "Name", "Description", "Code", "Department"};

    public ObservableList<Course> list = FXCollections.observableArrayList(

    );

    public void addCourses(){
        //call the course thingy
        for(Course u : list){
            if(u.getAdd().isSelected()){

            }
        }
    }

    public void search(){
        //running the search by filters
        Search search = new Search();
        allCourses = search.searchCoursesC(GuiMain.conn); //has all the courses
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

    //root element has been added, this allows us to change it
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchText.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        choiceBox.getItems().addAll(choices);
        search(); //fills the allCourses
        list.addAll(allCourses);
        cid.setCellValueFactory(new PropertyValueFactory<Course, Integer>("id"));
        locationRoom.setCellValueFactory(new PropertyValueFactory<Course, Integer>("room"));
        time.setCellValueFactory(new PropertyValueFactory<Course, String>("begin_time"));
        title.setCellValueFactory(new PropertyValueFactory<Course, String>("short_title"));
        courseAdd.setCellValueFactory(new PropertyValueFactory<Course, String>("add"));
        viewShed.setItems(list);
    }
}
