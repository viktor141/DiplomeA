package org.example.client;

import org.example.common.ChatConsumer;
import org.example.common.Side;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer extends ChatConsumer {

    private final PrintWriter out;
    private final Scanner in;

    private final Socket socket;

    public ChatServer(Socket socket) throws IOException {
        this.socket = socket;
        name = "server";
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
        sendMessage(Client.userName);
    }

    @Override
    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public Side side() {
        return Client.side;
    }

    public boolean hasMessage(){
        return in.hasNext();
    }

    public String getMessage(){
        return in.nextLine();
    }

    public void closeConnect() throws IOException {
        socket.close();
        out.close();
        in.close();
    }
}
