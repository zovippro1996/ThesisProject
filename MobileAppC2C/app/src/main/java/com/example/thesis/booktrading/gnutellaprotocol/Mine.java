package com.example.thesis.booktrading.gnutellaprotocol;/*
  Lame way to get local IP address. Java's localHost method returns
  127.0.0.1; s this opens a socket to Sun' web server.
 */
import com.example.thesis.booktrading.helper.SocketJavaSunAsyncTask;

import java.net.*;

public class Mine  {

    static int port = 8089;
    static String ipString = "127.0.0.1";
    static IPAddress ipObject = new IPAddress(127, 0, 0, 1, 8080);
    static byte[] serventID = new byte[16];

    public static void updateAddress() {
        try {
            SocketJavaSunAsyncTask socketJavaSunAsyncTask = new SocketJavaSunAsyncTask();
            Socket s = socketJavaSunAsyncTask.execute("").get();
            ipString = s.getLocalAddress().getHostAddress();
            System.out.println("Local address: " + ipString);
            byte[] ipbytes = s.getLocalAddress().getAddress();
            ipObject = new IPAddress(ipbytes[0], ipbytes[1], ipbytes[2], ipbytes[3], port);
            for (int i = 0; i < 16; i++) {
                serventID[i] = ipbytes[i % 4];
            }
            s.close();
        } catch (Exception e) {
            // printStackTrace method
            // prints line numbers + call stack
            e.printStackTrace();
            String checkError = e.getMessage();
            // Prints what exception has been thrown
            System.out.println(e);
        }
    }

    public static IPAddress getIPAddress() {
        return ipObject;
    }

    public static byte[] getServentIdentifier() {
        return serventID;
    }

    public static int getPort() {
        return port;
    }

    public static int getSpeed() {
        return (128);
    }

}
