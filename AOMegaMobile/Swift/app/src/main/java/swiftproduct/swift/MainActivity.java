package swiftproduct.swift;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Button enterButton;
    EditText usrID, IP, Pin;
    private MyFTPClientFunctions ftpclient = null;
    private static final String TAG = "MyFTP Main Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ftpclient = new MyFTPClientFunctions();
        //connect();
        enterButton = (Button)findViewById(R.id.enter_button);
        usrID = (EditText) findViewById(R.id.UserID);
        IP = (EditText) findViewById(R.id.IP);
        Pin = (EditText) findViewById(R.id.Pin);
        //getImageCounts();
        addData();
    }
    public void addData(){


        enterButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GlobalApplication mApp = ((GlobalApplication)getApplication());
                        mApp.setUserID(usrID.getText().toString());
                        mApp.setIP_Address(IP.getText().toString());
                        mApp.setPassword(Pin.getText().toString());

                        //mApp.setUserID("pi");
                        //mApp.setIP_Address("192.168.1.117");
                        //mApp.setPassword("helloworld");

                        //Toast.makeText(MainActivity.this, mApp.getUserID()+ " " + mApp.getIP_Address() +" " + mApp.getPassword(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, mApp.getUserID()+ " " + mApp.getIP_Address() +" " + mApp.getPassword());
                        /*new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                        + "/AlphaOmega/" + file_name);
                                if (status) {
                                    Log.d(TAG, "Open App Download success");
                                } else {
                                    Log.d(TAG, "Open App Download failed");
                                }
                            }
                        }).start();*/
                        Intent intent = new Intent ("swiftproduct.swift.Main_Menu");
                        startActivity(intent);
                        finish();
                    }

                }
        );
    }

    public void connect() {

        if (isOnline(MainActivity.this)) {
            /*Toast.makeText(MainActivity.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();*/
            Log.d(TAG, "Online");

            connectToFTPAddress();
        } else {
            /*Toast.makeText(MainActivity.this,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG).show();*/
            Log.d(TAG, "Offline");
            return;
        }
    }

    private void connectToFTPAddress() {

        final String host = ((GlobalApplication) this.getApplication()).getIP_Address().trim();
        final String username = ((GlobalApplication) this.getApplication()).getUserID().trim();
        final String password = ((GlobalApplication) this.getApplication()).getPassword().trim();


        new Thread(new Runnable() {
            public void run() {
                boolean status = false;
                status = ftpclient.ftpConnect(host, username, password, 21);
                if (status == true) {
                    Log.d(TAG, "Connection Success");
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

  /*  private void getImageCounts() {

                File file = new File(Environment.getExternalStorageDirectory()
                        + "/AlphaOmega/" + file_name_counts);
                if (file.exists()) {
                    try {


                        FileInputStream stream = new FileInputStream(file);
                        String jsonStr = null;
                        try {
                            FileChannel fc = stream.getChannel();
                            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                            jsonStr = Charset.defaultCharset().decode(bb).toString();

                            JSONObject jsonObj = new JSONObject(jsonStr);

                            // Getting data from JSON
                            Integer thermal_count = jsonObj.getInt("thermal_count");
                            Integer cam_count = jsonObj.getInt("cam_count");

                            GlobalApplication mApp = ((GlobalApplication) getApplication());

                            mApp.setThermal_image_count(thermal_count);
                            mApp.setCam_image_count(cam_count);
                            Log.d(TAG, "Thermal Images: " + mApp.getThermal_image_count().toString() + " Cam Images: " + mApp.getCam_image_count().toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            stream.close();
                        }
                    } catch (FileNotFoundException e1) {

                        Log.d(TAG, "File not Found" + e1);
                    } catch (IOException e2) {

                        Log.d(TAG, "IOException" + e2);
                    }
                }

            }
*/

}
