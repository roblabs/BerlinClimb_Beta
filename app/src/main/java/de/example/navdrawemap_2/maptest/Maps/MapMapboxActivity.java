package de.example.navdrawemap_2.maptest.Maps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import de.example.navdrawemap_2.maptest.R;

public class MapMapboxActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_mapbox);

        mapView = (MapView) findViewById(R.id.mapviewmapbox);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                // add markers, change camera position, etc. here!

            }});
    }

          /*
        TextView textViewTitle = (TextView)findViewById(R.id.texttitel);
        TextView textViewTitleBig = (TextView)findViewById(R.id.texttitel_big);
        TextView textViewSnippet = (TextView)findViewById(R.id.textsnippet);
        Intent intentbundleStrings = getIntent();

        if (intentbundleStrings != null) {
            textViewTitleBig.setText(intentbundleStrings.getStringExtra("title"));
            // textViewTitle.setText(intentbundleStrings.getStringExtra("title"));
            // Titel im Header nachtragen
            textViewSnippet.setText(intentbundleStrings.getStringExtra("snippet"));
        }else{
            textViewTitle.setText(intentbundleStrings.getStringExtra("N.A."));
            textViewSnippet.setText(intentbundleStrings.getStringExtra("N.A."));
        } */




    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}