package cht.projekt_cht;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket server;
    private ArrayList<ConnectionHandler> connections;
    private boolean shutdown;
    private ExecutorService threadPool;

    public Server() {
        connections = new ArrayList<>();
        shutdown = false;
    }

    @Override
    public void run() {
        try {

            server = new ServerSocket(42565);
            threadPool  = Executors.newCachedThreadPool();
            while (!shutdown) {
                Socket newClient = server.accept();
                ConnectionHandler newHandler = new ConnectionHandler(newClient);
                connections.add(newHandler);
                threadPool.execute(newHandler);
            }
            } catch(Exception e){
            try {
                shutdown();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler cHandler : connections) {
            if (cHandler != null) {
                cHandler.sendMessage(message);
            }
        }
    }

    public void shutdown() throws IOException {
            try {
                shutdown = true;
                if (!server.isClosed()) {
                    server.close();
                }
                for (ConnectionHandler newHandler : connections) {
                    newHandler.shutdown();
                }
            } catch (IOException e) {

            }
    }

    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader input;
        private PrintWriter output;
        private String nickname;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                int state = 0;
                output = new PrintWriter(client.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                //output.println("Please enter nickname: ");
                String message;
                while ((message = input.readLine()) != null && state == 0) {
                    if (message.startsWith("/newu")) {
                        String[] splitMessage3 = message.split(" ", 3);
                        Path fileName = Path.of("/home/pappgr/Java/papga9/projekt/gzihx2_projekt_cht/src/main/java/cht/gzihx2_projekt_cht/" + splitMessage3[1] + ".txt");
                        Files.writeString(fileName, splitMessage3[2]);
                    }
                    String[] splitMessage2 = message.split(" ", 2);
                    try {
                        //File file = new File(splitMessage2[0] + ".txt");
                        File file = new File("/home/pappgr/Java/papga9/projekt/gzihx2_projekt_cht/src/main/java/cht/gzihx2_projekt_cht/" + splitMessage2[0] + ".txt");
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String st = br.readLine();
                        System.out.println(splitMessage2[0]);
                        System.out.println(splitMessage2[1]);
                        if (st.equals(splitMessage2[1])) {
                            state = 2;
                            output.println("Succesful login");
                        } else{
                            state = 3;
                            output.println("Unsuccesful login");
                        }
                    } catch (Exception e) {
                        output.println("wrong username or password");
                    }
                }

                nickname = input.readLine();
                System.out.println(nickname + "connected");
                broadcast(nickname + " has joined.");
                while ((message = input.readLine()) != null) {
                    if (message.startsWith("/changeNick")) {
                        String[] splitMessage = message.split(" ", 2);
                        if (splitMessage.length == 2) {
                            broadcast(nickname + " known as " + splitMessage[1] + " from now on.");
                            nickname = splitMessage[1];
                            output.println("Nickname changed.");
                        } else {
                            output.println("Unacceptable format");
                        }
                    } else if (message.startsWith("/exit") || message.startsWith("/logout") || message.startsWith("/exit")) {
                        broadcast(nickname + " left.");
                        shutdown();
                    } else {
                        broadcast(nickname + ": " + message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }

        public void sendMessage(String message) {
            output.println(message);
        }

        public void shutdown() {
            try {
                input.close();
                output.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e){

            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
