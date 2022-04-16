package JavaFx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FilterSearchManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /*View Schedule ids*/
    @FXML
    private TableColumn<mockUser, Integer> cid;
    @FXML
    private TableColumn<mockUser, Integer> locationRoom;
    @FXML
    private TableColumn<mockUser, String> time;
    @FXML
    private TableColumn<mockUser, String> title;
    @FXML
    private TableColumn<mockUser, String> courseAdd;
    @FXML
    private TableView<mockUser> viewShed;

    public ObservableList<mockUser> list = FXCollections.observableArrayList(
            new mockUser(204, 500, "12:00 PM", "Phyc")
    );

    public void addCourses(){
        //call the course thingy
        for(mockUser u : list){
            if(u.getAdd().isSelected()){
                System.out.println("Adding Course");
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
        cid.setCellValueFactory(new PropertyValueFactory<mockUser, Integer>("cid"));
        locationRoom.setCellValueFactory(new PropertyValueFactory<mockUser, Integer>("locationRoom"));
        time.setCellValueFactory(new PropertyValueFactory<mockUser, String>("time"));
        title.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        courseAdd.setCellValueFactory(new PropertyValueFactory<mockUser, String>("add"));
        viewShed.setItems(list);
    }
}
