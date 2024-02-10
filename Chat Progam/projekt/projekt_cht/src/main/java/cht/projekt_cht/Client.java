package cht.projekt_cht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    public void test() {
        System.out.println("keksz");
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

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}


