/*package swiftproduct.swift;

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
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.File;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

public class Thermal_Images extends AppCompatActivity {

    ImageButton thermalImage;
    Button returnButton, refreshButton;
    private MyFTPClientFunctions ftpclient = null;
    private static final String TAG = "MyFTP Thermal";
    String file_name = "thermal.jpg";
    Integer count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermal__images);
        ftpclient = new MyFTPClientFunctions();
        connect();
        thermalImage = (ImageButton)findViewById(R.id.Thermal_Image_Button);
        refreshButton= (Button) findViewById(R.id.Refresh_Button_Thermal);
        returnButton = (Button)findViewById(R.id.ReturntoMain_Thermal);
        onClickRefreshButton();
        onClickReturnButton();
        onClickPictureButton();

    }

    public void onClickPictureButton(){

        thermalImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        File imageFile = new File(Environment.getExternalStorageDirectory()
                                + "/AlphaOmega/Thermal/" + count.toString());
                        GlobalApplication mApp = ((GlobalApplication) getApplication());
                        Log.d(TAG, "Global Count: " + mApp.thermal_image_count.toString() + " imageFile: " + count.toString());
                        if (imageFile.exists()) {
                            thermalImage.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                            if (count == mApp.thermal_image_count)
                                count = 1;
                            else
                                count++;
                        }
                    }
                }
        );
    }


    public void onClickReturnButton(){

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
                            File root = new File(Environment.getExternalStorageDirectory(),
                                    "AlphaOmega/Thermal/");
                            if (!root.exists()) {
                                root.mkdirs();
                            }
                            try {

                                new Thread(new Runnable() {
                                    public void run() {
                                        boolean status = false;
                                        GlobalApplication mApp = ((GlobalApplication) getApplication());
                                        status = ftpclient.ftpDownload(file_name, Environment.getExternalStorageDirectory()
                                                + "/AlphaOmega/Thermal/" + count.toString());
                                        if (status) {
                                            Log.d(TAG, "Download success");
                                            mApp.increment_therm_count();
                                            Log.d(TAG, "Global Count: " + mApp.thermal_image_count.toString());
                                        } else {
                                            Log.d(TAG, "Download failed");
                                        }
                                    }
                                }).start();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }else{
                            Toast.makeText(Thermal_Images.this,
                                    "Problems Connecting to Hub. Please Restart and try Again.",
                                    Toast.LENGTH_LONG).show();

                        }
                    }


                    }
        );
    }

    public void connect() {

        if (isOnline(Thermal_Images.this)) {
            /*Toast.makeText(Thermal_Images.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "Online");
            connectToFTPAddress();
        } else {
           /* Toast.makeText(Thermal_Images.this,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG).show();
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
*/