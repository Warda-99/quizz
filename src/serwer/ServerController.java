package serwer;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class ServerController {
    @FXML private TextArea quiz_field;

    private BlockingQueue<Socket> blockingQueue;
    private int counter;
    private Iterator<Map.Entry<String, String>> iterator;
    private Map.Entry<String, String> current;

    int i = 1;


    @FXML
    public void initialize() {
        quiz_field.setEditable(false);

        this.readQuestions();
    }

    public void setBlockingQueue(BlockingQueue<Socket> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void consumerStart() {
        Thread thread = new Thread(new Runnable() {
            private Socket socket;

            @Override
            public void run() {
                try {
                    while (counter >= 0) {
                        socket = blockingQueue.take();
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        sample.Answer answer = ((sample.Answer) in.readObject());

                        printInfo(answer.getNick() + ": " + answer.getAnswer());

                        if (answer.getAnswer().equalsIgnoreCase(getReply())) {
                            i++;
                            printInfo("Poprawna odpoweidz\n");
                            nextReply();
                            if (counter >= 0) {
                                printInfo("Nr " + i + ") " + getQuestion());
                            }
                        }
                    }
                } catch (InterruptedException | IOException | ClassNotFoundException e) {

                }
                printInfo("KONIEC!");
            }
        });
        thread.start();
    }

    private void nextReply() {
        if (this.iterator.hasNext()) {
            current = iterator.next();
        }
        this.counter--;
    }

    private String getReply() {
        return current.getKey();
    }

    private String getQuestion() {
        return current.getValue();
    }

    private void printInfo(String s) {
        quiz_field.appendText(s + "\n");
    }

    private void readQuestions() {
        Map<String, String> map = new LinkedHashMap<>();
        Path path = Paths.get("C:\\Users\\0nsid\\IdeaProjects", "pytania.txt");
        System.out.println(path);
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> map.putIfAbsent(line.split(":")[1], line.split(":")[0]));
        } catch (IOException e) {
            System.out.println("Problem z wczytaniem pytan");
        }
        this.counter = map.size();
        iterator = map.entrySet().iterator();
        nextReply();
        this.printInfo("Nr " + i + ") " + getQuestion());
    }
}
