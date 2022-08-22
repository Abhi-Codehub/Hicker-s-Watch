package com.example.hickerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.ULocale;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        LocationManager locationManager;
        LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
            updateLocationInfo(location);

            }
        };
        // has the user gives us the permission of using location (Checking whether user gave permission or not )
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            // if not then this code will run asking for the permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ,1);
        }
        // if the user has permitted for the location
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            // if location granted then the last location code is this cause in case the app opens then the last location can be accessed
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null){
                updateLocationInfo(lastKnownLocation);
            }
        }
    }

    @Override //    GET TO KNOW WHEN SOMEONE SAYS YES OR NO TO THE ASKED PERMISSIONS
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
              startListening();
            }
        }
        // we formed a method called startListening through which we dont need to write the permission granted and if yes then for asking for the exact location code
        public void startListening(){
            // If the permission is granted then
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                // the application is asked for the exact location
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
    }

    public void updateLocationInfo(Location location){
        TextView latTextView = findViewById(R.id.latTextView);
        TextView lonTextView = findViewById(R.id.lonTextView);
        TextView accTextView = findViewById(R.id.accTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView addTextView = findViewById(R.id.addTextView);

        lonTextView.setText("Longitude: "+ Double.toString(location.getLongitude()));
        latTextView.setText("Latitude: "+ Double.toString(location.getLatitude()));
        accTextView.setText("Accuracy: "+ Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: "+ Double.toString(location.getAltitude()));

        String Address = "Address Couldn't Found :(";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            // for getting user actual current address.
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if(listAddress != null && listAddress.size()>0 ){
                Address = "Address : \n";
                // Street address
                if (listAddress.get(0).getThoroughfare() != null){
                    Address += listAddress.get(0).getThoroughfare() +"\n" ;
                }
                //City name
                    if (listAddress.get(0).getLocality() != null){
                            Address += listAddress.get(0).getLocality() + " " ;
                }
                //State name
                if (listAddress.get(0).getPostalCode() != null){
                    Address += listAddress.get(0).getPostalCode() + "\n" ;
                }

                if (listAddress.get(0).getAdminArea() != null){
                    Address += listAddress.get(0).getAdminArea()  ;
                }
            }

        }
            catch (Exception e) {
            e.printStackTrace();
        }
        addTextView.setText(Address);

    }
}