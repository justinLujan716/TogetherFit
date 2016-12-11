package com.example.heem.togetherfit;

import java.util.ArrayList;

/**
 * Created by Heem on 12/10/16.
 * This class to be used by add friends, in filter part
 */

public class userInfo {

    ArrayList<String> username;
    ArrayList<String> fitnesstype;
    ArrayList<String> type;
    ArrayList<String> image;

    public userInfo(ArrayList<String> username, ArrayList<String> fitnesstype, ArrayList<String> type, ArrayList<String> image) {
        this.username = username;
        this.fitnesstype = fitnesstype;
        this.type = type;
        this.image = image;
    }

    public ArrayList<String> getUsername() {
        return username;
    }

    public void setUsername(ArrayList<String> username) {
        this.username = username;
    }

    public ArrayList<String> getFitnesstype() {
        return fitnesstype;
    }

    public void setFitnesstype(ArrayList<String> fitnesstype) {
        this.fitnesstype = fitnesstype;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }
}
