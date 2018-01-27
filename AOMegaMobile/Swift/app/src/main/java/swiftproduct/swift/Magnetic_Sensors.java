package swiftproduct.swift;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Magnetic_Sensors extends AppCompatActivity {

    private static final String TAG = "MyFTP MagSens";
    Button returnButton, refreshButton;
    String file_name = "status.json";
    private MyFTPClientFunctions ftpclient = null;
    TextView DoorSensor;
    private Handler mHandle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetic_sensor);

        ftpclient = new MyFTPClientFunctions();
        refreshButton = (Button)findViewById(R.id.Refresh_Button_Doors);
        returnButton = (Button)findViewById(R.id.ReturntoMain_Doors);
        DoorSensor = (TextView)findViewById(R.id.Status1);
        mHandle = new Handler();
        startUpDoors();
        connect();
        //refresh();
        onClickReturnButton();
        onClickRefreshButton();
        //refreshSensors();

    }

    //This function will read the current state of the doors before activity really begins
    private void startUpDoors() {

        try {

            File file = new File(Environment.getExternalStorageDirectory()
                    + "/AlphaOmega/" + file_name);
            if (file.exists()) {
                try {
                    FileInputStream stream = new FileInputStream(file);
                    String jsonStr = null;
                    try {

                        FileChannel fc = stream.getChannel();
                        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                        jsonStr = Charset.defaultCharset().decode(bb).toString();

                        JSONObject jsonObj = new JSONObject(jsonStr);
                        GlobalApplication mApp = ((GlobalApplication) getApplication());

                        // Getting data from JSON
                        Boolean data = jsonObj.getBoolean("door");

                        if (data) {
                            mApp.setDoor(true);
                            Log.d(TAG, "Door Locked");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DoorSensor.setText("Closed");
                                    DoorSensor.setTextColor(Color.BLACK);
                                }
                            });

                        } else {
                            mApp.setDoor(false);
                            Log.d(TAG, "Door Unlocked");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DoorSensor.setText("Open");
                                    DoorSensor.setTextColor(Color.RED);
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        stream.close();
                    }
                } catch (FileNotFoundException e1) {

                    Log.d(TAG, "File not Found " + e1);
                } catch (IOException e2) {

                    Log.d(TAG, "IOException " + e2);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "" + e);
        }
    }
    //This will refresh the Global Variable for Doors and UI Text every 10 sec
    private void refreshSensors() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/AlphaOmega/" + file_name);
                    if (file.exists()) {
                        try {
                            FileInputStream stream = new FileInputStream(file);
                            String jsonStr = null;
                            try {

                                FileChannel fc = stream.getChannel();
                                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                                jsonStr = Charset.defaultCharset().decode(bb).toString();

                                JSONObject jsonObj = new JSONObject(jsonStr);
                                GlobalApplication mApp = ((GlobalApplication) getApplication());

                                // Getting data from JSON
                                Boolean data = jsonObj.getBoolean("door");

                                if (data) {
                                    mApp.setDoor(true);
                                    Log.d(TAG, "Door Locked");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DoorSensor.setText("Closed");
                                            DoorSensor.setTextColor(Color.BLACK);
                                        }
                                    });

                                } else {
                                    mApp.setDoor(false);
                                    Log.d(TAG, "Door Unlocked");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DoorSensor.setText("Open");
                                            DoorSensor.setTextColor(Color.RED);
                                        }
                                    });

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                stream.close();
                            }
                        } catch (FileNotFoundException e1) {

                            Log.d(TAG, "File not Found " + e1);
                        } catch (IOException e2) {

                            Log.d(TAG, "IOException " + e2);
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Timer problem " + e);
                }
            }


        }, 10000, 10000);

    }

    //This one is untested, but it is automatic refreshing without the refresh button
    private void refresh() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    new Thread(new Runnable() {
                        public void run() {
                            boolean status = false;
                            status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                    + "/AlphaOmega/" + file_name);
                            if (status) {
                                Log.d(TAG, "Automatic Refresh Download success");
                            } else {
                                Log.d(TAG, "Automatic Refresh Download failed");
                            }
                        }
                    }).start();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }, 15000);
    }


    public void onClickReturnButton(){

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            public void run() {
                                ftpclient.ftpDisconnect();
                            }
                        }).start();
                        Intent intent = new Intent("swiftproduct.swift.Main_Menu");
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }


    public void onClickRefreshButton() {

        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GlobalApplication mApp = ((GlobalApplication) getApplication());
                        if(mApp.connected) {
                            try {

                                new Thread(new Runnable() {
                                    public void run() {
                                        boolean status = false;
                                        status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                                + "/AlphaOmega/" + file_name);
                                        if (status) {
                                            Log.d(TAG, "Download success");
                                        } else {
                                            Log.d(TAG, "Download failed");
                                        }
                                    }
                                }).start();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            //Update UI
                            mHandle.postDelayed(new Runnable() {
                                public void run() {
                                    try {

                                        File file = new File(Environment.getExternalStorageDirectory()
                                                + "/AlphaOmega/" + file_name);
                                        if (file.exists()) {
                                            try {
                                                FileInputStream stream = new FileInputStream(file);
                                                String jsonStr = null;
                                                try {

                                                    FileChannel fc = stream.getChannel();
                                                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                                                    jsonStr = Charset.defaultCharset().decode(bb).toString();

                                                    JSONObject jsonObj = new JSONObject(jsonStr);
                                                    GlobalApplication mApp = ((GlobalApplication) getApplication());

                                                    // Getting data from JSON
                                                    Boolean data = jsonObj.getBoolean("door");

                                                    if (data) {
                                                        mApp.setDoor(true);
                                                        Log.d(TAG, "Door Locked");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                DoorSensor.setText("Closed");
                                                                DoorSensor.setTextColor(Color.BLACK);
                                                            }
                                                        });

                                                    } else {
                                                        mApp.setDoor(false);
                                                        Log.d(TAG, "Door Unlocked");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                DoorSensor.setText("Open");
                                                                DoorSensor.setTextColor(Color.RED);
                                                            }
                                                        });

                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    stream.close();
                                                }
                                            } catch (FileNotFoundException e1) {

                                                Log.d(TAG, "File not Found " + e1);
                                            } catch (IOException e2) {

                                                Log.d(TAG, "IOException " + e2);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.d(TAG, "File grab " + e);
                                    }
                                }
                            }, 2000);
                        }else{

                            Toast.makeText(Magnetic_Sensors.this,
                                    "Problems Connecting to Hub. Please Restart and try Again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
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
    public void connect() {

        if (isOnline(Magnetic_Sensors.this)) {
            /*Toast.makeText(Magnetic_Sensors.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();*/
            connectToFTPAddress();
        } else {
            /*Toast.makeText(Magnetic_Sensors.this,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG).show();*/
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


            @Override
            protected void onStop() {
                super.onStop();
                GlobalApplication mApp = ((GlobalApplication) getApplication());
                if (mApp.connected) {
                    new Thread(new Runnable() {
                        public void run() {
                            ftpclient.ftpDisconnect();
                        }
                    }).start();
                }
                mApp.connected = false;

            }

}