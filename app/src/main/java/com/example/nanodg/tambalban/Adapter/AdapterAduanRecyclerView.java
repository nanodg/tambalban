package com.example.nanodg.tambalban.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nanodg.tambalban.ListAduanActivity;
import com.example.nanodg.tambalban.Model.Aduan;
import com.example.nanodg.tambalban.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


/**
 * Created by Hafizh Herdi on 10/8/2017.
 */

public class AdapterAduanRecyclerView extends RecyclerView.Adapter<AdapterAduanRecyclerView.ViewHolder>  {

    private ArrayList<Aduan> daftarAduan;
    private Context context;
    FirebaseDataListener listener;
    CardView kontener;

    private DatabaseReference database;

    public AdapterAduanRecyclerView(ArrayList<Aduan> aduans, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarAduan = aduans;
        context = ctx;
        listener = (ListAduanActivity)ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Inisiasi View
         * Di tutorial ini kita hanya menggunakan data String untuk tiap item
         * dan juga view nya hanyalah satu TextView
         */
        TextView pembuat,namatambal,kategori;

        ViewHolder(View v) {
            super(v);
            pembuat = (TextView) v.findViewById(R.id.tv_pembuat);
            namatambal = (TextView) v.findViewById(R.id.tv_nama);
            kategori = (TextView) v.findViewById(R.id.tv_kategori);
            kontener = (CardView) v.findViewById(R.id.kontener);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aduan, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         */
        final String pembuat = daftarAduan.get(position).getPembuat();
        final String namatambal = daftarAduan.get(position).getNamatambal();
        final String kategori = daftarAduan.get(position).getKategori();

        kontener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /**
                 *  Kodingan untuk tutorial delete dan update data
                 */
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view1);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                //apabila tombol delete diklik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                listener.onDeleteData(daftarAduan.get(position), position);
                            }
                        }
                );
                return true;
            }
        });


        holder.pembuat.setText("Pengirim : " +pembuat);
        holder.namatambal.setText("Nama Tambal Ban : " +namatambal);
        if(kategori.equals("1")){
            holder.kategori.setText("Status : Informasi Umum");
        } if(kategori.equals("2")){
            holder.kategori.setText("Status : Kritik dan Syarat");
        } if(kategori.equals("3")){
            holder.kategori.setText("Status : Lainya");
        }


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