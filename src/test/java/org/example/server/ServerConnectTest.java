package org.example.server;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnectTest {


    @Test
    void Connect(){
        Server server = new Server(true);

        while (!server.online){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try(Socket clientSocket = new Socket("localhost", server.PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner in = new Scanner(clientSocket.getInputStream());){

            Assertions.assertTrue(clientSocket.isConnected());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void joinToChat(){
        Server server = new Server(true);

        while (!server.online){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try(Socket clientSocket = new Socket("localhost", server.PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner in = new Scanner(clientSocket.getInputStream());){

            String name = "Victor";
            out.println(name);

            Assertions.assertEquals(name + " joined to the chat.", in.nextLine());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
