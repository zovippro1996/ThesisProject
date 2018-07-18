package com.example.thesis.booktrading.gnutellaprotocol;

import java.util.*;
import java.io.*;

public class DownloadServer extends Thread
{
    Connection myconnection;
    String incoming;

    public DownloadServer(Connection c, String incoming)
    {
        c.changeType(Connection.UPLOADING);
        myconnection = c;
        this.incoming = incoming;
    }

    public void run()
    {
        StringTokenizer st = new StringTokenizer(incoming, "/");
        if ((st.countTokens() == 5) &&
                (st.nextToken().equals("GET ")) &&
                (st.nextToken().equals("get")))
        {
            int index = Integer.parseInt(st.nextToken());
            String filename = st.nextToken();

            if ((index >= 0) && (filename.endsWith(" HTTP")))
            {
                filename = filename.substring(0, (filename.length() - 5));
                if (SharedDirectory.validate(index, filename))
                {
                    File tosend = SharedDirectory.getFile(index);
                    int size = SharedDirectory.getFileSize(index);
                    String responsestring = ("HTTP 200 OK\r\nServer: Marmalade\r\nContent-type: application/octet-stream\r\nContent-length: " +
                            size + "\r\n\r\n");
                    byte[] response = responsestring.getBytes();

                    try
                    {
                        myconnection.getByteWriter().write(response, 0, response.length);
                        myconnection.getByteWriter().flush();
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(tosend));
                        OutputStream out = myconnection.getSocket().getOutputStream();
                        byte[] buf = new byte[8192];
                        int len = 0;
                        while ((len = in.read(buf)) != -1) {
                            out.write(buf, 0, len);
                        }
//                        for (int i = 0; i < size; i++)
//                        {
//                            myconnection.getByteWriter().write((byte)in.read());
//                        }
                        in.close();
                        out.flush();
                        out.close();
                        myconnection.getByteWriter().close();
                    }
                    catch (Exception e)
                    {
                        System.out.println("Unable to upload file.");
                    }

                }
            }
        }
    }
}
