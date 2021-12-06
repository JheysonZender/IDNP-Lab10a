package com.example.lab10a;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    DataBaseHelper miBD;
    ListView listac;

    double lat;
    double lon;
    double alt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listac = (ListView) findViewById(R.id.ListView);
        miBD = new DataBaseHelper(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                check100(location);
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

        Location lastKnownLocation;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    locationListener);
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = lastKnownLocation.getLatitude();
            lon = lastKnownLocation.getLongitude();
            alt = lastKnownLocation.getAltitude();
            add(lat, lon, alt);
            updatetable();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void add(double a, double b, double c){
        boolean insertData = miBD.addData(a,b,c);
        if(insertData == true){
    //        Toast.makeText(this, "Datos insertados correctamente", Toast.LENGTH_LONG).show();
        }else{
     //       Toast.makeText(this, "Algo salio mal", Toast.LENGTH_LONG).show();
        }
    }

//calculo de distancia

    public void check100(Location location) {

        double lat1 = location.getLatitude();
        double lon1 = location.getLongitude();
        double alt1 = location.getAltitude();

        double distancia = Haversine(lon, lat, lon1, lat1);
        String a = String.valueOf(distancia);
      //  Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        if(distancia >= 100){
            add(lat1, lon1, alt1);
            updatetable();
            lat = lat1;
            lon = lon1;
            alt = alt1;
        }
    }

    private static double Haversine(double lon1, double lat1, double lon2, double lat2) {
        final double earthRadius = 6371000; // en metros
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);

        Double a = Math.sin(latDistance/2) * Math.sin(latDistance/2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance/2) * Math.sin(lonDistance/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        Double distance = earthRadius * c;

        return distance;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }


    private void updatetable(){
        ArrayList<String> listados = new ArrayList<>();
        Cursor data = miBD.getListaContenidos();
        while(data.moveToNext()){
            listados.add(" id: " + data.getString(0) +
                    " - lat: " + data.getString(1) +
                    " - lon: " + data.getString(2) +
                    " - alt: " + data.getString(3) );
            ListAdapter listAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, listados);

            listac.setAdapter(listAdapter);
        }
    }

}