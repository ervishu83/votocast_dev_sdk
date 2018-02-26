package com.votocast.votocast;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.PagerAdapter;
import dialogFragment.LoadVideoFragment;
import fragments.ActivityFragment;
import fragments.CampaignFragment;
import fragments.ProfileFragment;
import fragments.SearchFragment;
import fragments.TestHomeFragment;

public class VC_MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    protected static final int SELECT_PICTURE = 1;
    private static final int PROFIL_PERMISSION_REQUEST_CODE = 111;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 124;
    TabLayout tabLayout;

    // Location
    private LocationRequest mLocationRequest;
    private static GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private static boolean isLocationSend = false;
    private static boolean isAskedForGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor(Constant.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Constant.colorPrimaryDark));
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//        Tracker t = ((MyAppTracker)this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
//        t.setScreenName("Main Activity");
//        t.send(new HitBuilders.AppViewBuilder().build());

        // ----------- simple tab layout code ----------

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_tab));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search_tab));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.play));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_flash_tab));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        Log.e("HomeActivity", "not null -- " + getIntent().toString() + " - " + getIntent().getStringExtra("campId"));
        if (getIntent().getStringExtra("campId") != null && !getIntent().getStringExtra("campId").equals("")) {
//            replaceFragment(new CampaignFragment());
            CampaignFragment fragment = new CampaignFragment();
            Bundle b1 = new Bundle();
            b1.putString("campId", getIntent().getStringExtra("campId"));
            b1.putString("from", "main");
            fragment.setArguments(b1);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
//            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
            getIntent().removeExtra("campId");
        } else
            replaceFragment(new TestHomeFragment());


//        replaceFragment(new TestHomeFragment());
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
//                        replaceFragment(new HomeFragment());
                        replaceFragment(new TestHomeFragment());
                        break;
                    case 1:
//                        tabLayout.setVisibility(View.GONE);
                        replaceFragment(new SearchFragment());
                        break;
                    case 2:
//                        uploadVideo();
                        int result = ContextCompat.checkSelfPermission(VC_MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (result == PackageManager.PERMISSION_GRANTED) {


//                            MyUtils.showToast(VC_MainActivity.this.getApplicationContext(), "Please wait videos are loading..");
                            Constant.saveSharedData(VC_MainActivity.this, "camp_id", "");
                            Constant.saveSharedData(VC_MainActivity.this, "short_code", "");
//                            tabLayout.setVisibility(View.GONE);
                            LoadVideoFragment fragment = new LoadVideoFragment();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.frame_container, fragment);
                            ft.addToBackStack(fragment.getClass().getName());
                            ft.commit();
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(VC_MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(VC_MainActivity.this, "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
                            } else {
                                ActivityCompat.requestPermissions(VC_MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            }
                        }
                        break;
                    case 3:
//                        replaceFragment(new DebugFragment());
                        replaceFragment(new ActivityFragment());
                        break;
                    case 4:
                        ProfileFragment fragment = new ProfileFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("user_id", "0");
                        b1.putString("from", "main");

                        fragment.setArguments(b1);
                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft = fm1.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                    default:
                        replaceFragment(new TestHomeFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

//                Log.i("tab reselect",tab.getPosition()+"");

                switch (tab.getPosition()) {
                    case 0:
//                        replaceFragment(new HomeFragment());
                        replaceFragment(new TestHomeFragment());
                        break;
                    case 1:
                        replaceFragment(new SearchFragment());
                        break;
                    case 2:
//                        uploadVideo();
                        int result = ContextCompat.checkSelfPermission(VC_MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (result == PackageManager.PERMISSION_GRANTED) {

//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    MyUtils.showToast(VC_MainActivity.this, "Please wait videos are loading..");
//                                }
//                            });

                            Constant.saveSharedData(VC_MainActivity.this, "camp_id", "");
                            Constant.saveSharedData(VC_MainActivity.this, "short_code", "");
//                            tabLayout.setVisibility(View.GONE);
                            LoadVideoFragment fragment = new LoadVideoFragment();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.frame_container, fragment);
                            ft.addToBackStack(fragment.getClass().getName());
                            ft.commit();
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(VC_MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(VC_MainActivity.this, "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
                            } else {
                                ActivityCompat.requestPermissions(VC_MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            }
                        }
                        break;
                    case 3:
                        replaceFragment(new ActivityFragment());
//                        replaceFragment(new DebugFragment());
                        break;
                    case 4:
                        ProfileFragment fragment = new ProfileFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("user_id", "0");
                        b1.putString("from", "main");

                        fragment.setArguments(b1);
                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft = fm1.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                    default:
                        replaceFragment(new TestHomeFragment());
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    MyUtils.showToast(VC_MainActivity.this,"Now reselect video");

//                    Log.i("on permission video", "accept");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyUtils.showToast(VC_MainActivity.this.getApplicationContext(), "Please wait videos are loading..");
                        }
                    });
                    Constant.saveSharedData(VC_MainActivity.this, "camp_id", "");
                    Constant.saveSharedData(VC_MainActivity.this, "short_code", "");
//                            tabLayout.setVisibility(View.GONE);
                    LoadVideoFragment fragment = new LoadVideoFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.commitAllowingStateLoss();
                } else {
                    Toast.makeText(this, "Permission Denied, We can not get videos.", Toast.LENGTH_LONG).show();
                }
                break;
            case PROFIL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("on permission image", "accept");

                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_PICTURE);
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot upload profile picture.", Toast.LENGTH_LONG).show();
                }
                break;
            case LOCATION_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied for location.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void setTabHignlight(int index) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public ActionBar getActivityActionBar() {
        return getSupportActionBar();
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            final AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
//            myDialog.setTitle("Confirm")
//                    .setMessage("Do you really want to close this app?")
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .create()
//                    .show();
//        } else {
//            super.onBackPressed();
//        }

//    }

    // --------------------- location ---------------------
    @Override
    public void onStart() {
        super.onStart();
        //Initialize Google Play Services
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.e("MyLocation", "start build google api client");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Constant.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (Constant.mLastLocation == null) {
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    Log.i("MyLocation", "onConnected isGPSEnabled- " + isGPSEnabled);
                    if (isGPSEnabled) {
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(1000);
                        mLocationRequest.setFastestInterval(1000);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        Constant.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if(Constant.mLastLocation != null) {
                            new setLocationData().execute();
                            isLocationSend = true;
                        }
                    } else {
                        if (!isAskedForGPS && Constant.mLastLocation != null)
                            showSettingsAlert();
                    }
                } else {
                    Log.i("MyLocation", "onConnected - " + Constant.mLastLocation.getLatitude() + " - " + Constant.mLastLocation.getLongitude());
//                    setUpMap();
                    if (!isLocationSend && Constant.mLastLocation != null) {
                        new setLocationData().execute();
                        isLocationSend = true;
                    }
                }
            } else
                buildGoogleApiClient();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("MyLocation", "onConnectionSuspended ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("MyLocation", "onConnectionFailed ");
    }

    @Override
    public void onLocationChanged(Location location) {
        if (Constant.mLastLocation == null) {
            Constant.mLastLocation = location;
//            setUpMap();
            if (!isLocationSend && Constant.mLastLocation != null) {
                new setLocationData().execute();
                isLocationSend = true;
            }
            Log.i("MyLocation", "OnChange - " + Constant.mLastLocation.getLatitude() + " - " + Constant.mLastLocation.getLongitude());
        }
        Constant.mLastLocation = location;
        //stop location updates
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void showSettingsAlert() {
        isAskedForGPS = true;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Location Alert");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 1);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.setCancelable(false);
        alertDialog.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.i("MyLocation", "onActivity Result - " + requestCode + " - " + resultCode);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Constant.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (Constant.mLastLocation == null) {
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(1000);
                        mLocationRequest.setFastestInterval(1000);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
                    } else {
                        Log.i("MyLocation", "onActivity Result - " + Constant.mLastLocation.getLatitude() + " - " + Constant.mLastLocation.getLongitude());
                        if (!isLocationSend && Constant.mLastLocation != null) {
                            isLocationSend = true;
                            new setLocationData().execute();
                        }
                    }
                } else
                    buildGoogleApiClient();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    class setLocationData extends AsyncTask<Void, Void, Void> {
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(VC_MainActivity.this, "pref_login");

            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("latitude", Constant.mLastLocation.getLatitude() + ""));
            pair.add(new BasicNameValuePair("longitude", Constant.mLastLocation.getLongitude() + ""));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.add_location_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();
                    isLocationSend = true;
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }
}
