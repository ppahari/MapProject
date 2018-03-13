package com.example.realnapster.map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.Items;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ClusterMarker clusterMarker;
    public GoogleMap mMap;
    @BindView(R.id.input_search)
    EditText input_search;

    @BindView(R.id.ic_search)
    ImageView ic_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    private void init(){
        input_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled=false;
                if (TextUtils.isEmpty(input_search.getText().toString())){
                    Toast.makeText(MapsActivity.this,"Not Entered", Toast.LENGTH_SHORT).show();
            }
            else{
                    if(actionId==EditorInfo.IME_ACTION_SEARCH){
                        placesSearch();
                        handled=true;
                    }
                }
                return  handled;
            }
        });

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

    public void placesSearch(){
        Log.e("Search","I am inside Place Search");
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.0094477, -76.9658255), 10));

        clusterMarker = new ClusterMarker(this,mMap);
        clusterMarker.setupCluster();
    }


}

