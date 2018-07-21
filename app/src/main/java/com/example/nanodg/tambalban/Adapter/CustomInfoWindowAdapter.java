package com.example.nanodg.tambalban.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by NanoDG on 21-Jul-18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
   private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_infowindow,null);
    }

    private void rendoWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvtitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            tvtitle.setText(title);
        }
        String snnipet = marker.getSnippet();
        TextView tvsnnipet = (TextView) view.findViewById(R.id.snnipet);
        if(!snnipet.equals("")){
            tvsnnipet.setText(snnipet);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendoWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendoWindowText(marker, mWindow);
        return mWindow;
    }
}
