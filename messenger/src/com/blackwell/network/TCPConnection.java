package com.blackwell.network;

import java.io.*;
import java.net.Socket;

public class TCPConnection {
    private static final String CHARSET = "UTF-8";

    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddress, int port) throws IOException {
        this(eventListener, new Socket(ipAddress, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader( socket.getInputStream(),  CHARSET));
        out = new BufferedWriter(new OutputStreamWriter( socket.getOutputStream(), CHARSET));

        rxThread = new Thread(() -> {
            try {
                eventListener.onConnectionReady(TCPConnection.this);
                while (!Thread.currentThread().isInterrupted()){
                    eventListener.onReceiveString(TCPConnection.this, in.readLine());
                }
            } catch (IOException e) {
                eventListener.onException(TCPConnection.this, e);
            } finally {
                eventListener.onDisconnect(TCPConnection.this);
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String value){
        try {
            out.write(value + TCPConnectionListener.EOL);
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }


    private void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return String.format("TCPConnection: %s: %s;", socket.getInetAddress(), socket.getPort());
    }
}
