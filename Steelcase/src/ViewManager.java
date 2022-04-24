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
import java.util.ResourceBundle;

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
    private TableColumn<mockUser, String> dep;
    @FXML
    private TableColumn<mockUser, Integer> cid;
    @FXML
    private TableColumn<mockUser, String> title;
    @FXML
    private TableColumn<mockUser, String> begin_time;
    @FXML
    private TableColumn<mockUser, String> end_time;
    @FXML
    private TableColumn<mockUser, String> locationRoom;
    @FXML
    private TableColumn<mockUser, String> building;
    @FXML
    private TableView<mockUser> viewShed;
    @FXML
    private Button login;

    public ObservableList<mockUser> list = FXCollections.observableArrayList();

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
        if (GuiMain.schedule.hasSchedule()) {
            for (Course c : GuiMain.schedule.getSchedule()) {
                list.add(new mockUser(c));
            }
        }
        System.out.println("view schedule init after adding courses to schedule");


        System.out.println("before setCellValueFactories");

        dep.setCellValueFactory(new PropertyValueFactory<mockUser, String>("dep"));
        cid.setCellValueFactory(new PropertyValueFactory<mockUser, Integer>("cid"));
        title.setCellValueFactory(new PropertyValueFactory<mockUser, String>("title"));
        begin_time.setCellValueFactory(new PropertyValueFactory<mockUser, String>("begin_time"));
        end_time.setCellValueFactory(new PropertyValueFactory<mockUser, String>("end_time"));
        building.setCellValueFactory(new PropertyValueFactory<mockUser, String>("building"));
        locationRoom.setCellValueFactory(new PropertyValueFactory<mockUser, String>("locationRoom"));

        System.out.println("after setCellValueFactories");

        System.out.println("Before set items");
        viewShed.setItems(list);
    }
}
