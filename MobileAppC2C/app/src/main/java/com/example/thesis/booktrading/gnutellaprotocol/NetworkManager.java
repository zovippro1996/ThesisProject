package com.example.thesis.booktrading.gnutellaprotocol;/*
  Network Manager - started by main
*/
import java.io.*;

public class NetworkManager
{
    public static void writeToOne(IPAddress ip, Packet packet)
    {
        if (HostArray.isLive(ip))
        {
            Connection connection = HostArray.getConnection(ip);
            try
            {
                connection.getByteWriter().write (packet.contents(), 0, packet.totalLength());
                connection.getByteWriter().flush();
            }
            catch (IOException e)
            {
                try
                {
                    connection.getSocket().close();
                    HostArray.removeConnection(ip);
                }
                catch (IOException exception)
                {
                    System.err.println(exception);
                }
            }
        }
    }



    public static void writeToAll(Packet packet)
    {
        int testHostCount = HostArray.getCount();
        for (int i = 0; i < HostArray.getCount(); i++)
        {
            Connection c = HostArray.getConnection(i);
            try
            {
                c.getByteWriter().write (packet.contents(), 0, packet.totalLength());
                c.getByteWriter().flush();
            }
            catch (IOException e)
            {
                try
                {
                    c.getSocket().close();
                    HostArray.removeConnection(c);
                }
                catch (IOException exception)
                {
                    System.err.println(exception);
                }
            }
        }
    }


    public static void writeButOne(IPAddress ip, Packet packet)
    {
        if (packet.identify() == -128){
            System.out.println("Query");
        }
        int testHostArray = HostArray.getCount();
        for (int i = 0; i < HostArray.getCount(); i++)
        {
            Connection c = HostArray.getConnection(i);
            if (!(c.compareConnections(ip)))
            {
                try
                {
                    c.getByteWriter().write (packet.contents(), 0, packet.totalLength());
                    c.getByteWriter().flush();
                }
                catch (IOException e)
                {
                    try
                    {
                        c.getSocket().close();
                        HostArray.removeConnection(c);
                    }
                    catch (IOException exception)
                    {
                        System.err.println(exception);
                    }
                }
            }
        }
    }

    public static void notify(IPAddress ip) // Remove socket from open connection list, based on its IP.
    {
        if (HostArray.isLive(ip))
        {
            System.out.println("Killing " + ip);
            Connection c = HostArray.getConnection(ip);
            try
            {
                c.getSocket().close();
                HostArray.removeConnection(c);
            }
            catch (IOException e)
            {
                System.err.println(e);
            }
        }
    }
}



