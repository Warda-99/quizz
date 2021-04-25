package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientController {

    @FXML private TextField tf_answer;
    @FXML private TextField tf_nick;

    InetAddress address;
    static final int PORT = 7777;

    @FXML
    public void initialize() {

    }

    @FXML
    private void sendAnswer(ActionEvent event) {
        try (Socket socket = new Socket("localhost", PORT)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Answer answer = new Answer(tf_answer.getText(), tf_nick.getText());
            out.writeObject(answer);
            this.tf_answer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
