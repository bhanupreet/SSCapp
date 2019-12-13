package com.ssc.Derox;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SSCapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
