package swiftproduct.swift;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Handler;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.logging.Handler;
import android.util.Log;
import android.os.Environment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;







public class About extends AppCompatActivity {

    TextView sean,todd,josh,charles;
    String seanBio = "\n" +
            "Default"

    String charlesBio = "\n" +
            "Default"

    String joshBio = "\n" +
            "Default"

    String toddBio  = "\n" +
            "Default"

            
    private static final String TAG = "MyFTPClientFunctions";
    String file_name = "hello_file.txt";
    private Context cntx = null;
    private MyFTPClientFunctions ftpclient = null;
    Button returnButton;
    ImageButton testUpload;
    //TextView bio;
    //String Bio =


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        cntx = this.getBaseContext();
        sean = (TextView) findViewById(R.id.Sean_bio);sean.setText(seanBio);
        todd = (TextView) findViewById(R.id.Todd_bio);todd.setText(toddBio);
        josh = (TextView) findViewById(R.id.Josh_bio);josh.setText(joshBio);
        charles = (TextView) findViewById(R.id.Charles_bio);charles.setText(charlesBio);

        returnButton = (Button)findViewById(R.id.ReturntoMain_About);
        testUpload = (ImageButton)findViewById(R.id.Todd_pic);
        onClickReturnButton();
        onClickTestUploadButton();
        //bio = (TextView)findViewById(R.id.Bio);
        //bio.setText(Bio);
        ftpclient = new MyFTPClientFunctions();

        //Create dummy test file
        createDummyFile();
        test();
        connect();
    }

    public void connect() {

        if (isOnline(About.this)) {
            Toast.makeText(About.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();
            connectToFTPAddress();
        } else {
            Toast.makeText(About.this,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG).show();
            return;
        }//Log.d(TAG, Environment.getExternalStorageDirectory()
        //+ "/AlphaOmega/" + file_name);
    }

    public void onClickTestUploadButton() {
        testUpload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            new Thread(new Runnable() {
                                public void run() {
                                    boolean status = false;
                                    status = ftpclient.ftpUpload(Environment.getExternalStorageDirectory()
                                                    + "/AlphaOmega/" + file_name,
                                            file_name, "/", cntx);
                                    if (status) {
                                        Log.d(TAG, "Upload success");
                                    } else {
                                        Log.d(TAG, "Upload failed");
                                    }
                                }
                            }).start();

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }


                }
        );
    }

    public void onClickReturnButton() {

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalApplication mApp = ((GlobalApplication) getApplication());
                        if(mApp.connected){
                            new Thread(new Runnable() {
                                public void run() {
                                    ftpclient.ftpDisconnect();
                                }
                            }).start();
                        }
                        mApp.connected = false;
                        Intent intent = new Intent("swiftproduct.swift.Main_Menu");
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }


    private void test(){

        final String host = ((GlobalApplication) this.getApplication()).getIP_Address().trim();
        final String username = ((GlobalApplication) this.getApplication()).getUserID().trim();
        final String password = ((GlobalApplication) this.getApplication()).getPassword().trim();

        Toast.makeText(About.this, username + "@" + host +" " + password, Toast.LENGTH_LONG).show();
    }

    private void connectToFTPAddress() {

        final String host = ((GlobalApplication) this.getApplication()).getIP_Address().trim();
        final String username = ((GlobalApplication) this.getApplication()).getUserID().trim();
        final String password = ((GlobalApplication) this.getApplication()).getPassword().trim();


        new Thread(new Runnable() {
            public void run() {
                boolean status = false;
                status = ftpclient.ftpConnect(host, username, password, 21);
                if (status) {
                    Log.d(TAG, "Connection Success");
                    GlobalApplication mApp = ((GlobalApplication) getApplication());
                    mApp.connected = true;
                } else {
                    Log.d(TAG, "Connection failed");
                }
            }
        }).start();

    }


    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void createDummyFile() {

        try {
            File root = new File(Environment.getExternalStorageDirectory(),
                    "AlphaOmega");
            if (!root.exists()) {
                root.mkdirs();
            }
            File AlphaOmegafile = new File(root, file_name);
            FileWriter writer = new FileWriter(AlphaOmegafile);
            writer.append("Hi this is a sample file to for testing upload functionality of AlphaOmega App!");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        GlobalApplication mApp = ((GlobalApplication) getApplication());
        if(mApp.connected){
            new Thread(new Runnable() {
                public void run() {
                    ftpclient.ftpDisconnect();
                }
            }).start();
        }
        mApp.connected = false;

    }
}
