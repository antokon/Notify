package com.example.antonio.notify;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;


import com.example.antonio.notify.db.DatabaseHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {

    DatabaseHandler mydb;
    Button buttonSave, btnviewAll;
    EditText editName, editMessage, editTextId;
    TextView tv, tv1;

    double latitude;
    double longitude;
    //PendingIntent pendingIntent;
    //AlarmManager alarmManager;
    //identify and track our permission request
    public static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mydb = new DatabaseHandler(this);
        buttonSave = (Button) findViewById(R.id.btn_save);
        btnviewAll = (Button) findViewById(R.id.btn_viewAll);
        editName = (EditText) findViewById(R.id.editname);
        editMessage = (EditText) findViewById(R.id.editmessage);
        editTextId = (EditText) findViewById(R.id.editText_id);
        TextView tv = (TextView) findViewById(R.id.pos);
        TextView tv1 = (TextView) findViewById(R.id.pos1);
       // AddData();
        viewAll();




        buttonSave.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Main2Activity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    0);
                            return;
                        }
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
//                            String lon =String.valueOf(longitude);
//                            String lat = String.valueOf(latitude);
                        Log.d("long", Double.toString(longitude));
                        Log.d("latitude", Double.toString(latitude));
                        Intent intent = new Intent(getApplicationContext(), ProximityAlertReceiver.class);
                        intent.putExtra("message", editMessage.getText().toString());
                        int notifId = id;
                        id++;
                        intent.putExtra("id", notifId);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        locationManager.addProximityAlert(latitude, longitude, 1, -1, pendingIntent);
                        Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();

                        boolean isInserted = mydb.addNotification(editName.getText().toString(), editMessage.getText().toString(), latitude, longitude);

                        if (isInserted == true)
                            Toast.makeText(Main2Activity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Main2Activity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );


    }



//    public static ArrayList<HashMap<String, Double>> compareValues(Context context) throws ClassNotFoundException, SQLException {
//        ArrayList<HashMap<String, Double>> notifications = new ArrayList<>();
//        // String sql = "Select LATITUDE, LONGITUDE From Notifications";
//        DatabaseHandler myDb = new DatabaseHandler(context);
//        Cursor c = myDb.getAllNotification();
//
//
//        while (c.moveToNext()) {
////           double lat = Double.parseDouble(c.getString(c.getColumnIndexOrThrow(Da.KEY_LAT)));
////           double lng = Double.parseDouble(c.getString(c.getColumnIndexOrThrow(ParkingSpotDBAdapter.KEY_LNG)));
////           String reg_latitude =  c.getString(c.getDouble(COL_4));
////           String reg_longitude = c.getString(c.getDouble("LONGITUDE"));
//            HashMap<String, Double> entry = new HashMap<>();
//            entry.put("latitude", c.getDouble(3));
//            entry.put("longitude", c.getDouble(4));
//            notifications.add(entry);
//            //Notifications.add(Double.parseDouble(c.getDouble(3) + "\n"));
//            //Notifications.add(Double.parseDouble(c.getDouble(4) + "\n"));
//        }
//        return notifications;
//
//    }

//    public void AddData() {
//
//       buttonSave.setOnClickListener(
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                        if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        longitude = location.getLongitude();
//                        latitude = location.getLatitude();
////                            String lon =String.valueOf(longitude);
////                            String lat = String.valueOf(latitude);
//                        Log.d("long", Double.toString(longitude));
//                        Log.d("latitude", Double.toString(latitude));
//                        Intent intent = new Intent();
//                        intent.putExtra("message", editMessage.getText().toString());
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        locationManager.addProximityAlert(latitude, longitude, 10, -1, pendingIntent);
//                        Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
//
//                        boolean isInserted = mydb.addNotification(editName.getText().toString(), editMessage.getText().toString(), latitude, longitude);
//
//                        if (isInserted == true)
//                            Toast.makeText(Main2Activity.this, "Data Inserted", Toast.LENGTH_LONG).show();
//                        else
//                            Toast.makeText(Main2Activity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = mydb.getAllNotification();
                        if (res.getCount() == 0) {
                            // show message
                            showMessage("Error", "Nothing found");

                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :" + res.getString(0) + "\n");
                            buffer.append("Name :" + res.getString(1) + "\n");
                            buffer.append("Message :" + res.getString(2) + "\n");
                            buffer.append("Lat :" + res.getString(3) + "\n");
                            buffer.append("Long :" + res.getString(4) + "\n\n");
                        }

                        // Show all data
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


