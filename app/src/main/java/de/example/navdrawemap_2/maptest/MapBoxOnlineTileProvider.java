package de.example.navdrawemap_2.maptest;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hagen on 10.06.2016.
 */

public class MapBoxOnlineTileProvider extends UrlTileProvider {

    private static final String FORMAT;

    static {
        FORMAT = "%s://api.tiles.mapbox.com/v4/%s/3/2/3.0@2x.png?/access_token=pk.eyJ1Ijoidm9uc2VlbGVuIiwiYSI6ImNpbTR0d3VsZzAwMXB3Y2tsMzljaGR6dmEifQ.WpMtbLMfW0f-1DVbWgUuDA";
    }

    // ------------------------------------------------------------------------
    // Instance Variables
    // ------------------------------------------------------------------------

    private boolean mHttpsEnabled;
    private String mMapIdentifier;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    public MapBoxOnlineTileProvider(String mapIdentifier) {
        this(mapIdentifier, false);
    }

    public MapBoxOnlineTileProvider(String mapIdentifier, boolean https) {
        super(512, 512);

        this.mHttpsEnabled = https;
        this.mMapIdentifier = mapIdentifier;
    }

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    /**
     * The MapBox map identifier being used by this provider.
     *
     * @return the MapBox map identifier being used by this provider.
     */
    public String getMapIdentifier() {
        return this.mMapIdentifier;
    }

    /**
     * Sets the identifier of the MapBox hosted map you wish to use.
     *
     * @param aMapIdentifier the identifier of the map.
     */
    public void setMapIdentifier(String aMapIdentifier) {
        this.mMapIdentifier = aMapIdentifier;
    }

    /**
     * Whether this provider will use HTTPS when requesting tiles.
     *
     * @return {@link true} if HTTPS is enabled on this provider.
     */
    public boolean isHttpsEnabled() {
        return this.mHttpsEnabled;
    }

    /**
     * Sets whether this provider should use HTTPS when requesting tiles.
     *
     * @param enabled
     */
    public void setHttpsEnabled(boolean enabled) {
        this.mHttpsEnabled = enabled;
    }

    @Override
    public URL getTileUrl(int x, int y, int z) {
        try {
            String protocol = this.mHttpsEnabled ? "https" : "http";
            return new URL(String.format(FORMAT, protocol, this.mMapIdentifier, z, x, y));
        }
        catch (MalformedURLException e) {
            return null;
        }
    }

}