package swiftproduct.swift;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Camera extends AppCompatActivity {

    ImageButton cameraImage;
    Button returnButton, refreshButton;
    private MyFTPClientFunctions ftpclient = null;
    private static final String TAG = "MyFTPClientFunctions";
    String image_file_name = "/home/pi/Desktop/Doorway/cam.png", file_name = "status.json" ;

    Integer count = 1;
    AlertDialog alertDialog;
    private Handler mHandle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        alertDialog = new AlertDialog.Builder(Camera.this).create();
        ftpclient = new MyFTPClientFunctions();
        connect();
        cameraImage = (ImageButton)findViewById(R.id.Camera_Button);
        refreshButton= (Button) findViewById(R.id.Refresh_Button_Camera);
        returnButton = (Button)findViewById(R.id.ReturntoMain_Camera);
        GlobalApplication mApp = ((GlobalApplication) getApplication());
        count = mApp.cam_image_count;
        onClickRefreshButton();
        onClickReturnButton();
        onClickPictureButton();

    }

    public void onClickPictureButton(){

        cameraImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalApplication mApp = ((GlobalApplication) getApplication());
                        if (count == 5)
                            count = 1;
                        File imageFile = new File(Environment.getExternalStorageDirectory()
                                + "/AlphaOmega/Camera/" + count.toString());

                        Log.d(TAG, "Global Count: " + mApp.cam_image_count.toString() + " imageFile: " + count.toString());
                        if (imageFile.exists()) {
                            cameraImage.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                            if (count == mApp.cam_image_count+1)
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

                        Intent intent = new Intent ("swiftproduct.swift.Main_Menu");
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
                                            Log.d(TAG, "Download success " + file_name);
                                        } else {
                                            Log.d(TAG, "Download failed " + file_name);
                                        }
                                    }
                                }).start();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            Utils.delay(3, new Utils.DelayCallback() {
                                @Override
                                public void afterDelay() {
                                    // Do something after delay

                                    File root = new File(Environment.getExternalStorageDirectory(),
                                            "AlphaOmega/Camera/");
                                    if (!root.exists()) {
                                        root.mkdirs();
                                    }
                                    File file = new File(Environment.getExternalStorageDirectory()
                                            + "/AlphaOmega/" + file_name);
                                    if (file.exists()) {
                                        try {
                                            FileInputStream stream = new FileInputStream(file);
                                            String jsonStr = null;
                                            try {
                                                GlobalApplication mApp = ((GlobalApplication) getApplication());
                                                FileChannel fc = stream.getChannel();
                                                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                                                jsonStr = Charset.defaultCharset().decode(bb).toString();

                                                JSONObject jsonObj = new JSONObject(jsonStr);

                                                // Getting data from JSON
                                                Boolean data = jsonObj.getBoolean("detected_cam");

                                                if (data) {
                                                    //Get the new image
                                                    getImage();
                                                    alertDialog.setTitle("Alert");
                                                    alertDialog.setMessage("!Individual Detected!");
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent intent = new Intent("swiftproduct.swift.Alert");
                                                                    startActivity(intent);
                                                                    finish();
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                    alertDialog.show();
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
                                }
                            });

                        }else{

                            Toast.makeText(Camera.this,
                                    "Problems Connecting to Hub. Please Restart and try Again.",
                                    Toast.LENGTH_LONG).show();
                        }
                }


                }
        );
    }

    public void getImage(){

        try {

            new Thread(new Runnable() {
                public void run() {
                    boolean status = false;
                    GlobalApplication mApp = ((GlobalApplication) getApplication());
                    Integer temp = mApp.cam_image_count +1;
                    status = ftpclient.ftpDownload(image_file_name, Environment.getExternalStorageDirectory()
                            + "/AlphaOmega/Camera/" + temp.toString());
                    if (status) {
                        Log.d(TAG, "Download success for Image");
                        mApp.increment_cam_count();
                        mApp.alertImageNum = mApp.cam_image_count;
                        Log.d(TAG, "mApp.alertImageNum"+mApp.alertImageNum.toString());
                        count++;
                    } else {
                        Log.d(TAG, "Download Failed for Image");
                    }
                }
            }).start();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void connect() {

        if (isOnline(Camera.this)) {
            /*Toast.makeText(Camera.this,
                    "Online!",
                    Toast.LENGTH_LONG).show();*/
            Log.d(TAG, "Online");
            connectToFTPAddress();
        } else {
            /*Toast.makeText(Camera.this,
                    "Please check your internet connection!",
                    Toast.LENGTH_LONG).show();*/
            Log.d(TAG, "Offline");
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
