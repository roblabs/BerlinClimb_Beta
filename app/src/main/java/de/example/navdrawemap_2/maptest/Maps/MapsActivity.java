package de.example.navdrawemap_2.maptest.Maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.example.navdrawemap_2.maptest.ListViewSpots.ListViewSpotsActivity;
import de.example.navdrawemap_2.maptest.OverActivity;
import de.example.navdrawemap_2.maptest.R;
import de.example.navdrawemap_2.maptest.XMLParser;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    // Map stuff
    private static final int MY_PERMISSIONS_REQUEST_GET_LOCATION = 1;
    private LatLng myposition;
    private Location myLocation;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    // Data
    private String gml = null;

    // Notes for DOM
    String KEY_FM = "gml:featureMember"; // entering Tag for XML-File
    String KEY_NAME = "ogr:NAME";
    String KEY_LAT = "ogr:LAT";
    String KEY_LONG = "ogr:LONG";
    String KEY_USE = "ogr:NUTZEN";
    String KEY_AREA = "ogr:BEZIRK";
    String KEY_INFO = "ogr:INFO";
    String KEY_KROUT = "ogr:KROUTEN";
    String KEY_BROUT = "ogr:BROUTEN";
    String KEY_INOUT = "ogr:INOUT";

    // Floating Action Buttons Menu
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton fabBoulder, fabClimb, fabBoulderandClimb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Check if we were successful in obtaining the map.

        // FA Buttons
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        materialDesignFAM.setAnimated(true);
        materialDesignFAM.setMenuButtonLabelText("Filter");

        fabClimb = (FloatingActionButton) findViewById(R.id.floating_action_menu_climb);
        fabBoulder = (FloatingActionButton) findViewById(R.id.floating_action_menu_boulder);
        fabBoulderandClimb = (FloatingActionButton) findViewById(R.id.floating_action_menu_BandC);

        fabClimb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View CoordiLayout) {
                mMap.clear();
                setMarkers(gml, true, false, false);
                // Message for the user
                Snackbar snackbar = Snackbar
                        .make(CoordiLayout, "reine Kletterspots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        fabBoulder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.clear();
                setMarkers(gml, false, true, false);
                Snackbar snackbar = Snackbar
                        .make(v, "reine Boulderspots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        fabBoulderandClimb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.clear();
                setMarkers(gml, true, true, true);
                Snackbar snackbar = Snackbar
                        .make(v, "Boulder- und Kletterspots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    // optionmenu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // method for the options menu; s and calls for other activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.item_overview:
                Intent intentoverview = new Intent(this, ListViewSpotsActivity.class);
                startActivity(intentoverview);
                break;
            case R.id.item_over:
                Intent intentover = new Intent(this, OverActivity.class);
                startActivity(intentover);
                break;
            case R.id.item_close:
                System.exit(0);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // setting up the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mUiSettings;

        // sets the maptyp, in these case Terrain
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Adapter an Listener for custom InfoWindows for the markers
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(this);

        // Move to Berlin and some Mapsettings
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(52.52001, 13.40495)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
        mMap.setOnMapLoadedCallback(this);
        mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);
        mUiSettings.setAllGesturesEnabled(true);

      /*  // MapBox Tiles, realized with a TileOverlay
        final MapBoxOnlineTileProvider provider = new MapBoxOnlineTileProvider("mapbox.streets", true);
        final TileOverlayOptions overlay = new TileOverlayOptions().tileProvider(provider);
        mMap.addTileOverlay(overlay);  */

        // codebloc for location runtime permissions. It does some If-checks and asks the user for permission
        // or not.

        int hasFineLocationPermission = 0;
        int hasCoarseLocationPermission = 0;
        List<String> permissions = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            hasCoarseLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_LOCATION);
            //  requestPermissions(permissions.toArray(new String[permissions.size()]), MY_PERMISSIONS_REQUEST_GET_LOCATION);
        }

        // two If-Checks for Runtime Permissions/ in these case fine and coarse location
        if (hasFineLocationPermission != 0) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_LOCATION);
        }
        if (hasCoarseLocationPermission != 0) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_LOCATION);
        }

        // Locationmanager which combines the GPS and the Network (WIFI and PhoneNetwork) providers
        // plus one listener which is import for the regular updates
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // default location
        myposition = new LatLng(52.52001, 13.40495);

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000,
                    0, locationListener);
            myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (myLocation != null) {
                myposition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            }
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (myLocation != null) {
                myposition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            }
        }

        // loading data and sets the markers
        String filename = "spotsberlin3";
        try {
            XMLParser parser = new XMLParser();
            gml = parser.loadFile(filename, getResources(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Calls the method that places all markers on the map
        setMarkers(gml, true, true, true);
    }

    public void onMapLoaded() {
        CameraUpdate center = CameraUpdateFactory.newLatLng(myposition);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom, 2000, null);
    }

    private LocationListener locationListener = new LocationListener() {
        public String TAG;

        @Override
        public void onLocationChanged(Location location) {
            String longitude = "Longitude: " + location.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + location.getLatitude();
            Log.v(TAG, latitude);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (myLocation !=null) {
                        myposition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    }
                } else {
                    myposition = new LatLng(52.52001, 13.40495);
                }
            }
            break;

            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // method for setting the markers on the map; requires the filter variables boulder ...
    public void setMarkers(String gml, Boolean climb, Boolean boulder, Boolean boulderandclimb) {

        XMLParser parser = new XMLParser();
        Document doc = parser.getDomElement(gml);
        NodeList nl = doc.getElementsByTagName(KEY_FM);

        // Set Markers on map
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            double LAT = Double.parseDouble(parser.getValue(e, KEY_LAT));
            double LONG = Double.parseDouble(parser.getValue(e, KEY_LONG));
            String name = parser.getValue(e, KEY_NAME);
            String use = parser.getValue(e, KEY_USE);
            String kRouten = parser.getValue(e, KEY_KROUT);
            String bRouten = parser.getValue(e, KEY_BROUT);
            String inout = parser.getValue(e, KEY_INOUT);

           /* if (high_wire && use.equals("Hochseilgarten")) {
                // Icon Colour
                BitmapDescriptor pointIcon = BitmapDescriptorFactory
                        .defaultMarker(setMarkerColor(inout));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LAT, LONG))
                        .title(name)
                        .anchor(0, 1)
                        .icon(pointIcon).snippet("\n" + "Kletterrouten: " + kRouten +
                                "\nBoulderrouten: " + bRouten + "\nInfo: " + info));

                // adds item to Clustermanager class
                MyItem item = new MyItem(LAT, LONG, name, pointIcon);
                mClusterManager.addItem(item);
            } */

            if (climb && use.equals("Klettern")) {
                // Icon Colour
                BitmapDescriptor pointIcon = BitmapDescriptorFactory
                        .defaultMarker(setMarkerColor(inout));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LAT, LONG))
                        .title(name)
                        .flat(true)
                        .anchor(0, 1)
                        .icon(pointIcon).snippet("\n" + "Kletterrouten: " + kRouten +
                                "\nKletter oder Bouldern: " + use + "\nBoulderrouten: "
                                + bRouten + "\nIn- oder Outdoor: " + inout));
            }

            if (boulder && use.equals("Bouldern")) {
                BitmapDescriptor pointIcon = BitmapDescriptorFactory
                        .defaultMarker(setMarkerColor(inout));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LAT, LONG))
                        .title(name)
                        .flat(true)
                        .anchor(0, 1)
                        .icon(pointIcon).snippet("\n" + "Kletterrouten: " + kRouten +
                                "\nKletter oder Bouldern: " + use + "\nBoulderrouten: "
                                + bRouten + "\nIn- oder Outdoor: " + inout));
            }

            if (climb && use.equals("Klettern & Bouldern") &&
                    boulder && use.equals("Klettern & Bouldern")) {
                BitmapDescriptor pointIcon = BitmapDescriptorFactory
                        .defaultMarker(setMarkerColor(inout));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LAT, LONG))
                        .title(name)
                        .flat(true)
                        .anchor(0, 1)
                        .icon(pointIcon).snippet("\n" + "Kletterrouten: " + kRouten +
                                "\nKletter oder Bouldern: " + use + "\nBoulderrouten: "
                                + bRouten + "\nIn- oder Outdoor: " + inout));
            }
            /*
            if (boulder && use.equals("Klettern&Bouldern")) {
                // Icon Colour
                BitmapDescriptor pointIcon = BitmapDescriptorFactory
                        .defaultMarker(setMarkerColor(inout));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LAT, LONG))
                        .title(name)
                        .flat(true)
                        .anchor(0, 1)
                        .icon(pointIcon).snippet("\n" + "Kletterrouten: " + kRouten +
                                "\nBoulderrouten: " + bRouten + "\nInfo: " + info));
            }*/

        }

    }

    // Setting the Icons/Color for the markers
    public float setMarkerColor(String inout) {

        switch (inout) {
            case "outdoor":
                return BitmapDescriptorFactory.HUE_GREEN;
            case "indoor":
                return BitmapDescriptorFactory.HUE_RED;
            default:
                return BitmapDescriptorFactory.HUE_BLUE;
        }
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_infowindow_map, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_infowindow_content_map, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge = 0;
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("n.v.");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            //TextView snippetUi2 = ((TextView) view.findViewById(R.id.snippet2));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("n.v.");
            }
        }
    }

    // method for infowindowbubble in map when is clicked;
    public void onInfoWindowClick(Marker marker) {
        Intent intentinfoWindow = new Intent(this, InfoWindowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", marker.getTitle());
        bundle.putString("snippet", marker.getSnippet());
        intentinfoWindow.putExtras(bundle);
        startActivity(intentinfoWindow);
    }

    // lifecycle methods
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapFragment.onSaveInstanceState(outState);

    }

    public void onPause() {
        super.onPause();
        mapFragment.onPause();
        Log.d("lifecycle", "onPause invoked");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");
    }

}