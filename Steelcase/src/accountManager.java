import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.http.auth.InvalidCredentialsException;

public class accountManager implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text accountHolderText;

    @FXML
    private Button cPBtn;

    @FXML
    private Button dAccountBtn;

    @FXML
    private Text emailText;

    @FXML
    private Button login;

    private Popup       popup;
    private Button      popupDeleteBttn;
    private Button      popupCancelBttn;
    private Text        popupErrorText;
    private Text        popupText;
    private PasswordField popupPField;
    private HBox        popupHBox;
    private VBox        popupVBox;
    private StackPane   popupSP;
    private Rectangle   popupRect;
    private Stage       popupOrigin;

    public void setAccountHolderText(String accountHolder){
        accountHolderText.setText("Account Holder: " + accountHolder);
    }

    public void setEmailText(String email){
        emailText.setText("Email: " + email);
    }

    private boolean delete(Popup popup, String password) {
        StackPane sp = (StackPane) popup.getContent().get(0);
        System.out.println("Attempt to delete the account");
        try {
            boolean deleted = Account.deleteAccount(GuiMain.account, password);
            popup.hide();
            return deleted;
        } catch(InvalidCredentialsException ex) {
            // normal throw
            System.out.println("Failed account deletion: invalid password.");
            ((Text)sp.lookup("#errorText")).setText("Incorrect password!");
            sp.lookup("#errorText").setVisible(true);
        } catch(Exception ex) {
            // something is wrong.
            ((Text)sp.lookup("#errorText")).setText("Something went wrong, try again later.");
            sp.lookup("#errorText").setVisible(true);
            ex.printStackTrace();
        }
        return false;
    }

    private void cancel(Popup popup) {
        System.out.println("Don't delete");
        popup.hide();
    }

    public void dAccount(ActionEvent e){
        // reset error text
        ((Text)popup.getContent().get(0).lookup("#errorText")).setText("");

        if(popup.isShowing()) {
            popup.hide();
        } else {
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            popupOrigin = stage;
            popup.show(stage);
        }
    }

    private void createPopup() {
        System.out.println("Create Account Deletion Confirmation Popup");

        double rectX = 300;
        double rectY = 100;
        
        // BorderPane bp = new BorderPane();
        this.popupText = new Text("Confirm your password to delete your account.\n"
            + "This will delete your schedule and log you out.");
        this.popupErrorText = new Text();
        this.popupDeleteBttn = new Button("Delete Account");
        this.popupPField = new PasswordField();
        this.popupCancelBttn = new Button("x");
        this.popupHBox = new HBox();
        this.popupVBox = new VBox();
        this.popup = new Popup();
        this.popupSP = new StackPane();
        this.popupRect = new Rectangle(rectX, rectY);
        popupRect.setFill(Color.WHITE);

        // StackPane sPane2 = new StackPane(cancel);
        // sPane2.setAlignment(Pos.CENTER_RIGHT);
        // bp.setTop(sPane2);//sets cancel button in top of border pane
        // bp.center is set lower, after forming the other elements

        popupPField.setPromptText("Your Password");

        popupDeleteBttn.setPrefSize(100, 20);
        // set delete action on popup
        popupDeleteBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String pword = popupPField.getText();
                popupPField.setText("");
                if(delete(popup, pword)) {
                    // log out
                    logout(popupOrigin);
                }
            }
        });

        popupCancelBttn.setPrefSize(20, 20);
        // fine tune to the top right corner with a little space
        popupCancelBttn.setTranslateX(rectX*0.5-20);
        popupCancelBttn.setTranslateY(-(rectY*0.5-20));
        // set cancel action on popup
        popupCancelBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                cancel(popup);
            }
        });

        popupHBox.setPadding(new Insets(10, 12, 0, 12));
        popupHBox.setSpacing(10);
        popupHBox.getChildren().addAll(popupPField, popupDeleteBttn);

        popupErrorText.setVisible(false);
        // errorText.setText("Howdy");
        popupErrorText.setFill(Color.RED);
        popupErrorText.setId("errorText");

        popupVBox.getChildren().addAll(popupText, popupErrorText, popupHBox);
        popupVBox.setPadding(new Insets(10, 12, 0, 12));

        popupSP.getChildren().addAll(popupRect, popupVBox, popupCancelBttn);

        // popup.setAutoHide(true);
        // popup.consumeAutoHidingEventsProperty().set(true);
        popup.getContent().add(popupSP);
        
        // do a popup warning and asking for confirmation

    }

    public void cPBtn(ActionEvent e){
        System.out.println("Changing the password");
        // maybe do a popup
        // change password
    }

    public void logout(ActionEvent e){
        GuiMain.account = null;
        GuiMain.loggedIn = false;

        GuiMain.schedule = new Schedule();

        try {
            switchToMain(e);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void logout(Stage e){
        GuiMain.account = null;
        GuiMain.loggedIn = false;

        GuiMain.schedule = new Schedule();

        try {
            switchToMain(e);
        }catch (Exception ex){
            ex.printStackTrace();
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
    
    public void switchToMain(Stage stage)throws IOException {
        System.out.println("Switching to main");
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        // stage = (Stage)((Node)e.getSource()).getScene().getWindow();
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

    @FXML
    public void switchToPreReq(ActionEvent e) throws IOException{
        System.out.println("Switching to Register");
        root = FXMLLoader.load(getClass().getResource("addPreReq.fxml"));
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
        setAccountHolderText(GuiMain.account.getFirstName() + " " + GuiMain.account.getLastName());
        setEmailText(GuiMain.account.getEmail());
        createPopup();

    }
}
