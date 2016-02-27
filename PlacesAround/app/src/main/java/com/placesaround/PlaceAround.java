package com.placesaround;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Le Huy Sinh on 2/25/2016.
 */
public class PlaceAround  implements Serializable{
    private static final long serialVersionUID = -3218192554748324182L;
    private String id;
    private String name;
    private String address;
    private LatLng latlng;
    private float rate;

    public PlaceAround(String id, String name, String address, LatLng latlng, float rate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latlng = latlng;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


}
