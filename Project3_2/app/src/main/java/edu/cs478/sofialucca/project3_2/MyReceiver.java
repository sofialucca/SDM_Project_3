package edu.cs478.sofialucca.project3_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.i("Project 3_2", "Programmatic receiver called");
        Intent intentActivity = new Intent();

        switch (intent.getStringExtra("nameActivity")) {
            case "Attractions":
                intentActivity.setClass(context, AttractionsActivity.class);

                break;
            case "Hotels":
                intentActivity.setClass(context, HotelsActivity.class);

                break;
            default:
                return;
        }

        intentActivity.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK ) ;
        context.startActivity(intentActivity);


    }
}