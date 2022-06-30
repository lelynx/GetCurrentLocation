package com.example.getcurrentlocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * https://www.vogella.com/tutorials/AndroidLocationAPI/article.html
 * Pour les permissions: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
 */
public class ShowLocationActivity
        extends AppCompatActivity
        implements LocationListener {

    private static final String TAG = "ShowLocationActivity";

    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    //
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        // provider of String type--Returns the name of the provider that best meets the given criteria.
        // Vaeur retournée: gps
        provider = locationManager.getBestProvider(criteria, false);
        Log.d(TAG, "onCreate: (provider == null): " + (provider == null));
        Log.d(TAG, "onCreate: criteria: " + criteria.toString());
        Log.d(TAG, "onCreate: provider: " + provider);
        // NB: le demande de permission est ajoutée automatiquement par le compiler, qui, sinon, affiche une erreur
        // Pour demander au compiler de générer le code automatiquement, commenter l'appel de checkPermission
        checkPermission(permissions[0], PERMISSIONS_REQUEST_CODE);
        checkPermission(permissions[1], PERMISSIONS_REQUEST_CODE);
        // Enfin: On obtient la localisation!!
        // on passe le best provider en argument
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d(TAG, "onCreate: (location == null): " + (location == null));
        // Initialize the location fields
        if (location != null) {
            Log.d(TAG, "onCreate: provider " + provider + " has been selected");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }
//        Geocoder geocoder = new Geocoder(this, );
    }// on Create()

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: enter");

        // NB: la demande de permission est ajoutée automatiquement par le compiler, qui, sinon, affiche une erreur
        // sur requestLocationUpdates
        checkPermission(permissions[0], PERMISSIONS_REQUEST_CODE);
        checkPermission(permissions[1], PERMISSIONS_REQUEST_CODE);

        Log.d(TAG, "onResume: locationManager.requestLocationUpdates");
        Log.d(TAG, "onResume: (locationManager == null): " + (locationManager == null));
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove the location manager: pour faire en sorte que l'application ne
        // recherche la position que quand elle est visible!!
        // pour économiser les ressources!
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // si on change la position sur l'émulateur, la méthode est appelée,
        // et les coordonnées sont mises à jour!!
        Log.d(TAG, "onLocationChanged: latitude: " + location.getLatitude() + "; longitiude: " + location.getLongitude());
        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    // Function to check and request permission
    // voir: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }


    // This function is called when user accept or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(this, "Access Fine Location Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Access Fine Location Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }//onRequestPermissionsResult


}