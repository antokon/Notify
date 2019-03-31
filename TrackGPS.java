package com.example.antonio.notify;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Antonio on 2/18/2018.
 */

public class TrackGPS extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;
    Location loc;
    public double latitude;
    public double longitude;
    public Location previousBestLocation = null;
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static double currentLat;
    private static double currentLon;
    Intent intent;
    int counter = 0;
    LocationListener listener;

    public static final String BROADCAST_ACTION = "Location hello";
    private static final int TWO_MINUTES = 1000 * 10;
    public LocationManager locationManager;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;
    private static final int REQUEST_CODE_PERMISSION = 1;

    private static final long MIN_TIME_BW_UPDATES = 10000;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Activity activity;

    public TrackGPS(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, pendingIntent);
        } else {

        }
    }

//    @Override
//    public void onStart(Intent intent, int startId) {
//      onLocationChanged(loc);
//    }

    private void getLocation() {


        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // No network provider is enabled
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                longitude = loc.getLongitude();
                latitude = loc.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request Permission >> ActivityCompat#requestPermissions
               // Act on User's Response
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        return ;
            }
            locationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
            //Log.d("GPS Enabled", "GPS Enabled");


          //  return loc;
        }


//    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
//        if (currentBestLocation == null) {
//            // A new location is always better than no location
//            return true;
//        }
//
//        // Check whether the new location fix is newer or older
//        long timeDelta = location.getTime() - currentBestLocation.getTime();
//        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
//        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
//        boolean isNewer = timeDelta > 0;
//
//        // If it's been more than two minutes since the current location, use the new location
//        // because the user has likely moved
//        if (isSignificantlyNewer) {
//            return true;
//            // If the new location is more than two minutes older, it must be worse
//        } else if (isSignificantlyOlder) {
//            return false;
//        }
//
//        // Check whether the new location fix is more or less accurate
//        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
//        boolean isLessAccurate = accuracyDelta > 0;
//        boolean isMoreAccurate = accuracyDelta < 0;
//        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
//
//        // Check if the old and new location are from the same provider
//        boolean isFromSameProvider = isSameProvider(location.getProvider(),
//                currentBestLocation.getProvider());
//
//        // Determine location quality using a combination of timeliness and accuracy
//        if (isMoreAccurate) {
//            return true;
//        } else if (isNewer && !isLessAccurate) {
//            return true;
//        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            return true;
//        }
//        return false;
//    }

//    private boolean isSameProvider(String provider1, String provider2) {
//        if (provider1 == null) {
//            return provider2 == null;
//        }
//        return provider1.equals(provider2);
//    }

    public double getLongitude(Location loc) {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
            // Log.d("long",Double.toString(longitude) );
            Log.d("latitude", Double.toString(latitude));
        }
        return latitude;

    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


//    public void stopUsingGPS() {
//        if (locationManager != null) {
//
//            locationManager.removeUpdates(TrackGPS.this);
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(final Location loc) {
//        Log.d("", "Location changed");
//        if (isBetterLocation(loc, previousBestLocation)) {
        loc.getLatitude();
        loc.getLongitude();
        try {
            ArrayList<HashMap<String, Double>> geoFences = Main2Activity.compareValues(mContext);
            for (HashMap<String, Double> entry :
                    geoFences) {
                double savedlatitude = entry.get("latitude");
                double savedlongitude = entry.get("longitude");
                Location A = new Location("saved Loc");
                A.setLatitude(savedlatitude);
                A.setLongitude(savedlongitude);
                //sqrt[(a1-a2)^2 + (b1 - b1)^2]
                //copute the distance between entry and loc
                double distance = loc.distanceTo(A);
                if (distance <= 2) {
                    if (alarmManager == null) {
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(this, AlarmReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000,
                                pendingIntent);
                    }


                }
                // if the distance is less that 2m
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
//            intent.putExtra("Latitude", loc.getLatitude());
//            intent.putExtra("Longitude", loc.getLongitude());
//            intent.putExtra("Provider", loc.getProvider());
//            sendBroadcast(intent);
//
//
//        }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


//    private void addListenerLocation() {
//        mLocationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        mLocationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                currentLat = location.getLatitude();
//                currentLon = location.getLongitude();
//
//                Toast.makeText(getBaseContext(), currentLat + "-" + currentLon, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                try {
//                    Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (lastKnownLocation != null) {
//                        currentLat = lastKnownLocation.getLatitude();
//                        currentLon = lastKnownLocation.getLongitude();
//                    }
//                } catch (SecurityException e) {
//
//                }
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 2, mLocationListener);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(mLocationListener);
    }

//    public void onDestroy() {
//        // handler.removeCallbacks(sendUpdatesToUI);
//        super.onDestroy();
//        Log.d("STOP_SERVICE", "DONE");
//        locationManager.removeUpdates(listener);
//    }
}
