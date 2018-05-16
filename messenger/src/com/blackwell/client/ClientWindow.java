package com.blackwell.client;

import com.blackwell.network.TCPConnection;
import com.blackwell.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {
    private static final int WIDTH = 266;
    private static final int HEIGHT = 400;
    private static final String IP_ADDRESS = "192.168.31.142"; // ip address of the server

    public static void main(String[] args) {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

        } catch (UnknownHostException e) {

            e.printStackTrace();

        }
        SwingUtilities.invokeLater(() -> new ClientWindow());
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickName = new JTextField("anonymous");
    private final JTextField fieldInput = new JTextField("Hi, guys!");

    private TCPConnection connection;

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);


        log.setEditable(false);
        log.setLineWrap(true);

        add(log, BorderLayout.CENTER);
        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickName, BorderLayout.NORTH);

        setVisible(true);

        try {
            connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            printMessege("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if ("".equals(msg)) return;

        fieldInput.setText(null);
        connection.sendString(fieldNickName.getText() +": "+ msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessege("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessege(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessege("Connection close.");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        printMessege("Connection exception: " + ex);
    }

    private synchronized void printMessege(String msg){
        SwingUtilities.invokeLater(() -> {
            log.append(msg + EOL);
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

}
