package com.example.realnapster.map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import model.Items;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<Items> mClusterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setFloatButton();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setFloatButton(){
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.mipmap.ic_settings);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon).build();

        // For Sub Menu Floating Buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        // Setting Image Icon Buttons for Sub Action Menu Items
        ImageView subIconA = new ImageView(this);
        ImageView subIconB = new ImageView(this);
        subIconA.setImageResource(R.mipmap.ic_google);
        subIconB.setImageResource(R.mipmap.ic_list);
        SubActionButton subIconAbtn = itemBuilder.setContentView(subIconA).build();
        SubActionButton subIconBbtn = itemBuilder.setContentView(subIconB).build();

        // Create the menu with the above items

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subIconAbtn)
                .addSubActionView(subIconBbtn)
                .attachTo(actionButton)
                .build();



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));
        setupCluster();
    }


    private void setupCluster(){
        mClusterManager = new ClusterManager<>(this,mMap);
        Log.e("Hello","HelloWorld");
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        addItems();
        mClusterManager.cluster();
    }

    private void addItems(){
        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;
        String mTitle;
        String mSnippet;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            mTitle = " latitude : longitude " + lat +" : " + lng;
            mSnippet= "Hello I'm Snippet " +i ;
            Items offsetItem = new Items(lat, lng,mTitle,mSnippet);
            mClusterManager.addItem(offsetItem);
            Log.e("Item Log","Item Added");
        }

    }
}

