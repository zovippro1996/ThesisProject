package com.example.thesis.booktrading;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thesis.booktrading.gnutellaprotocol.GnutellaCoordinator;
import com.example.thesis.booktrading.gnutellaprotocol.ResultSet;
import com.example.thesis.booktrading.gnutellaprotocol.SharedDirectory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FoundResultSetActivity extends AppCompatActivity {

    GnutellaCoordinator gnutellaCoordinator = null;

    TextView textView_index;
    TextView textView_filename;
    TextView textView_ip;
    TextView textView_Size;

    String ip = "";
    String port = "";

    int index = 0;
    int portInt = 0;

    private static final String ns = null;

    String targetPhone = "";
    String targetIP = "";
    String targetname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_result_set);

        textView_index = findViewById(R.id.textView_index);
        textView_filename = findViewById(R.id.textView_filename);
        textView_ip = findViewById(R.id.textView_ip);
        textView_Size = findViewById(R.id.textView_Size);

        ResultSet rs = (ResultSet) getIntent().getParcelableExtra("ResultSet");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");
        while (rs.more()) {
            Integer index = new Integer(rs.getIndex());
            Integer size = new Integer(rs.getFilesize());
            String name = rs.getName();
            textView_index.setText(Integer.toString(index));
            textView_filename.setText(name);
            if(!ip.equals("0.0.0.0")){
                textView_ip.setText(ip+":"+port);
            }
            textView_Size.setText(Integer.toString(size));
        }
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onClick_getInfo(View view) {
        index = Integer.parseInt(textView_index.getText().toString());
        String name = textView_filename.getText().toString();
        portInt = Integer.parseInt(port);

        //Shared Directory
        File optDir = this.getDir("opt", Context.MODE_PRIVATE);
        //Download Directory
        File ccrickDir = this.getDir("ccrick", Context.MODE_PRIVATE);
        deleteRecursive(ccrickDir);
        File ccrickDir_recreated = this.getDir("ccrick", Context.MODE_PRIVATE);

        String preferencesDir = this.getFilesDir().getAbsolutePath() + "/preferences.txt";
        gnutellaCoordinator = new GnutellaCoordinator(preferencesDir);
        System.out.println("BEFORE dISOJD!@# CLICK"); //Checked
        gnutellaCoordinator.downloadInfo(index, name, ip, portInt);

        File toread = new File((SharedDirectory.getOurSavePath().getPath() + File.separatorChar +
                name.replaceAll("\\s+","").replaceAll(":","").toString()));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            System.out.println("documentBuilder A11"); //Checked
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            System.out.println("documentBuilder A12"); //Checked
            document = documentBuilder.parse(toread);

            targetIP = document.getElementsByTagName("ipaddress").item(0).getTextContent();
            targetPhone = document.getElementsByTagName("phone").item(0).getTextContent();
            targetname = document.getElementsByTagName("name").item(0).getTextContent();

            System.out.println(targetIP + " " + targetname); //Checked

            Intent intent = new Intent(this, TargetProfileActivity.class);
            intent.putExtra("ipaddress",targetIP);
            intent.putExtra("phone",targetPhone);
            intent.putExtra("name",targetname);
            intent.putExtra("bookname", name.replace(".xml",""));
            System.out.println("Start Intent"); //Checked
            startActivity(intent);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
