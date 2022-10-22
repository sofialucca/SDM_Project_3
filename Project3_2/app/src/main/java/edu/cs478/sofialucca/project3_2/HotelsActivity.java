package edu.cs478.sofialucca.project3_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HotelsActivity extends AppCompatActivity {


    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //set broadcast listener

        //((TextView) findViewById(R.id.textView)).setText("Hotels");
        mFilter = new IntentFilter("com.cs478.sofialucca.BroadcastReceiver.displayInfos");
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, mFilter);
    }

    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.attractions_item:
                Intent intentActivity = new Intent(HotelsActivity.this, AttractionsActivity.class);
                intentActivity.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK ) ;
                startActivity(intentActivity);
                finish();
                return true;
            case R.id.hotels_item:
                Toast.makeText(getApplicationContext(),"Hotels already selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }


    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}