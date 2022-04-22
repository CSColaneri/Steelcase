import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class calendarManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableColumn<mockUser, String> fridayColumn;

    @FXML
    private TableView<mockUser> fridayTable;

    @FXML
    private TableColumn<mockUser, String> mondayColumn;

    @FXML
    private TableView<mockUser> mondayTable;

    @FXML
    private TableColumn<mockUser, String> thursdayColumn;

    @FXML
    private TableView<mockUser> thursdayTable;

    @FXML
    private TableColumn<String, String> timeColumn;

    @FXML
    private TableView<String> timeTable;

    @FXML
    private TableColumn<mockUser, String> tuesdayColumn;

    @FXML
    private TableView<mockUser> tuesdayTable;

    @FXML
    private TableColumn<mockUser, String> wednesdayColumn;

    @FXML
    private TableView<mockUser> wednesdayTable;

    //for the times
    public ObservableList<String> timesList = FXCollections.observableArrayList(
            "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM"
    );

    public ObservableList<mockUser> mondayTimes = FXCollections.observableArrayList(
            new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser()
    );

    public ObservableList<mockUser> tuesdayTimes = FXCollections.observableArrayList(
            new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser()
    );

    public ObservableList<mockUser> wednesdayTimes = FXCollections.observableArrayList(
            new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser()
    );

    public ObservableList<mockUser> thursdayTimes = FXCollections.observableArrayList(
            new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser()
    );

    public ObservableList<mockUser> fridayTimes = FXCollections.observableArrayList(
            new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser(), new mockUser()
    );

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
        timeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        timeTable.setItems(timesList);

        mondayColumn.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        mondayTable.setItems(mondayTimes);
        mondayTable.getItems().get(4).setTitle("CODE");

        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        tuesdayTable.setItems(tuesdayTimes);
        tuesdayTable.getItems().get(2).setTitle("TEST");

        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        wednesdayTable.setItems(wednesdayTimes);

        thursdayColumn.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        thursdayTable.setItems(thursdayTimes);

        fridayColumn.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        fridayTable.setItems(fridayTimes);
    }
}
