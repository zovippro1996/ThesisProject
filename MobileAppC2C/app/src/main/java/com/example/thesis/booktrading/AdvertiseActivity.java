package com.example.thesis.booktrading;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thesis.booktrading.gnutellaprotocol.GnutellaCoordinator;
import com.example.thesis.booktrading.gnutellaprotocol.Listener;
import com.example.thesis.booktrading.gnutellaprotocol.PeriodicConnector;
import com.example.thesis.booktrading.gnutellaprotocol.PingHandler;
import com.example.thesis.booktrading.gnutellaprotocol.Pinger;
import com.example.thesis.booktrading.gnutellaprotocol.Preferences;
import com.example.thesis.booktrading.gnutellaprotocol.QHandler;
import com.example.thesis.booktrading.gnutellaprotocol.Searcher;
import com.example.thesis.booktrading.gnutellaprotocol.SharedDirectory;
import com.example.thesis.booktrading.helper.AsyncTaskIpify;
import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

public class AdvertiseActivity extends AppCompatActivity {

    TextView textView_waitingStatus = null;
    Button button_Transact = null;
    int thisDeviceIP = 0;
    private static final String TAG = "BookingDebugAPI";

    private DatabaseHelper databaseHelper;
    GnutellaCoordinator gnutellaCoordinator = null;

    /**
     * Our handler to Nearby Connections.
     */
    private ConnectionsClient mConnectionsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        databaseHelper = new DatabaseHelper(this);
        mConnectionsClient = Nearby.getConnectionsClient(this);

        textView_waitingStatus = findViewById(R.id.textView_waitingStatus);
        button_Transact = findViewById(R.id.button_Transact);


        //Shared Directory
        File optDir = this.getDir("opt", Context.MODE_PRIVATE);
        //Download Directory
        File ccrickDir = this.getDir("ccrick", Context.MODE_PRIVATE);
        deleteRecursive(optDir);
        File optDir_recreated = this.getDir("opt", Context.MODE_PRIVATE);

        //Create text file for each Books in Inventory
        Cursor bookInventoryCursor = databaseHelper.getAllBooksInventory();
        String PhoneNumber = databaseHelper.getPersonalInfoPhone();
        String OwnerName = databaseHelper.getPersonalInfoName();
        try {
            while (bookInventoryCursor.moveToNext()) {
                String filename_book = bookInventoryCursor.getString(bookInventoryCursor
                        .getColumnIndex
                                (DatabaseHelper.COL_BOOK_NAME));
                String testAbsoluePath = optDir.getAbsolutePath();
                File storeFile = new File(optDir.getAbsolutePath(), filename_book+".xml");

                //-----------------Name XML Tag---------------
                String infodata = "infodata";
                String ipaddress = "ipaddress";
                String phone = "phone";
                String name = "name";
                //---------------------------------------------

                try {
                    FileOutputStream fileinput = new FileOutputStream(storeFile);
                    PrintStream printstream = new PrintStream(fileinput);
                    XmlSerializer xmlSerializer = Xml.newSerializer();
                    StringWriter writer = new StringWriter();
                    xmlSerializer.setOutput(writer);
                    xmlSerializer.startDocument("UTF-8", true);

                    xmlSerializer.startTag(null, infodata);
                    xmlSerializer.startTag(null, ipaddress);
                    xmlSerializer.text(gnutellaCoordinator.getMineIPPort());
                    xmlSerializer.endTag(null, ipaddress);

                    xmlSerializer.startTag(null, phone);
                    xmlSerializer.text(PhoneNumber);
                    xmlSerializer.endTag(null, phone);

                    xmlSerializer.startTag(null, name);
                    xmlSerializer.text(OwnerName);
                    xmlSerializer.endTag(null, name);
                    xmlSerializer.endTag(null, infodata);

                    xmlSerializer.endDocument();
                    xmlSerializer.flush();
                    String dataWrite = writer.toString();

                    printstream.print(dataWrite);
                    fileinput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            bookInventoryCursor.close();
        }

        String preferencesDir = this.getFilesDir().getAbsolutePath() + "/preferences.txt";
        gnutellaCoordinator = new GnutellaCoordinator(preferencesDir);

        startAdvertising();
        startDiscovery();
    }

    @Override
    protected void onStop() {
        gnutellaCoordinator.updatePreferenceFile(this.getFilesDir().getAbsolutePath() + "/preferences.txt");
        mConnectionsClient.stopAllEndpoints();
        super.onStop();
    }


    public void onClick_targetProfile(View view) {
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Get IP For mobile Local or Public IP
     */
    public String getMobileIP() {
        String ipv4 = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof
                            Inet4Address)) {
                        String ipaddress = inetAddress.getHostAddress();
                        return ipaddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IPException", "Exception in Get IP Address: " + ex.toString());
        }
        return null;
    }

    /**
     * Get Public Mobile IP from ipify API
     */
    public static String getPublicMobileIP() {
        //Some url endpoint that you may have
        String myUrl = "https://api.ipify.org";
        //String to place our result in
        String result = "";
        //Instantiate new instance of our class
        AsyncTaskIpify getRequest = new AsyncTaskIpify();
        //Perform the doInBackground method, passing in our url

        try {
            result = getRequest.execute(myUrl).get();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }

    /*-------------------------- Advertise SECTION ------------------------------------------*/
    private void startAdvertising() {
        Nearby.getConnectionsClient(this).startAdvertising(
                getMobileIP(),
                "com.example.thesis.booktrading",
                mConnectionLifecycleCallback,
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        ).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unusedResult) {
                        // We're advertising!
                        popupToast("We're advertising as " + getMobileIP());
                    }
                }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // We were unable to start advertising.
                        popupToast("Failed to Advertising");
                    }
                });
    }

    // Callbacks for finding other devices
    private EndpointDiscoveryCallback mEndpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i(TAG, "onEndpointFound: endpoint found, connecting " +  info.getEndpointName());
                    mConnectionsClient.requestConnection(getMobileIP(), endpointId,
                            connectionLifecycleCallback);

                    String testIPString = info.getEndpointName() + ":8089";
                    gnutellaCoordinator.connectNewHost(info.getEndpointName() + ":8089");

                }

                @Override
                public void onEndpointLost(String endpointId) {
                    Log.i(TAG, "A previously discovered endpoint has gone away. " +  endpointId);
                }
            };

    private void startDiscovery() {
        Nearby.getConnectionsClient(this).startDiscovery(
                "com.example.thesis.booktrading",
                mEndpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        ).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unusedResult) {
                        // We're discovering!
                        popupToast("We're discovering as " + getMobileIP());
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // We were unable to start discovering.
                        popupToast("Failed to Discovering as " + e.toString());
                    }
                }
        );
    }

    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {

                @Override
                public void onConnectionInitiated(
                        String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    Nearby.getConnectionsClient(getApplicationContext()).acceptConnection
                            (endpointId,
                                    mPayloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            // The connection broke before it was able to be accepted.
                            break;
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.
                    popupToast("We've been disconnected from endpoint.");
                }
            };

    private void sendPayload(String endpointId, Payload payload) {
        if (payload.getType() == Payload.Type.BYTES) {
            // No need to track progress for bytes.
            return;
        }
    }

    private PayloadCallback mPayloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
//                    popupToast(String.format("onPayloadReceived(endpointId=%s, payload=%s)",
//                            endpointId, payload));
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate
                        update) {
//                    popupToast(String.format(
//                            "onPayloadTransferUpdate(endpointId=%s, update=%s)", endpointId,
//                            update));
                }
            };

    // Callbacks for connections to other devices
    private ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo
                        connectionInfo) {
                    popupToast("onConnectionInitiated: accepting connection");
                    mConnectionsClient.acceptConnection(endpointId, mPayloadCallback);
                    String peerName = connectionInfo.getEndpointName();
                    popupToast("onConnectionInitiated: accepting connection " + peerName);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        popupToast("onConnectionResult: connection successful");
//                        connectionsClient.stopDiscovery();
//                        connectionsClient.stopAdvertising();

                        String opponentEndpointId = endpointId;
                        popupToast("onConnectionResult: connection successful " +
                                opponentEndpointId);

                    } else {
                        popupToast("onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    popupToast("onDisconnected: disconnected from the opponent");
                }
            };

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
