package com.cs478.sofialucca.project3_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    protected ImageButton attractions;
    protected ImageButton hotels;
    protected String typeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attractions = findViewById(R.id.attractionsBttn);
        hotels = findViewById(R.id.hotelsBttn);
        hotels.setOnClickListener(buttonListener);
        attractions.setOnClickListener(buttonListener);
    }


    public View.OnClickListener buttonListener = (v) -> {
        String toastMessage = "Redirection to '";

        switch(v.getId()){
            case R.id.attractionsBttn:
                toastMessage += "Chicago's attractions'";
                typeRequest = "Attractions";
                break;
            case R.id.hotelsBttn:
                toastMessage += "Chicago's hotels'";
                typeRequest = "Hotels";
                break;
            default:
                return;
        }
        Log.i("Project 3_1",toastMessage);
        // check if I have permission

        checkPermission();
    };
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startIntentRequest();
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
        }

    }
    private void startIntentRequest(){
        Intent intentRequest = new Intent();
        intentRequest.setAction("com.cs478.sofialucca.BroadcastReceiver.displayInfos");
        intentRequest.putExtra("nameActivity", typeRequest);

        Log.i("Project 3_1","Okay permission");
        //intentRequest.putExtra("contextRequest", MainActivity.this.toString());
        //intentRequest.putExtra("contextRequest",(Serializable) MainActivity.this);
        sendBroadcast(intentRequest);
        finish();
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), "edu.uic.cs478.fall22.mp3") == PackageManager.PERMISSION_GRANTED){
            startIntentRequest();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"edu.uic.cs478.fall22.mp3"}, 1);
        }
    }

}