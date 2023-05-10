package org.example.server;

import org.example.common.FileConfigReading;
import org.example.common.Logger;
import org.example.common.Side;

import java.util.Properties;
import java.util.Scanner;

public class Server {

    private static Server INSTANCE;

    private ThreadGroup serverThreads = new ThreadGroup("ServerThreads");
    public ThreadGroup clientsThreads = new ThreadGroup("ClientsThreads");
    private Thread connectThread;
    private Thread chatThread;

    private Thread handleThread;

    public boolean online;

    public Logger.LogLevel level  = Logger.LogLevel.INFO;

    public Side side = Side.SERVER;

    public int PORT;


    public Server(boolean isTest){
        new Thread(()-> initServer(isTest)).start();
    }

    public Server(){
        this(false);
    }

    public static Server getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args) {
        new Server();
    }

    private void initServer(boolean isTest){
        Server.INSTANCE = this;
        Logger.getLogger().logging("Starting the server", Logger.LogLevel.DEBUG, side);

        Properties properties = FileConfigReading.read(side.name().toLowerCase());
        PORT = Integer.parseInt(properties.getProperty("port"));
        level = Logger.LogLevel.valueOf(properties.getProperty("logLevel"));


        Logger.getLogger().setLogLevel(level);

        online = true;
        start();

        while (!connectThread.isAlive() || !handleThread.isAlive() || !chatThread.isAlive()){

        }

        Logger.getLogger().logging("Server started", Logger.LogLevel.INFO, side);

        if(!isTest) {
            commandHandler();
        }

        while (serverThreads.activeCount() != 0 || clientsThreads.activeCount() != 0){

        }
        Logger.getLogger().logging("Server stopped", Logger.LogLevel.INFO, side);
    }

    public void start() {
        connectThread = new Thread(serverThreads, new ConnectThread());
        connectThread.start();

        handleThread = new Thread(serverThreads, new HandleThread());
        handleThread.start();

        chatThread = new Thread(serverThreads, new ChatThread());
        chatThread.start();
    }

    public void stop(){
        Logger.getLogger().logging("Stopping the server", Logger.LogLevel.INFO, side);

        online = false;

        serverThreads.interrupt();

        clientsThreads.interrupt();

        ConnectThread.clients.forEach(ChatClient::close);

        ConnectThread.closeConnection();
    }

    public void commandHandler(){
        Scanner inCommand = new Scanner(System.in);
        while (online){
            String command = inCommand.nextLine();
            if(command.equals("stop")){
                stop();
                break;
            }
            Logger.getLogger().logging("Unknown command: " + command, Logger.LogLevel.INFO, side);
        }
    }


}
