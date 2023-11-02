package com.program.gpstracking;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApplication {
    private static MyApplication extends Application {

    private static MyApplication singleton;

    private List<Location> myLocations;

    public List<Location> getMyLocation() {
        return.myLocations;
        }

        public void setMyLocations(List<Location> myLocations) {
        this.myLocations = mylocations;
        }

    public MyApplication getInstance() {
        return singleton;
        }

        public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
        }


}
