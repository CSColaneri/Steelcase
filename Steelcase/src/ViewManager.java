import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Author - Collin Campbell
 * Manages the input to the View Schedule
 */
public class ViewManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /*View Schedule ids*/
    @FXML
    private TableColumn<Course, String> dep;
    @FXML
    private TableColumn<Course, Integer> cid;
    @FXML
    private TableColumn<Course, String> title;
    @FXML
    private TableColumn<Course, String> begin_time;
    @FXML
    private TableColumn<Course, String> end_time;
    @FXML
    private TableColumn<Course, String> locationRoom;
    @FXML
    private TableColumn<Course, String> building;
    @FXML
    private TableView<Course> viewShed;
    @FXML
    private Button login;
    @FXML
    private TableColumn<Course, String> delete;


    public ObservableList<Course> list = FXCollections.observableArrayList();

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

    @FXML
    public void switchToRegister(ActionEvent e) throws IOException {
        System.out.println("Switching to Register");
        root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void saveShed() {
        GuiMain.schedule.saveSchedule(GuiMain.account);
    }

    public void deleteSelected(){
//        for(mockUser mU : list){
//            if(mU.getAdd().isSelected()){
//                int id = mU.getId();
//                try {
//                    GuiMain.schedule.removeCourse(id);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    e.getCause();
//                }
//                list.remove(mU);
//            }
//        }

        ArrayList<Course> removed = new ArrayList<Course>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getAdd().isSelected()){
                int id = list.get(i).getId(); //maybe remove from list after remove from GUI
                list.remove(i);
                i--;
                removed.add(GuiMain.schedule.getCourse(id));
                GuiMain.schedule.removeCourse(id);
            }
        }
        GuiMain.state.add(new State("removeFromSchedule", removed));
        GuiMain.updateState();
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

        System.out.println("view schedule init before adding courses to schedule");
        if(GuiMain.schedule.hasSchedule()) {
            for(Course c : GuiMain.schedule.getSchedule()) {
                list.add(c);
            }
        }
        System.out.println("view schedule init after adding courses to schedule");


        System.out.println("before setCellValueFactories");


        dep.setCellValueFactory(new PropertyValueFactory<Course, String>("department"));
        cid.setCellValueFactory(new PropertyValueFactory<Course, Integer>("code"));
        title.setCellValueFactory(new PropertyValueFactory<Course, String>("short_title"));
        begin_time.setCellValueFactory(new PropertyValueFactory<Course, String>("begin_time"));
        end_time.setCellValueFactory(new PropertyValueFactory<Course, String>("end_time"));
        building.setCellValueFactory(new PropertyValueFactory<Course, String>("building"));
        locationRoom.setCellValueFactory(new PropertyValueFactory<Course, String>("room"));
        delete.setCellValueFactory(new PropertyValueFactory<Course, String>("add"));
        

        System.out.println("after setCellValueFactories");

        System.out.println("Before set items");
        viewShed.setItems(list);
    }
}
