import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.SQLException;

public class GuiMain extends Application{
    public static Account account;
    public static Schedule schedule = new Schedule();
    public static boolean loggedIn = false;
    public static Connection conn = null;

    public static void main(String[] args) {
        try{
            conn = DataSource.getConnection();
        }catch (Exception e){
            e.getCause();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("Hi");
                try{
                    conn.close();
                }catch (Exception ex){
                    ex.getCause();
                }
                System.exit(0);
            }
        });

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Main");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
