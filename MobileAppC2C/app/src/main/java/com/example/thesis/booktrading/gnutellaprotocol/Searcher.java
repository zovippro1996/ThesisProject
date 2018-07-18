package com.example.thesis.booktrading.gnutellaprotocol;

import android.content.Intent;

import com.example.thesis.booktrading.FindTargetGnutellaActivity;
import com.example.thesis.booktrading.FoundResultSetActivity;

import java.util.Iterator;
import java.util.LinkedList;

public class Searcher {
    static FindTargetGnutellaActivity findTargetGnutellaActivity;
    // private static int Speed has to come from the user.
    static LinkedList searches = new LinkedList();

    public Searcher(){

    }

    //added constructor
    public Searcher(FindTargetGnutellaActivity context) {
        findTargetGnutellaActivity = context;
    }

    public static void addSearch(Query search) // Called when the user presses the "Search" button
    {
        searches.add(search);
    }

    public static void clear() // Called when the user presses the "Clear" button
    {
        searches = new LinkedList();
    }

    public static void updateAddedConnection(Connection c) {
        addAConnection(c.getIPAddress().toString(), c.getIPAddress().getPort(), c.getTypeString(), "Connected");
    }

    public static void updateRemovedConnection(IPAddress ip) {
        removeAConnection(ip.toString(), ip.getPort(), "Disconnected");
    }

    public static void updateHostCache(Host h, boolean flag) {
        if (flag) {
            addAHostInCache(h.getName(), h.getPort());
        } else {
            removeAHostInCache(h.getName(), h.getPort());
        }
    }

    //List host will save in SQLite
    public static void addAConnection(String host, int port, String type, String status) {
        Object[] newRow = new Object[4];
        newRow[0] = host;
        newRow[1] = new Integer(port);
        newRow[2] = type;
        newRow[3] = status;
//        liveModel.insertRow(0, newRow);
    }



    //List host will save in SQLite
    public static void removeAConnection(String host, int port, String status) {
//        for (int i = 0; i < liveModel.getRowCount(); i++) {
//            String ip = (String) (liveModel.getValueAt(i, 0));
//            if (ip.equals(host)) {
//                liveModel.removeRow(i);
//            }
//        }
    }

    //List host will save in SQLite
    public static void addAHostInCache(String host, int port) {
        Object[] newRow = new Object[2];
        newRow[0] = host;
        newRow[1] = new Integer(port);
//        cacheModel.insertRow(0, newRow);
    }

    //List host will save in SQLite - Trung
    public static void removeAHostInCache(String host, int port) {
//        for (int i = 0; i < cacheModel.getRowCount(); i++) {
//            String h = (String) (cacheModel.getValueAt(i, 0));
//            if (host.equals(h)) {
//                cacheModel.removeRow(i);
//            }
//        }
    }

    public static void updateInfo(int hosts, int totalkb, int totalfiles) {
//        myconnectionpanel.updateStats(hosts, totalfiles, totalkb);
    }

    public static void inform(Query q) {
//        mymonitorpanel.addQuery(q.getIP().toString(), q.getIP().getPort(), q.getSearchString());
    }

    public static void inform(IPAddress ip, QueryHit qh) {
        Integer port = new Integer(qh.getPort());
        String myip = qh.getIP().toString();

        Iterator iter = searches.iterator();
        while (iter.hasNext()) {
            Query b = (Query) iter.next();
            if (qh.compare(b)) {
                ResultSet r = qh.getResults();
//                while (r.more()) {
//                    Integer index = new Integer(r.getIndex());
//                    Integer size = new Integer(r.getFilesize());
//                    String name = r.getName();
////                    mypanel.addQHit(index, name, size, myip, port);
//                }
                Intent intent = new Intent(findTargetGnutellaActivity, FoundResultSetActivity.class);
                intent.putExtra("ip",myip);
                intent.putExtra("port",port.toString());
                intent.putExtra("ResultSet",r);
                findTargetGnutellaActivity.startActivity(intent);

            }
        }
    }

    public static void addFileTransfer(IPAddress ip, String name) {
//        for (int i = 0; i < downloadtable.getRowCount(); i++) /* If we've already tried downloading the same thing before, update it properly on the table rather
//								 than adding a new one. */ {
//            if (((IPAddress) downloadtable.getValueAt(i, 0)).equals(ip) && ((String) downloadtable.getValueAt(i, 1)).equals(name)) {
//                updateConnectionStatus(ip, name, "Connecting...");
//            }
//        }
//        Object[] newRow = new Object[4];
//        newRow[0] = ip;
//        newRow[1] = name;
//        newRow[2] = "Connecting...";
//        newRow[3] = "0% Complete";
//        downloadModel.addRow(newRow);
    }

    public static void updateConnectionStatus(IPAddress ip, String name, String status) {
//        for (int i = 0; i < downloadtable.getRowCount(); i++) {
//            if (((IPAddress) downloadtable.getValueAt(i, 0)).equals(ip) && ((String) downloadtable.getValueAt(i, 1)).equals(name)) {
//                downloadModel.setValueAt(status, i, 2);
//                break;
//            }
//        }
    }

    public static void updateFileTransferStatus(IPAddress ip, String name, int percent) {
//        for (int i = 0; i < downloadtable.getRowCount(); i++) {
//            if (((IPAddress) downloadtable.getValueAt(i, 0)).equals(ip) && ((String) downloadtable.getValueAt(i, 1)).equals(name)) {
//                downloadModel.setValueAt((String) (percent + "% Complete"), i, 3);
//                break;
//            }
//        }
    }
}