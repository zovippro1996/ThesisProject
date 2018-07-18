package com.example.thesis.booktrading.gnutellaprotocol;

import com.example.thesis.booktrading.gnutellaprotocol.Connection;
import com.example.thesis.booktrading.helper.SocketAsyncTask;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

public class Downloader extends Thread
{
    private int myindex;
    private String myname;
    private String myip;
    private int myport;
    private boolean oktodownload = false;
    private int filesize = 0;

    public Downloader (int index, String name, String ip, int port)
    {
        myindex = index;
        myname = name;
        myip = ip;
        myport = port;
    }

    public void run()
    {
        try
        {System.out.println("Downloader started.");
            SocketAsyncTask socketAsyncTask = new SocketAsyncTask();
            Socket s = socketAsyncTask.execute(myip+":"+ Integer.toString(myport))
                    .get();
//            Socket s = new Socket(myip, myport);
            Connection connection = new Connection(s, Connection.DOWNLOADING);

            Searcher.addFileTransfer(connection.getIPAddress(), myname);

            String greetstring = ("GET /get/" + myindex + "/" + myname + " HTTP/1.0\r\nConnection: Keep-Alive\r\nRange: bytes=0-\r\n\r\n");
            byte[] greeting = greetstring.getBytes();

            connection.getByteWriter().write(greeting, 0, greeting.length);
            connection.getByteWriter().flush();

            String responseline;

            while (!((responseline = connection.getTextReader().readLine()).equals(""))) // Run through the HTTP header
            {
                responseline = connection.getTextReader().readLine();
                if (responseline.startsWith("Content-length: "))
                {
                    filesize = Integer.parseInt(responseline.substring(16)); // Start reading right after the space
                    oktodownload = true;
                    Searcher.updateConnectionStatus(connection.getIPAddress(), myname, "Received handshake...");
                }
            }

            if (oktodownload)
            {
                File towrite = new File((SharedDirectory.getOurSavePath().getPath() + File.separatorChar + myname.replaceAll("\\s+","").replaceAll(":","").toString()));
                if (towrite.createNewFile())
                {
                    InputStream in = connection.getSocket().getInputStream();
                    OutputStream out = new FileOutputStream(towrite);
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    String doc2 = new String(buf, "UTF-8");
                    out.flush();
                    out.close();
                    in.close();
                }
                else
                {
                    Searcher.updateConnectionStatus(connection.getIPAddress(), myname, "Unable to create new file in shared directory.");
                }
            }
            else
            {
                Searcher.updateConnectionStatus(connection.getIPAddress(), myname, "Bad HTTP handshake.");
            }
        }
        catch (IOException e)
        {
            System.out.println("Unable to connect.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}




