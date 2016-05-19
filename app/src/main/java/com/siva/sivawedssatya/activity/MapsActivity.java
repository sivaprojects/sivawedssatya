package com.siva.sivawedssatya.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.WeddingApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements LocationListener {
    //	LatLng startPoint = new LatLng(17.385, 78.4867);
//	LatLng endPoint = new LatLng(20.0, 77.0);
    private static LatLng CURRENTLOCATION = new LatLng(17.385, 78.4867);
    private static final LatLng FUCTIONHALL = new LatLng(16.945181, 82.238647);
    private static final LatLng SIVAHOUSE = new LatLng(16.945181, 82.238647);
    private GoogleMap map;
    private SupportMapFragment fragment;
    private LatLngBounds latlngBounds;
    private Button bNavigation;
    private Polyline newPolyline;
    private boolean isTravelingToSivaHouse = false;
    private int width, height;
    private Toolbar mToolbar;
    private CoordinatorLayout coordinatorLayout;
    //    GoogleMap googleMap;
    boolean isFromContacts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_navigation);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(mToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                    .coordinatorLayout);
            getSreenDimanstions();
            fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            map = fragment.getMap();

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
//        }

            isFromContacts = getIntent().getBooleanExtra("ISCONTACTS", false);
            if (isFromContacts) {
                getSupportActionBar().setTitle("Navigate to Siva's Home");
                if (!isNetworkConnected()) {
                    return;
                }

                findDirections(CURRENTLOCATION.latitude, CURRENTLOCATION.longitude, SIVAHOUSE.latitude, SIVAHOUSE.longitude, GMapV2Direction.MODE_DRIVING);

            } else {
                getSupportActionBar().setTitle("Navigate to Wedding Hall");
                if (!isNetworkConnected()) {
                    return;
                }
                findDirections(CURRENTLOCATION.latitude, CURRENTLOCATION.longitude, FUCTIONHALL.latitude, FUCTIONHALL.longitude, GMapV2Direction.MODE_DRIVING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            CURRENTLOCATION = new LatLng(latitude, longitude);
            if (isFromContacts) {
                if (!isNetworkConnected()) {
                    return;
                }
                findDirections(CURRENTLOCATION.latitude, CURRENTLOCATION.longitude, SIVAHOUSE.latitude, SIVAHOUSE.longitude, GMapV2Direction.MODE_DRIVING);
            } else {
                if (!isNetworkConnected()) {
                    return;
                }
                findDirections(CURRENTLOCATION.latitude, CURRENTLOCATION.longitude, FUCTIONHALL.latitude, FUCTIONHALL.longitude, GMapV2Direction.MODE_DRIVING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == android.R.id.home) {
                setResult(101);
                finish();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        try {
            super.onResume();
            latlngBounds = createLatLngBoundsObject(CURRENTLOCATION, FUCTIONHALL);
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        try {
            PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

            for (int i = 0; i < directionPoints.size(); i++) {
                rectLine.add(directionPoints.get(i));
            }
            if (newPolyline != null) {
                newPolyline.remove();
            }
            newPolyline = map.addPolyline(rectLine);
            if (isTravelingToSivaHouse) {
                latlngBounds = createLatLngBoundsObject(CURRENTLOCATION, FUCTIONHALL);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
            } else {
                latlngBounds = createLatLngBoundsObject(CURRENTLOCATION, SIVAHOUSE);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getSreenDimanstions() {
        try {
            Display display = getWindowManager().getDefaultDisplay();
            width = display.getWidth();
            height = display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation) {
        try {
            if (firstLocation != null && secondLocation != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(firstLocation).include(secondLocation);

                return builder.build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
            map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
            map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
            map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
            map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

            GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
            asyncTask.execute(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkConnected() {
        try {
            if (!WeddingApplication.isNetworkConnected()) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);

                snackbar.show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}