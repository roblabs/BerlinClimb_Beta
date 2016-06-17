package de.example.navdrawemap_2.maptest.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.example.navdrawemap_2.maptest.R;

public class Maps_singlespot_Activity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_singlespot);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // FA Buttons
     /*   materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mUiSettings;
        Number lat= null;
        Number longC = null;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(this);

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }

        Intent intentbundleStrings = getIntent();
        if (intentbundleStrings != null) {

            // Icon Colour
            BitmapDescriptor pointIcon = BitmapDescriptorFactory
                    .defaultMarker(setMarkerColor(intentbundleStrings.getStringExtra("inout")));

            NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

            try {
                 lat = format.parse(intentbundleStrings.getStringExtra("lat"));
                 longC =  format.parse(intentbundleStrings.getStringExtra("long"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat.doubleValue(), longC.doubleValue()))
                            .title(intentbundleStrings.getStringExtra("heads"))
                            .flat(true)
                            .anchor(0, 1) // Noch unrelevant
                            .icon(pointIcon).snippet("\n" + "Kletterrouten: " + intentbundleStrings.getStringExtra("title") +
                                    "\nBoulderrouten: " + intentbundleStrings.getStringExtra("title")
                                    + "\nInfo: " + intentbundleStrings.getStringExtra("desc")));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat.doubleValue(), longC.doubleValue())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    private float setMarkerColor(String inout) {
        switch (inout) {
            case "outdoor":
                return BitmapDescriptorFactory.HUE_GREEN;
            case "indoor":
                return BitmapDescriptorFactory.HUE_ORANGE;
            default:
                return BitmapDescriptorFactory.HUE_RED;
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
                titleUi.setText("nicht vorhanden");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            //TextView snippetUi2 = ((TextView) view.findViewById(R.id.snippet2));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, snippetText.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("nicht vorhanden");
            }
        }
    }


    public void onInfoWindowClick(Marker marker) {
        Intent intentinfoWindow = new Intent(this, InfoWindowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", marker.getTitle());
        bundle.putString("snippet", marker.getSnippet());
        intentinfoWindow.putExtras(bundle);
        startActivity(intentinfoWindow);
    }
}
