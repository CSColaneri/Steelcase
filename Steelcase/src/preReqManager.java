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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class preReqManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /*View Schedule ids*/
    @FXML
    private Button clearBtn;
    @FXML
    private TableColumn<Course, String> day;
    @FXML
    private TableColumn<Course, Integer> cCode;
    @FXML
    private TableColumn<Course, Integer> cid;
    @FXML
    private TableColumn<Course, Integer> locationRoom;
    @FXML
    private TableColumn<Course, String> description;
    @FXML
    private TableColumn<Course, String> startTime;
    @FXML
    private TableColumn<Course, String> professor;
    @FXML
    private TableColumn<Course, String> endTime;
    @FXML
    private TableColumn<Course, String> department;
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
    @FXML
    private Button login;

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
    private String[] choices = {"Professor", "Class Name", "Description", "Code", "Department", "ID", "Day"};

    public ObservableList<Course> list = FXCollections.observableArrayList();

    public void addCourses(ActionEvent e){ //write prereq code here
        //call the course thingy
        ArrayList<Integer> c = new ArrayList<>();
        for(Course u : list){
            if(u.getAdd().isSelected()){
                u.getAdd().setSelected(false);
                System.out.println("Adding course: " + u.getLong_title());
                c.add(u.getId());
            }
        }
        GuiMain.account.addCoursesTaken(c);

        try {
            switchToPreReq(e);
        }catch (IOException xe){
            xe.printStackTrace();
        }
    }

    public void search(){
        //running the search by filters
        Search search = new Search();

        if(choiceBox.getValue() == null) {
            try(Connection conn = DataSource.getConnection()) {
                allCourses = search.loadCourses(conn); //has all the courses
            } catch(Exception e) {
                System.err.println("Failed to grab connection");
                e.printStackTrace();;
            }
        }else{
            clearBtn.setVisible(true);
            ArrayList<Course> filteredCourses = new ArrayList<>();
            //do a filtered search
            System.out.println("Doing a filtered search: " + choiceBox.getValue());
            list.removeAll(allCourses); //refreshed the list to an empty state
            //add the courses that you want to show to the user after the filter search
            if(choiceBox.getValue().equals("Code")){
                System.out.println("Doing a code filtered search");
                for(Course c : allCourses){
                    try {
                        if (c.getCode() == Integer.parseInt(searchText.getText())) {
                            filteredCourses.add(c); //add the class
                        }
                    }catch (NumberFormatException e){
                        System.out.println("User inputted a number, throws exception");
                    }
                }
            }else if(choiceBox.getValue().equals("Professor")){
                System.out.println("Doing a professor filtered search");
                for(Course c : allCourses){
                    if(c.getProfessor() != null && c.getProfessor().contains(searchText.getText())){
                        filteredCourses.add(c); //add the class
                    }
                }
            }else if(choiceBox.getValue().equals("Description")){
                System.out.println("Doing a description filtered search");
                for(Course c : allCourses){
                    if(c.getDescription() != null && c.getDescription().contains(searchText.getText())){
                        filteredCourses.add(c); //add the class
                    }
                }
            }else if(choiceBox.getValue().equals("Class Name")){
                System.out.println("Doing a description class name search");
                for(Course c : allCourses){
                    if(c.getLong_title() != null && c.getLong_title().contains(searchText.getText().toUpperCase(Locale.ROOT))){
                        filteredCourses.add(c); //add the class
                    }
                }
            }else if(choiceBox.getValue().equals("Department")){
                System.out.println("Doing a department name search");
                for(Course c : allCourses){
                    if(c.getDepartment() != null && c.getDepartment().contains(searchText.getText().toUpperCase(Locale.ROOT))){
                        filteredCourses.add(c); //add the class
                    }
                }
            }else if(choiceBox.getValue().equals("ID")){
                System.out.println("Doing a id name search");
                for(Course c : allCourses){
                    try {
                        if (c.getId() == Integer.parseInt(searchText.getText())) {
                            filteredCourses.add(c); //add the class
                        }
                    }catch (NumberFormatException e){
                        System.out.println("User entered a non-number");
                    }
                }
            }else if(choiceBox.getValue().equals("Day")){
                System.out.println("Doing a day search");
                for(Course c : allCourses){
                    if(c.getDay().contains(searchText.getText())){
                        filteredCourses.add(c); //add the class
                    }
                }
            }
            list.addAll(filteredCourses);
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
    public void switchToPreReq(ActionEvent e) throws IOException{
        System.out.println("Switching to Register");
        root = FXMLLoader.load(getClass().getResource("addPreReq.fxml"));
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
        clearBtn.setVisible(false);
        searchText.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        choiceBox.getItems().addAll(choices);
        search(); //fills the allCourses
        list.addAll(allCourses);
        for(Course c : list){
            if(GuiMain.schedule.conflicts(c)){
                c.getAdd().setOpacity(.2);
                c.getAdd().setOnMouseClicked(actionEvent -> {
                    c.getAdd().setSelected(false);
                    System.out.println("Showing the popUp");
                    Button btn = new Button("X");
                    btn.setTranslateX(180);
                    btn.setTranslateY(-80);
                    Text text = new Text("Conflicting course(s) can not added to schedule");
                    Rectangle rect = new Rectangle(400, 200);
                    rect.setFill(Color.WHITE);
                    StackPane sPane = new StackPane();
                    sPane.getChildren().addAll(rect, text, btn);
                    Popup popup = new Popup();
                    popup.getContent().add(sPane);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    popup.show(stage);
                    btn.setOnAction(actionEvent1 ->
                    {
                        popup.hide();
                    });
                });
            }
        }
        cid.setCellValueFactory(new PropertyValueFactory<Course, Integer>("code"));
        day.setCellValueFactory(new PropertyValueFactory<Course, String>("day"));
        locationRoom.setCellValueFactory(new PropertyValueFactory<Course, Integer>("room"));
        startTime.setCellValueFactory(new PropertyValueFactory<Course, String>("begin_time"));
        endTime.setCellValueFactory(new PropertyValueFactory<Course, String>("end_time"));
        title.setCellValueFactory(new PropertyValueFactory<Course, String>("short_title"));
        description.setCellValueFactory(new PropertyValueFactory<Course, String>("description"));
        professor.setCellValueFactory(new PropertyValueFactory<Course, String>("professor"));
        courseAdd.setCellValueFactory(new PropertyValueFactory<Course, String>("add"));
        department.setCellValueFactory(new PropertyValueFactory<Course, String>("department"));
        cCode.setCellValueFactory(new PropertyValueFactory<Course, Integer>("id"));
        viewShed.setItems(list);
    }

    public void reset(){
        choiceBox.setValue(null);
        clearBtn.setVisible(false);
        list.removeAll(allCourses);
        list.addAll(allCourses);
    }
}
