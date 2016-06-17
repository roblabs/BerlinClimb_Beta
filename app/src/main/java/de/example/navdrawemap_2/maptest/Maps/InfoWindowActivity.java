package de.example.navdrawemap_2.maptest.Maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.example.navdrawemap_2.maptest.OverActivity;
import de.example.navdrawemap_2.maptest.ListViewSpots.ListViewSpotsActivity;
import de.example.navdrawemap_2.maptest.R;

public class InfoWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window);
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_infowindow, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

