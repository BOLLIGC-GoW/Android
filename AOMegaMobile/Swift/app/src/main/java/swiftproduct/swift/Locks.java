package swiftproduct.swift;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

public class Locks extends AppCompatActivity {

    private static final String TAG = "MyFTP Locks";
    Switch DoorSwitch;
    Button returnButton, refreshButton;
    TextView door1Status;
    private Context cntx = null;
    String file_name = "status.json";
    String file_upload_name = "update.txt";
    private MyFTPClientFunctions ftpclient = null;
    public Boolean lockStatus;
    private Handler mHandle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locks);
        cntx = this.getBaseContext();
        ftpclient = new MyFTPClientFunctions();
        mHandle = new Handler();
        connect();
        door1Status = (TextView)findViewById(R.id.Status1);
        refreshButton = (Button)findViewById(R.id.Refresh_Button_Locks);
        DoorSwitch = (Switch)findViewById(R.id.DoorSwitch);
        DoorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       /* if(isChecked == lockStatus == true){
                            Log.d(TAG, "Already locked" + " stateVar: " + lockStatus.toString());
                            DoorSwitch.setChecked(true);
                            return;
                        } else if(isChecked == lockStatus == false){
                            Log.d(TAG, "Already unlocked" + " stateVar: " + lockStatus.toString());
                            DoorSwitch.setChecked(false);
                        }*/if(isChecked == true){
                            //Upload file to lock door
                            try {
                                File root = new File(Environment.getExternalStorageDirectory(),
                                        "AlphaOmega");
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File AlphaOmegafileUpload = new File(root, file_upload_name);
                                FileWriter writer = new FileWriter(AlphaOmegafileUpload);
                                writer.append("lock");
                                writer.flush();
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else{
                            //Upload file to unlock door
                            try {
                                File root = new File(Environment.getExternalStorageDirectory(),
                                        "AlphaOmega");
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File AlphaOmegafileUpload = new File(root, file_upload_name);
                                FileWriter writer = new FileWriter(AlphaOmegafileUpload);
                                writer.append("unlock");
                                writer.flush();
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                         new Thread(new Runnable() {
                             public void run() {
                                 boolean status = false;
                                 status = ftpclient.ftpUpload(Environment.getExternalStorageDirectory()
                                                 + "/AlphaOmega/" + file_upload_name,
                                         file_upload_name, "/", cntx);
                                 if (status) {
                                     Log.d(TAG, "Upload success");
                                 } else {
                                     Log.d(TAG, "Upload failed");
                                 }
                             }
                         }).start();

                     }
        });
        returnButton = (Button) findViewById(R.id.ReturntoMain_Locks);
        lockStatus = false;
        startUpLocks();

        onClickReturnButton();
        onClickRefreshButton();
        //refresh();
        //refreshLocks();

    }


    //This function will read the current state of the locks before activity really begins
    private void startUpLocks(){

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
                        Boolean data = jsonObj.getBoolean("locked");

                        if (data) {
                            mApp.setDoor(true);
                            Log.d(TAG, "Door Locked");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DoorSwitch.setChecked(true);
                                    door1Status.setText("Connected");
                                    door1Status.setTextColor(Color.BLACK);
                                    lockStatus = true;

                                }
                            });

                        } else {
                            mApp.setDoor(false);
                            Log.d(TAG, "Door Unlocked");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DoorSwitch.setChecked(false);
                                    door1Status.setText("Connected");
                                    door1Status.setTextColor(Color.BLACK);
                                    lockStatus = false;
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
            Log.d(TAG, ""+e);
        }
    }
    //This will read the state of the locks and refresh UI every 10 sec
    private void refreshLocks() {

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
                                Boolean data = jsonObj.getBoolean("locked");

                                if (data) {
                                    mApp.setDoor(true);
                                    Log.d(TAG, "Door Locked");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DoorSwitch.setChecked(true);
                                            door1Status.setText("Connected");
                                            door1Status.setTextColor(Color.BLACK);
                                            lockStatus = true;
                                        }
                                    });

                                } else {
                                    mApp.setDoor(false);
                                    Log.d(TAG, "Door Unlocked");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DoorSwitch.setChecked(false);
                                            door1Status.setText("Connected");
                                            door1Status.setTextColor(Color.BLACK);
                                            lockStatus= false;
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
                                                    Boolean data = jsonObj.getBoolean("locked");

                                                    if (data) {
                                                        mApp.setDoor(true);
                                                        Log.d(TAG, "Door Locked");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                DoorSwitch.setChecked(true);
                                                                door1Status.setText("Connected");
                                                                door1Status.setTextColor(Color.BLACK);
                                                                lockStatus = true;
                                                            }
                                                        });

                                                    } else {
                                                        mApp.setDoor(false);
                                                        Log.d(TAG, "Door Unlocked");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                DoorSwitch.setChecked(false);
                                                                door1Status.setText("Connected");
                                                                door1Status.setTextColor(Color.BLACK);
                                                                lockStatus = false;
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
                            }, 1000);
                        }else{

                            Toast.makeText(Locks.this,
                                    "Problems Connecting to Hub. Please Restart and try Again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                }
        );
    }

    public void connect() {

        if (isOnline(Locks.this)) {
            /*Toast.makeText(Locks.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();*/
            Log.d(TAG, "Online");
            connectToFTPAddress();
        } else {
            /*Toast.makeText(Locks.this,
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
