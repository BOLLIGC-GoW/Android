package swiftproduct.swift;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Alert extends AppCompatActivity {

    Button returnButton;
    ImageView alertPic;
    private static final String TAG = "MyFTP Alert";
    private MyFTPClientFunctions ftpclient = null;
    String file_upload_name = "update.txt";
    private Context cntx = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        cntx = this.getBaseContext();
        returnButton = (Button) findViewById(R.id.ReturntoMain_Alert);
        alertPic = (ImageView)findViewById(R.id.AlertPicture);
        ftpclient = new MyFTPClientFunctions();
        connect();
        GlobalApplication mApp = ((GlobalApplication) getApplication());
        File imageFile = new File(Environment.getExternalStorageDirectory()
                + "/AlphaOmega/Camera/" + mApp.alertImageNum);
        Log.d(TAG, "Global Count: " + mApp.cam_image_count.toString() + " AlertFile: " + mApp.alertImageNum.toString());
        if (imageFile.exists()) {
            alertPic.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        }
        onClickReturnButton();
        //Upload file to unlock door
        try {
            File root = new File(Environment.getExternalStorageDirectory(),
                    "AlphaOmega");
            if (!root.exists()) {
                root.mkdirs();
            }
            File AlphaOmegafileUpload = new File(root, file_upload_name);
            FileWriter writer = new FileWriter(AlphaOmegafileUpload);
            writer.append("camera");
            writer.flush();
            writer.close();

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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

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

    public void connect() {

        if (isOnline(Alert.this)) {
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
