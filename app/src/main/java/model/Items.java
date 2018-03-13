package model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.realm.RealmObject;

/**
 * Created by Realnapster on 3/9/2018.
 */

public class Items extends RealmObject implements ClusterItem {
    private final  LatLng mPosition;
    private  final String mTitle;
    private final String mSnippet;
    public final int photoRes;

    public Items(double Lat,double Lang,String mTitle, String mSnippet,int photoRes) {
        mPosition = new LatLng(Lat,Lang);
        this.mTitle= mTitle;
        this.mSnippet = mSnippet;
        this.photoRes=photoRes;

    }

    public int getPhotoRes() {
        return photoRes;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
