package com.blackwell.server;

import com.blackwell.network.TCPConnection;
import com.blackwell.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) { new ChatServer(); }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
        System.out.println("Server running...");
        try( ServerSocket serverSocket = new ServerSocket(PORT) ){

            while (true){
                 try{
                    new TCPConnection(this, serverSocket.accept());
                 }catch (IOException e) {
                     System.out.printf("TCPConnection exception: %s;", e);
                 }
            }

        }catch (IOException e){
            System.out.println("Server stopped");
            throw new RuntimeException(e);
        }

    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        //sendToAllClient("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllClient(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllClient("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception ex) {
        System.out.printf("TCPConnection exception: %s;%n", ex);
    }

    private void sendToAllClient(String value){
        System.out.printf("sendToAllClient: %s;%n", value);

        for(TCPConnection tcp : connections)
            tcp.sendString(value);
    }
}
