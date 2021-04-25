package serwer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server extends Application {

    private ServerController controller;
    private static final int PORT = 7777;

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("serverWindow.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setTitle("Server");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BlockingQueue<Socket> blockingQueue = new ArrayBlockingQueue<>(2);
        controller.setBlockingQueue(blockingQueue);
        controller.consumerStart();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket s = new ServerSocket(PORT)) {
                    while (true) {
                        Socket socket = s.accept(); // oczekiwanie na nadejscie polaczenia
                        blockingQueue.put(socket);  // dodaj przychodzace odpowiedzi do kolejki
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        });
        thread.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
