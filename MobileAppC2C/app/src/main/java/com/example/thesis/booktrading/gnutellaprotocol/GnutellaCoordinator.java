package com.example.thesis.booktrading.gnutellaprotocol;

import com.example.thesis.booktrading.FindTargetGnutellaActivity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class GnutellaCoordinator {

//    static FindTargetGnutellaActivity findTargetGnutellaActivity;
    // private static int Speed has to come from the user.
    static LinkedList searches = new LinkedList();

    public GnutellaCoordinator(String preferencesPath){
//        findTargetGnutellaActivity = context;
        System.out.println("Setting up hash tables...");
        QHandler.initQueryTable(); // Checked
        PingHandler.initPingTable(); //Checked
        System.out.println("Determining network address..."); //Checked
        Mine.updateAddress(); //Checked
        System.out.println("Reading preferences file..."); //Checked

        new Searcher(); //Checked
        Preferences.readFromFile(preferencesPath);
        System.out.println("Setting up file table..."); //Checked

        new SharedDirectory(Preferences.SHAREPATH, Preferences.SAVEPATH);
        Listener listener = new Listener();
        listener.start(); // Beginning listening for network connections
        PeriodicConnector periodicconnector = new PeriodicConnector(Preferences.AUTO_CONNECT); // Begin actively trying to connect
        periodicconnector.start();
        Pinger pinger = new Pinger();
        pinger.start(); // Start sending out periodic pings.
    }

    public void addSearchGnutella(String keyword){
        Query a = new Query(0, keyword);
        NetworkManager.writeToAll(a);
        Searcher.addSearch(a);
    }

    public static String getMineIPPort(){
        return Mine.ipString + ":" + Integer.toString(Mine.getPort());
    }

    public void connectNewHost(String ip) {
        StringTokenizer st = new StringTokenizer(ip, ":");
        if (st.countTokens() == 2) {
            ip = st.nextToken();
            int port = Integer.parseInt(st.nextToken());
            Connector connector = new Connector(ip, port);
            connector.start();
        }
    }

    public int getNumberHostArray(){
        return HostArray.getCount();
    }

    public void updatePreferenceFile(String preferencesPath){
        Preferences.writeToFile(preferencesPath);
    }

    public void downloadInfo(int index, String name, String ip, int port){
        Downloader downloader = new Downloader(index, name, ip, port);
        downloader.start();
    }
}
