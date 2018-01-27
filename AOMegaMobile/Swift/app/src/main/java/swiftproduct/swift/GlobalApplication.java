package swiftproduct.swift;

import android.app.Activity;
import android.app.Application;

/**
 * Created by Charles on 10/15/17.
 */
public class GlobalApplication  extends Application{

    public String userID;
    public String IP_Address;
    public String Password;
    public Integer cam_image_count;
    public Boolean lock;
    public Boolean door;
    public Boolean connected;
    public Integer alertImageNum;


    public GlobalApplication() {
        this.userID = "";
        this.IP_Address = "";
        this.Password = "";
        this.cam_image_count = 4;
        this.lock = false;
        this.door = true;
        this.connected = false;
        this.alertImageNum = 0;

    }

    public GlobalApplication(String userID, String IP_Address, String password, Integer alertImageNum, Integer cam_image_count, Boolean lock, Boolean door, Boolean connected) {
        this.userID = userID;
        this.IP_Address = IP_Address;
        this.Password = password;
        this.cam_image_count = 4;
        this.lock = lock;
        this.door = door;
        this.connected = connected;
        this.alertImageNum = alertImageNum;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String str) {
        this.userID = str;
    }

    public String getIP_Address() {
        return IP_Address;
    }

    public void setIP_Address(String str) {
        this.IP_Address = str;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String str) {
        this.Password = str;
    }


    public Integer getCam_image_count() {
        return cam_image_count;
    }

    public void setCam_image_count(Integer cam_image_count) {
        this.cam_image_count = cam_image_count;
    }

    public void increment_cam_count(){

        this.cam_image_count = this.cam_image_count +1;
    }


    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Boolean getDoor() {
        return door;
    }

    public void setDoor(Boolean door) {
        this.door = door;
    }


}
