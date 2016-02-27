package com.placesaround;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Huy Sinh on 2/26/2016.
 */
public class PlaceInfo extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;

    TextView tvName;
    TextView tvAddress;
    TextView tvRate;
    ImageView imgPlace;
    ListView lvImage;
    private List<Bitmap> mlistImage;
    private PlaceListImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        Bundle bundle = getIntent().getExtras();

        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvRate = (TextView) findViewById(R.id.tvRate);
        imgPlace = (ImageView) findViewById(R.id.imagePlace);
        lvImage = (ListView) findViewById(R.id.lvImage);
        mlistImage = new ArrayList<>();

        tvName.setText(bundle.getString("name"));
        tvAddress.setText("Address: "+bundle.getString("address"));
        tvRate.setText("Distance: " + bundle.getFloat("rate") + " kilometers");

        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, bundle.getString("id")).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(final PlacePhotoMetadataResult placePhotoMetadataResult) {
                if (placePhotoMetadataResult.getStatus().isSuccess()) {
                    final PlacePhotoMetadataBuffer photoMetadata = placePhotoMetadataResult.getPhotoMetadata();
                    if (photoMetadata.getCount() > 0) {
                        for(int i = 0; i < photoMetadata.getCount(); i++) {
                            PlacePhotoMetadata placePhotoMetadata = photoMetadata.get(i);
                            placePhotoMetadata.getScaledPhoto(mGoogleApiClient, 500, 500).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                                @Override
                                public void onResult(PlacePhotoResult placePhotoResult) {
                                    if (placePhotoResult.getStatus().isSuccess()) {
                                        Bitmap bitmap = Bitmap.createBitmap(placePhotoResult.getBitmap());
                                        imgPlace.setImageBitmap(bitmap);
                                        mlistImage.add(bitmap);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Not image", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        adapter = new PlaceListImageAdapter(getApplicationContext(), mlistImage);
                        lvImage.setAdapter(adapter);

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not image", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    photoMetadata.release();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
