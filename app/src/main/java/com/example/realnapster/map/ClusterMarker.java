package com.example.realnapster.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.Items;

/**
 * Created by Realnapster on 3/11/2018.
 */

public class ClusterMarker implements ClusterManager.OnClusterClickListener<Items>,ClusterManager.OnClusterInfoWindowClickListener<Items>, ClusterManager.OnClusterItemClickListener<Items>, ClusterManager.OnClusterItemInfoWindowClickListener<Items> {
    private ClusterManager<Items> mClusterManager;
    private Context mContext;
    GoogleMap mMap;
    CustomInfoWindowAdapter infoWindowAdapter;

    public ClusterMarker(Context mContext,GoogleMap mMap)
    {this.mMap=mMap;
    this.mContext=mContext;
    }


    @Override
    public boolean onClusterClick(Cluster<Items> cluster) {

        Toast.makeText(mContext, String.valueOf(cluster.getSize()), Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

      // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
//        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Items> cluster) {
    }


    @Override
    public boolean onClusterItemClick(Items items) {
        return false;
    }


    @Override
    public void onClusterItemInfoWindowClick(Items items) {

    }


//***********************************************************************************************

    private class ItemsRenderer extends DefaultClusterRenderer<Items> {
        private final IconGenerator mIconGenerator = new IconGenerator(mContext);
        private final IconGenerator mClusterIconGenerator = new IconGenerator(mContext);
        private final ImageView mImageView;

        public ItemsRenderer() {
            super(mContext, mMap, mClusterManager);

            // Setting Layout for Each Cluster Marker
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mClusterIconGenerator.setStyle(IconGenerator.STYLE_BLUE);

            View marker = inflater.inflate(R.layout.marker, null);
            mImageView = marker.findViewById(R.id.imageView);
            mIconGenerator.setContentView(marker);
        }

        @Override
        protected void onBeforeClusterItemRendered(Items item, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(item.getPhotoRes());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle())
            .position(item.getPosition());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Items> cluster, MarkerOptions markerOptions) {

            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            Log.e("Cluster Size",String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }
//************************************************************************************************
// Setting up Custom Info Window
private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView title;
        TextView latLng;
        TextView snippet;
        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.infowindow,null);
        title = view.findViewById(R.id.title);

        latLng = view.findViewById(R.id.latLng);
        snippet = view.findViewById(R.id.snippet);

        title.setText(marker.getTitle());
        Log.e("Title",marker.getTitle());
        latLng.setText(marker.getPosition().toString());
        Log.e("Pos",marker.getPosition().toString());
       snippet.setText(marker.getSnippet());
        Log.e("Snippet",marker.getSnippet());
       return  view;
    }
}



// *** Setup Methods ****************


    public void setupCluster(){
        infoWindowAdapter = new CustomInfoWindowAdapter();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.0094477, -76.9658255), 10));
        mClusterManager = new ClusterManager<Items>(mContext,mMap);
        mClusterManager.setRenderer(new ItemsRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(infoWindowAdapter);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        addItems();
        mClusterManager.cluster();

    }

    private void addItems(){
        List<Address> addresses = new ArrayList<>();
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        // Set some lat/lng coordinates to start with.
        double lat = 39.0094477;
        double lng = -76.9658255;
        String mTitle;
        String mSnippet;
        // Add twenty cluster items in close proximity for Example purpose
        for (int i = 0; i < 20; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            try {
                addresses = geocoder.getFromLocation(lat,lng,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            mTitle = " Title " + i;
            mSnippet= address + " , " + city + " , " + state ;
            Items offsetItem = new Items(lat, lng,mTitle,mSnippet,R.mipmap.atm);
            mClusterManager.addItem(offsetItem);
            Log.e("Item Log","Item Added");
        }

    }
}
