package org.example.common;

import org.example.client.Client;
import org.example.server.ChatClient;
import org.example.server.ConnectThread;
import org.example.server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

public class IntegrationTest {


    @Test
    void oneClientConnect() {
        Server server = new Server(true);
        while (!server.online) {//wait server start
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String name = "Victor";
        System.setIn(new ByteArrayInputStream(name.getBytes()));

        Client client = new Client(true);
        Client.testMessages.add("Hi");

        try {
            Thread.sleep(1000);//wait until message was received
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        Assertions.assertEquals(ConnectThread.clients.stream().findFirst().get().getName(), name);


    }


    @Test
    void twoClient() {
        Server server = new Server(true);
        while (!server.online) {//wait server start
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String name1 = "Victor";
        System.setIn(new ByteArrayInputStream(name1.getBytes()));

        Client client = new Client(true);

        try {
            Thread.sleep(1000);//wait until message was received
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String name2 = "Petya";
        System.setIn(new ByteArrayInputStream(name2.getBytes()));

        Client client2= new Client(true);
        try {
            Thread.sleep(1000);//wait until message was received
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Iterator iterator = ConnectThread.clients.iterator();

        Assertions.assertEquals(name1, ((ChatClient) iterator.next()).name);
        Assertions.assertEquals(name2, ((ChatClient) iterator.next()).name);
    }
}
