package de.example.navdrawemap_2.maptest.ListViewSpots;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import de.example.navdrawemap_2.maptest.Maps.Maps_singlespot_Activity;
import de.example.navdrawemap_2.maptest.R;
import de.example.navdrawemap_2.maptest.XMLParser;

public class ListViewSpotsActivity extends AppCompatActivity {

    // XML-nodes
    private static final String KEY_LAT = "ogr:LAT";
    private static final String KEY_LONG = "ogr:LONG";
    private static final String KEY_FM = "gml:featureMember";
    private static final String KEY_HEAD = "ogr:NAME";
    private static final String KEY_DISTRICT = "ogr:BEZIRK";
    private static final String KEY_STREET = "ogr:STRASSE";
    private static final String KEY_POSTALCODE = "ogr:PLZ";
    private static final String KEY_HOUSENR = "ogr:HAUSNR";
    private static final String KEY_TYPE = "ogr:NUTZEN";
    //    private static final String KEY_INFO = "ogr:INFO";
    private static final String KEY_INOUT = "ogr:INOUT";
    private static final String KEY_KROUTEN = "ogr:KROUTEN";
    private static final String KEY_BROUTEN = "ogr:BROUTEN";
    private static final String KEY_MATERIAL = "ogr:MATERIAL";
    private static final String KEY_PRICE = "ogr:PREIS";
    private static final String KEY_WEBADRESS = "ogr:HOMEPAGE";
    private static final String KEY_IMAGEID = "ogr:IMAID";

    // Arrays and List for Datahandaling
    private int[] imageid;
    private double[] lat, longC;
    private String[] head, type, inout, krouten, brouten, adress, material, webadress, price;
    private CustomList customList;
    private ListView listView;

    // Floating Action Buttons Menu
    FloatingActionMenu FAM;
    FloatingActionButton fabBoulder, fabClimb, fabBoulderandClimb, fabAll;

    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overwiev);
        resources = getResources();
        FAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);

        fabClimb = (FloatingActionButton) findViewById(R.id.floating_action_menu_climb);
        fabBoulder = (FloatingActionButton) findViewById(R.id.floating_action_menu_boulder);
        fabBoulderandClimb = (FloatingActionButton) findViewById(R.id.floating_action_menu_BandC);
        fabAll = (FloatingActionButton) findViewById(R.id.floating_action_menu_all);

        fabClimb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSpots(true, false, false, false);
                // Message for the user
                Snackbar snackbar = Snackbar
                        .make(v, "Kletterspots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        fabBoulder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSpots(false, true, false, false);
                Snackbar snackbar = Snackbar
                        .make(v, "Boulderspots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        fabBoulderandClimb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSpots(false, false, true, false);
                Snackbar snackbar = Snackbar
                        .make(v, "Boulder und Kletterspots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        fabAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSpots(true, true, true, false);
                Snackbar snackbar = Snackbar
                        .make(v, "Alle Spots", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        handleSpots(true, true, true, false);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overwiev, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit:
                System.exit(0);
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void handleSpots(boolean filterclimb, boolean filterboulder,
                            boolean filterboulderandclimb, boolean filterhigh_wire) {

        // file input from raw folder
        String gml = null;
        try {
            XMLParser parser = new XMLParser();
            String filename = "spotsberlin3";
            gml = parser.loadFile(filename, resources, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XMLParser parser = new XMLParser();
        Document doc = parser.getDomElement(gml);
        NodeList nl = doc.getElementsByTagName(KEY_FM);

        // array initialising for data handling
        head = new String[nl.getLength()];
        imageid = new int[nl.getLength()];
        type = new String[nl.getLength()];
        inout = new String[nl.getLength()];
        krouten = new String[nl.getLength()];
        brouten = new String[nl.getLength()];
        price = new String[nl.getLength()];
        material = new String[nl.getLength()];
        adress = new String[nl.getLength()];
        lat = new double[nl.getLength()];
        longC = new double[nl.getLength()];
        webadress = new String[nl.getLength()];


        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);

            // filter checks for all tree filter variabels
            // the high wire data is always skipe put holded in the data
            if (!filterhigh_wire && parser.getValue(e, KEY_TYPE).equals("Hochseilgarten"))
                continue;
            if (!filterclimb && parser.getValue(e, KEY_TYPE).equals("Klettern"))
                continue;
            if (!filterboulder && parser.getValue(e, KEY_TYPE).equals("Bouldern"))
                continue;
            if (!filterboulderandclimb && parser.getValue(e, KEY_TYPE).equals("Klettern & Bouldern"))
                continue;

            head[i] = parser.getValue(e, KEY_HEAD);
            imageid[i] = getResources().getIdentifier(parser.getValue(e, KEY_IMAGEID),
                    "drawable", getPackageName());

            if (parser.getValue(e, KEY_KROUTEN).isEmpty()) {
                krouten[i] = "Anzahl der Kletterrouten: n.v.";
            } else {
                krouten[i] = "Anzahl der Kletterrouten: " + parser.getValue(e, KEY_KROUTEN);
            }

            if (parser.getValue(e, KEY_BROUTEN).isEmpty()) {
                brouten[i] = "Anzahl der Boulderrouten: n.v.";
            } else {
                brouten[i] = "Anzahl der Boulderrouten: " + parser.getValue(e, KEY_BROUTEN);
            }

            type[i] = parser.getValue(e, KEY_TYPE);
            inout[i] = "In- oder Outdoor: " + parser.getValue(e, KEY_INOUT);
            material[i] = "Material: " + parser.getValue(e, KEY_MATERIAL);
            price[i] = "Preis: " + parser.getValue(e, KEY_PRICE);

            adress[i] = "Adresse: " + parser.getValue(e, KEY_STREET) + " " + parser.getValue(e, KEY_HOUSENR) + ", "
                    + parser.getValue(e, KEY_POSTALCODE) + " " + parser.getValue(e, KEY_DISTRICT);

            lat[i] = Double.parseDouble(parser.getValue(e, KEY_LAT));
            longC[i] = Double.parseDouble(parser.getValue(e, KEY_LONG));

            if (parser.getValue(e, KEY_WEBADRESS).isEmpty()) {
                webadress[i] = "n.v.";
            } else {
                webadress[i] = parser.getValue(e, KEY_WEBADRESS);
            }

        }

        Arrays.asList(head).removeAll(Collections.singleton(""));
        remov_empty_elements(imageid);
        Arrays.asList(type).removeAll(Collections.singleton(""));
        Arrays.asList(inout).removeAll(Collections.singleton(""));
        Arrays.asList(krouten).removeAll(Collections.singleton(""));
        Arrays.asList(brouten).removeAll(Collections.singleton(""));
        Arrays.asList(material).removeAll(Collections.singleton(""));
        Arrays.asList(price).removeAll(Collections.singleton(""));
        Arrays.asList(adress).removeAll(Collections.singleton(""));
  //      Arrays.asList(Arrays.toString(lat)).removeAll(Collections.singleton(""));
        Arrays.asList(Arrays.toString(longC)).removeAll(Collections.singleton(""));
 //       Arrays.asList(webadress).removeAll(Collections.singleton(""));

        customList = new CustomList(this, head, imageid, type, inout, krouten, brouten,
                material, price, adress, lat, longC, webadress);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);

        // Click handle if a list item will be clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View selectedItem = customList.getViewByPosition(i, listView);

                ImageView imageview = (ImageView) selectedItem.findViewById(R.id.imageView);
                //        ImageView imageid = imageview.findViewById(R.id.imageView1);

                TextView textViewheads = (TextView) selectedItem.findViewById(R.id.textViewHeads);
                String tvheads = textViewheads.getText().toString();

                TextView texttype = (TextView) selectedItem.findViewById(R.id.textViewType);
                String tvtype = texttype.getText().toString();

                TextView textViewinout = (TextView) selectedItem.findViewById(R.id.textViewInOUT);
                String tvinout = textViewinout.getText().toString();

                TextView textViewlat = (TextView) selectedItem.findViewById(R.id.textViewLat);
                String tvlat = textViewlat.getText().toString();

                TextView textViewlong = (TextView) selectedItem.findViewById(R.id.textViewLong);
                String tvlong = textViewlong.getText().toString();

                Intent intentMaps2Activitiy = new Intent(getApplicationContext(),
                        Maps_singlespot_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("heads", tvheads);
                bundle.putString("type", tvtype);
                bundle.putString("inout", tvinout);
                bundle.putString("lat", tvlat);
                bundle.putString("long", tvlong);
                intentMaps2Activitiy.putExtras(bundle);
                startActivity(intentMaps2Activitiy);

            }
        });
    }

    public int []  remov_empty_elements(int [] arrayInput){
        int[] arrayOutput = new int[arrayInput.length];
        for (int i = 0; i < arrayInput.length; i++){
            if(arrayInput [i] != 0){
                arrayOutput[i] = arrayInput[i];
            }
        }
        return arrayOutput;
    }


    public void callWebAdress(View v) {

        TextView textviewwebadress = (TextView) v.findViewById(R.id.textViewWebadress);
        String url = textviewwebadress.getText().toString();

        if (!url.equals("n.v.")) {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);

        }
    }

    // lifecycle methods
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listView.onSaveInstanceState();
    }

    public void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause invoked");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");
    }
}