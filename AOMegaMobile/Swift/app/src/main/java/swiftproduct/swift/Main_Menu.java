package swiftproduct.swift;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Main_Menu extends AppCompatActivity {


    private static Button aboutButton;
    private static Button thermalImageButton;
    private static Button cameraButton;
    private static Button lockButton;
    private static Button magneticSwitchButton;
    String file_name = "image_counts.json";
    private static final String TAG = "MyFTPC Main Menu";
    private MyFTPClientFunctions ftpclient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        //ftpclient = new MyFTPClientFunctions();
        //connect();
        aboutButton = (Button) findViewById(R.id.About_Button);
        onClickAboutButton();
        //thermalImageButton = (Button) findViewById(R.id.Thermal_Image_Button);
        //onClickThermalImageButton();
        cameraButton = (Button) findViewById(R.id.Door_Camera_Button);
        onClickCameraButton();
        lockButton = (Button) findViewById(R.id.Door_Locks_Button);
        onClickLockButton();
        magneticSwitchButton = (Button) findViewById(R.id.Magnetic_Sensors_Button);
        onClickMagneticSensorButtton();

    }

    public void onClickAboutButton() {


        aboutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("swiftproduct.swift.About");
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickLockButton() {


        lockButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                        + "/AlphaOmega/" + file_name);
                                if (status) {
                                    Log.d(TAG, "Open Lock Download success");
                                } else {
                                    Log.d(TAG, "Open Lock Download failed");
                                }
                            }
                        }).start();*/
                        Intent intent = new Intent("swiftproduct.swift.Locks");
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickMagneticSensorButtton() {


        magneticSwitchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                        + "/AlphaOmega/" + file_name);
                                if (status) {
                                    Log.d(TAG, "Open Doors Download success");
                                } else {
                                    Log.d(TAG, "Open Doors Download failed");
                                }
                            }
                        }).start();*/
                        Intent intent = new Intent("swiftproduct.swift.Magnetic_Sensors");
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickThermalImageButton() {


        thermalImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                        + "/AlphaOmega/" + file_name);
                                if (status) {
                                    Log.d(TAG, "Open Thermal Download success");
                                } else {
                                    Log.d(TAG, "Open Thermal Download failed");
                                }
                            }
                        }).start();*/
                        Intent intent = new Intent("swiftproduct.swift.Thermal_Images");
                        startActivity(intent);
                    }
                }
        );
    }


    public void onClickCameraButton() {


        cameraButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                        + "/AlphaOmega/" + file_name);
                                if (status) {
                                    Log.d(TAG, "Open Camera Download success");
                                } else {
                                    Log.d(TAG, "Open Camera Download failed");
                                }
                            }
                        }).start();*/
                        Intent intent = new Intent("swiftproduct.swift.Camera");
                        startActivity(intent);
                    }
                }
        );
    }

    public void connect() {

        if (isOnline(Main_Menu.this)) {
            Toast.makeText(Main_Menu.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();
            connectToFTPAddress();
        } else {
            Toast.makeText(Main_Menu.this,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG).show();
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
}
