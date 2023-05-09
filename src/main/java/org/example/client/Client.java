package org.example.client;

import org.example.common.FileConfigReading;
import org.example.common.Logger;
import org.example.common.Side;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;

public class Client {

    public static ThreadGroup threadGroup = new ThreadGroup("ClientSide");

    public static Thread mainThread;
    public static String userName;
    public static boolean online;
    public static boolean isTest;

    public static Queue<String> testMessages;

    public static final Side side = Side.CLIENT;
    private ChatServer chatServer;

    private String HOST;
    private int PORT;

    private Logger.LogLevel level;

    public Client(){
        this(false);
    }

    public Client(boolean isTest){
        this.isTest = isTest;
        if(isTest){
            testMessages = new LinkedList<>() {
            };
        }
        mainThread = new Thread(this::clientStart);
        mainThread.start();
    }

    public static void main(String[] args) {
        new Client();
    }

    private void clientStart(){
        Logger.getLogger().logging("Starting client", Logger.LogLevel.INFO, side);
        Properties properties = FileConfigReading.read(side.name().toLowerCase());
        HOST = properties.getProperty("server");
        PORT = Integer.parseInt(properties.getProperty("port"));
        level = Logger.LogLevel.valueOf(properties.getProperty("logLevel"));

        Logger.getLogger().setLogLevel(level);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Hello enter your name: ");
        userName = scanner.nextLine();

        Logger.getLogger().logging("You entered name: " +  userName, Logger.LogLevel.DEBUG, side);
        online = true;
        connectToServer();
    }

    private void connectToServer() {

        try {
            Socket clientSocket = new Socket(HOST, PORT);

            chatServer = new ChatServer(clientSocket);
            ChatHandler handler = new ChatHandler(chatServer);


            stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop(){
        online = false;
        threadGroup.interrupt();
        try {
            chatServer.closeConnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Logger.getLogger().logging("Client stopped", Logger.LogLevel.INFO, side);
    }
}
