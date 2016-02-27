package com.placesaround;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Le Huy Sinh on 2/25/2016.
 */
public class PlaceListAdapter extends BaseAdapter {

    private Context mContext;
    private List<PlaceAround> mPlaceList;

    public PlaceListAdapter(Context mContext, List<PlaceAround> mPlaceList) {
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
        View v = View.inflate(mContext, R.layout.places_item, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        tvName.setText(mPlaceList.get(i).getName());

        v.setTag(mPlaceList.get(i).getId());
        return v;
    }
}
