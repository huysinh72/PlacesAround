package com.placesaround;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private GoogleApiClient mGoogleApiClient;
    Button btCurrentLocation;
    CountDownTimer updateTimer;
    private ListView lvPlaces;
    private PlaceListAdapter adapter;
    private List<PlaceAround> mPlaceList;
    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOK()) {
            setContentView(R.layout.activity_places_map);
            if(initMap()) {

                mMap.setMyLocationEnabled(true);
                mGoogleApiClient =  new GoogleApiClient
                        .Builder(this)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .enableAutoManage(this, this)
                        .build();
                mGoogleApiClient.connect();


            }
        }else{
            setContentView(R.layout.activity_main);
        }

        btCurrentLocation = (Button) findViewById(R.id.btCurrentLocation);
        btCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gotoMyLocation();

                mPlaceList.clear();
                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);

                    result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                        @Override
                        public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                            if (placeLikelihoods.getCount() == 0)
                                Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
                            else {
                                for (PlaceLikelihood placeLikelihood : placeLikelihoods) {

                                    String name = placeLikelihood.getPlace().getName().toString();
                                    String id = placeLikelihood.getPlace().getId();
                                    String address = placeLikelihood.getPlace().getAddress().toString();
                                    LatLng latLng = placeLikelihood.getPlace().getLatLng();
                                    float rate = placeLikelihood.getPlace().getRating();

                                    mPlaceList.add(new PlaceAround(id, name, address, latLng, rate));
                                }
                                placeLikelihoods.release();

                                adapter = new PlaceListAdapter(getApplicationContext(), mPlaceList);
                                lvPlaces.setAdapter(adapter);
                            }
                        }
                    });
                }
        });

        lvPlaces = (ListView) findViewById(R.id.lvPlaces);
        mPlaceList =  new ArrayList<>();

        updateTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

                gotoMyLocation();

                mPlaceList.clear();

                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);

                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {

                        if (placeLikelihoods.getCount() == 0)
                            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
                        else {
                            for (PlaceLikelihood placeLikelihood : placeLikelihoods) {

                                String name = placeLikelihood.getPlace().getName().toString();
                                String id = placeLikelihood.getPlace().getId();
                                String address = placeLikelihood.getPlace().getAddress().toString();
                                LatLng latLng = placeLikelihood.getPlace().getLatLng();

                                Location location = new Location("location");
                                location.setLatitude(latLng.latitude);
                                location.setLongitude(latLng.longitude);

                                float rate = currentLocation.distanceTo(location) / 1000;

                                mPlaceList.add(new PlaceAround(id, name, address, latLng, rate));
                            }
                            placeLikelihoods.release();

                            adapter = new PlaceListAdapter(getApplicationContext(), mPlaceList);
                            lvPlaces.setAdapter(adapter);
                        }
                    }
                });

            updateTimer.cancel();
        }
        };

        updateTimer.start();

        lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PlaceInfo.class);
                PlaceAround place = mPlaceList.get(i);
                intent.putExtra("name", place.getName());
                intent.putExtra("address", place.getAddress());
                intent.putExtra("id", place.getId());
                intent.putExtra("rate", place.getRate());

                startActivity(intent);
            }
        });
    }

    public boolean servicesOK(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean initMap(){
        if(mMap == null){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            mMap.setMyLocationEnabled(true);
        }
        return (mMap!=null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connect failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Ready to map", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void gotoMyLocation(){
        currentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if(currentLocation == null){
            Toast.makeText(this, "Map not connect", Toast.LENGTH_SHORT ).show();
            //finish();
        }else{
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(update);
        }
    }

}
