package com.example.nanodg.tambalban.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanodg.tambalban.FirebaseDBReadAduanActivity;
import com.example.nanodg.tambalban.ListAduanPemilikActivity;
import com.example.nanodg.tambalban.Model.Aduan;
import com.example.nanodg.tambalban.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NanoDG on 23-Jun-18.
 */

public class AdapterAduanPemilikRecyclerView extends RecyclerView.Adapter<AdapterAduanPemilikRecyclerView.ViewHolder> {

    private ArrayList<Aduan> daftarAduan;
    private Context context;
    FirebaseDataListener listener;
    CardView kontener;

    public AdapterAduanPemilikRecyclerView(ArrayList<Aduan> aduans, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarAduan = aduans;
        context = ctx;
        listener = (FirebaseDBReadAduanActivity)ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvpembuat,tvnama;

        ViewHolder(View v) {
            super(v);
            tvpembuat = (TextView) v.findViewById(R.id.tv_pembuat);
            tvnama = (TextView) v.findViewById(R.id.tv_nama);
            kontener = (CardView) v.findViewById(R.id.kontener);

        }
    }

    @Override
    public AdapterAduanPemilikRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aduan_pemilik, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya

        AdapterAduanPemilikRecyclerView.ViewHolder vh = new AdapterAduanPemilikRecyclerView.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(AdapterAduanPemilikRecyclerView.ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         */
        final String pembuat = daftarAduan.get(position).getPembuat();
        final String namatambal = daftarAduan.get(position).getNamatambal();
        kontener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *  Kodingan untuk tutorial Read detail data
                 */
                //Log.e("Data snapshot","Fetched Name"+daftarTambah);
                context.startActivity(ListAduanPemilikActivity.getActIntent((Activity) context).putExtra("data", daftarAduan.get(position)));
            }
        });

        holder.tvpembuat.setText(pembuat);
        holder.tvnama.setText(namatambal);

    }
    @Override
    public int getItemCount() {
        /**
         * Mengembalikan jumlah item pada barang
         */
        return daftarAduan.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(Aduan aduan, int position);
    }
    }