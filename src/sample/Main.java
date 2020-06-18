package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import py4j.GatewayServer;

public class Main extends Application {

    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        GatewayServer gatewayServer =  new GatewayServer(controller);
        gatewayServer.start();
        primaryStage.setTitle("GUI");
        primaryStage.setScene(new Scene(root, 1450
                , 740
        ));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
