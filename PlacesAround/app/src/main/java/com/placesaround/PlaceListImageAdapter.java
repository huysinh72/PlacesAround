package com.placesaround;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Le Huy Sinh on 2/27/2016.
 */
public class PlaceListImageAdapter  extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> mPlaceList;

    public PlaceListImageAdapter(Context mContext, List<Bitmap> mPlaceList) {
        this.mPlaceList = mPlaceList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mPlaceList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPlaceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.place_image_item, null);
        ImageView imgPlace = (ImageView) v.findViewById(R.id.imgPlace);
        imgPlace.setImageBitmap(mPlaceList.get(i));
        return v;
    }
}
