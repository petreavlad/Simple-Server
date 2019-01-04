package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Controller {

    public Button ipButton;
    public TextArea outputPanel;
    public Button serverStartButton;
    public TextField portTextBox;

    private boolean wasServerStarted = false;

    @FXML
    private void initialize() {
        outputPanel.setFocusTraversable(false);
    }

    @FXML
    private void showIpDialogue() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("This is your ip");
        String ip = "Ip could not be retrieved.";
        try {
            ip = "Your ip adress is: " + InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        alert.setHeaderText(null);
        alert.setContentText(ip);
        alert.showAndWait();
    }

    @FXML
    private void startServer() {
        String input = portTextBox.getText();
        if (input.matches("\\d{4}")) {
            if (!wasServerStarted) {
                wasServerStarted = true;
                clearLog();
                printOutText("\nServer will start shortly!", false);
                ConnectionHandler handler=new ConnectionHandler(Integer.parseInt(input),listener);
                handler.start();
            } else {
                printOutText("\nServer was already started!", false);
            }

        } else {
            printOutText("\nThis is not a valid port!", true);
        }
    }

    private void printOutText(String output, boolean error) {
        if (error) {
            outputPanel.setStyle("-fx-text-fill: red;");
        } else {
            outputPanel.setStyle(null);
        }
        String text = outputPanel.getText();
        text +="\n"+output;
        outputPanel.setText(text);
    }

    @FXML
    private void clearLog() {
        outputPanel.setText("");
    }

    private MessageListener listener=new MessageListener() {
        @Override
        public void onMessageReceived(String text) {
            printOutText(text,false);
        }
    };

}
