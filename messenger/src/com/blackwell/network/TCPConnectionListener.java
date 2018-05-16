package com.blackwell.network;

public interface TCPConnectionListener {

    String EOL = System.lineSeparator();
    int PORT = 8189;
    void onConnectionReady(TCPConnection tcpConnection); // Your connection is ready
    void onReceiveString(TCPConnection tcpConnection, String value); // your connection is receiving string
    void onDisconnect(TCPConnection tcpConnection); //disconnect
    void onException(TCPConnection tcpConnection, Exception ex); //something went wrong

}
