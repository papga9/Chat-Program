package cht.projekt_cht;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SceneController implements Runnable {

    private Socket client;
    private BufferedReader input;
    private PrintWriter output ;
    private boolean shutdown;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private InputHandler inHandler;

    @FXML
            private Text myText;
    @FXML
    private TextField myTextField;

    @FXML
    private Button myButton;
    @FXML
    private TextFlow myFlowText;
    @FXML
    private Button registerButton;
    @FXML
    private Button sendButton;
    @FXML
    private TextField chatTextField;

    private int accesStage = 0;
    private String credentials;
    private String nickname;

        //client.run();

    public void checkUsername(ActionEvent event) throws IOException {
        System.out.println(accesStage);
        try {
                switch (accesStage) {
                    case 0:
                        credentials = myTextField.getText();
                        nickname = myTextField.getText();
                        myText.setText("Enter Password");
                        myTextField.clear();
                        accesStage = 1;
                        break;
                    case 1:
                        credentials += " " + myTextField.getText();
                        connect();
                        output.println(credentials);
                        myText.setText("Do you wish to connect?");
                        myButton.setText("Yes");
                        myTextField.setText("");
                        myTextField.clear();
                        accesStage = 4;
                        break;
                    case 2:
                        credentials = "/newu " + myTextField.getText();
                        myText.setText("Enter new password");
                        accesStage = 1;
                        break;
                    /*case 3:
                        credentials += " " + myTextField.getText();
                        connect();
                        output.println(credentials);
                        accesStage = 4;
                        break;*/
                    case 4:
                        System.out.println("halo");
                        output.println("nickname");
                        accesStage = 5;
                        this.run();
                    default:
                        break;
                }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public void sendNickname(String nickname){
        output.println(nickname);
    }
    public void changeToRegister(ActionEvent event) {
        myText.setText("Enter new username");
        accesStage = 2;
    }
    public void sendMessage(ActionEvent evet) {
        if (accesStage == 5) output.println(nickname);
        output.println(chatTextField.getText());
    }

    public void connect() {
        try {
            Socket client = new Socket("127.0.0.1", 42565);
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            inHandler = new InputHandler();
            System.out.println("asd");
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown(){
        shutdown = true;
        try {
            input.close();;
            output.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        Thread thread = new Thread(inHandler);
        thread.start();
        System.out.println("hallo");


        /*String inmessage = null;
        while (true) {
            try {
                if (!((inmessage = input.readLine()) != null)) break;
                else {System.out.println("heheh");}
            } catch (IOException e) {
                shutdown();
            }
            System.out.println(inmessage);
        }*/
    }

    class InputHandler implements Runnable {

        @Override
        public void run() {
            System.out.println("halllo");
            /*try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                while (!shutdown) {
                    String message = in.readLine();
                    if (message.equals("/exit") || message.equals("/logout") || message.equals("/quit")) {
                        in.close();
                        shutdown();
                    } else {
                        output.println(message);

                    }
                }
            } catch (IOException e) {
                shutdown();
            }*/
            try {
                String inmessage;
                while ((inmessage = input.readLine()) != null) {
                    System.out.println(inmessage);
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }
}



/*
public class Client implements Runnable{

    private Socket client;
    private BufferedReader input;
    private PrintWriter output ;
    private boolean shutdown;

    @Override
    public void run() {
        try {
            Socket client = new Socket("127.0.0.1", 42565);
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            InputHandler inHandler = new InputHandler();
            Thread thread = new Thread(inHandler);
            thread.start();

            String inmessage;
            while ((inmessage = input.readLine()) != null) {
                System.out.println(inmessage);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown(){
        shutdown = true;
        try {
            input.close();;
            output.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {

        }
    }

    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                while (!shutdown) {
                    String message = in.readLine();
                    if (message.equals("/exit") || message.equals("/logout") || message.equals("/quit")) {
                        in.close();
                        shutdown();
                    } else {
                        output.println(message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }
 */