package edu.cs478.sofialucca.project3_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class AttractionsActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter ;
    public static String[] mNamesArray;
    public static String[] mWebsitesArray;

    FragmentManager mFragmentManager;
    private WebsiteFragment mWebsiteFragment=new WebsiteFragment();
    private NamesFragment mNamesFragment= new NamesFragment();
    private FrameLayout mNamesFrameLayout, mWebsitesFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG_WEB_FRAGMENT = "website_fragment_attractions";
    private static final String TAG_NAMES_FRAGMENT = "names_fragment_attractions";
    private static final String TAG = "AttractionsActivity";
    private ListViewModel mModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + ": entered onCreate()");
        mNamesArray = getResources().getStringArray(R.array.Attractions);
        mWebsitesArray = getResources().getStringArray(R.array.Attractions_links);
        setContentView(R.layout.activity_main);
        mWebsiteFragment = new WebsiteFragment();
        ///Log.i("AttractionsActivity",mNamesArray[0]);
        //set toolbar
        /*Toolbar myToolbar = findViewById(R.id.my_toolbar) ;
        setSupportActionBar(myToolbar) ;*/

        //set broadcast listener
        mFilter = new IntentFilter("com.cs478.sofialucca.BroadcastReceiver.displayInfos");
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, mFilter);
        /*
        //to allow the app to open over other app need modification of settings
        if (!Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);

        }
        // Start a new FragmentTransaction
*/


        mNamesFrameLayout = (FrameLayout) findViewById(R.id.name_fragment_container);
        mWebsitesFrameLayout = (FrameLayout) findViewById(R.id.website_fragment_container);


        // Get a reference to the SupportFragmentManager instead of original FragmentManager
        mFragmentManager = getSupportFragmentManager();
        //check retained fragments
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
         mNamesFragment = (NamesFragment) mFragmentManager.findFragmentByTag(TAG_NAMES_FRAGMENT);
        mWebsiteFragment = (WebsiteFragment) mFragmentManager.findFragmentByTag(TAG_WEB_FRAGMENT);


        if(mNamesFragment == null){
            Log.i("AttractionsActivity","NOT retained nameFragment");
            mNamesFragment = new NamesFragment();
            fragmentTransaction.add(R.id.name_fragment_container,
                    mNamesFragment, TAG_NAMES_FRAGMENT);
        }
        fragmentTransaction.commit();
        mFragmentManager.addOnBackStackChangedListener(
                // UB 2/24/2019 -- Use support version of Listener
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });
        if(mWebsiteFragment == null){
            Log.i("AttractionsActivity","NOT retained webFragment");
            mWebsiteFragment = new WebsiteFragment();
            FragmentTransaction fragmentTransaction2 = mFragmentManager.beginTransaction() ;

            // add quote fragment to display
            fragmentTransaction2.add(R.id.website_fragment_container,
                    mWebsiteFragment,TAG_WEB_FRAGMENT);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction2.addToBackStack(TAG_WEB_FRAGMENT);

            // Commit the FragmentTransaction
            fragmentTransaction2.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }


/*
        // Add the TitleFragment to the layout
        // UB: 10/2/2016 Changed add() to replace() to avoid overlapping fragments
        fragmentTransaction.replace(
                R.id.name_fragment_container,
                mNamesFragment);

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager.addOnBackStackChangedListener(
                // UB 2/24/2019 -- Use support version of Listener
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });*/

        // set up model observer to add and remove quotes fragment
        // Note: the fragment object never gets deleted, it just gets removed when the user
        // presses the "back" button

        mModel = new ViewModelProvider(this).get(ListViewModel.class) ;
        mModel.getSelectedItem().observe(this, item -> {
            Log.i("CHECK", "Is"+mWebsiteFragment.isAdded());
            Log.i("AttractionsActivity","Web Item" + mWebsiteFragment.getShownIndex()+item);
            if (!mWebsiteFragment.isAdded() && item != -1) {
                Log.i("AttractionsActivity", "Observer for onCreate");
                // if(mWebsiteFragment == null){

                FragmentTransaction fragmentTransaction2 = mFragmentManager.beginTransaction() ;

                // add quote fragment to display
                fragmentTransaction2.add(R.id.website_fragment_container,
                        mWebsiteFragment,TAG_WEB_FRAGMENT);

                // Add this FragmentTransaction to the backstack
                fragmentTransaction2.addToBackStack(TAG_WEB_FRAGMENT);

                // Commit the FragmentTransaction
                fragmentTransaction2.commit();

                // Force Android to execute the committed FragmentTransaction
                mFragmentManager.executePendingTransactions();
                mWebsiteFragment.showQuoteAtIndex(item);
            }else if(item == -1){
                mNamesFragment.removeSelected(item);
            }else{
                mWebsiteFragment.showQuoteAtIndex(item);
            }

            setLayout();
        });

        setLayout() ;


        //((TextView) findViewById(R.id.textView)).setText("Attractions");
        Log.i("Project 3_2", " ::" + AttractionsActivity.this);

    }

    private void setLayout() {
        //Log.i("AttractionsActivity", "item:"+mNamesFragment.isAdded()+mWebsiteFragment.isAdded());
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Determine whether the QuoteFragment has been added
            //if (!mWebsiteFragment.isAdded()) {
            if(mWebsiteFragment.getShownIndex() == -1){
                // Make the TitleFragment occupy the entire layout
                mNamesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                mWebsitesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));
            } else {

                // Make the TitleLayout take 1/3 of the layout's width
                mNamesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));

                // Make the QuoteLayout take 2/3's of the layout's width
                mWebsitesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
        } else {
            //if (!mWebsiteFragment.isAdded()) {

            if(mWebsiteFragment.getShownIndex() == -1){
                // Make the TitleFragment occupy the entire layout
                mNamesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                mWebsitesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));

            } else {
                Log.i("AttractionsActivity","Web Item" + mModel.getSelectedItem());
                // Make the TitleLayout take 1/3 of the layout's width
                mNamesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT,0)));

                // Make the QuoteLayout take 2/3's of the layout's width
                mWebsitesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));
            }
        }

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
                Toast.makeText(getApplicationContext(),"Attractions already selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.hotels_item:
                Intent intentActivity = new Intent(getApplicationContext(), HotelsActivity.class);
                intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT ) ;
                startActivity(intentActivity);
                mModel.selectItem(-1);
                finish();
                return true;
            default:
                return false;
        }
    }


    public void onDestroy(){
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}