package com.example.nanodg.tambalban.Adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nanodg.tambalban.FirebaseDBReadActivity;
import com.example.nanodg.tambalban.FirebaseDBReadSingleActivity;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.R;
import com.example.nanodg.tambalban.TambahActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Hafizh Herdi on 10/8/2017.
 */

public class AdapterBarangRecyclerView extends RecyclerView.Adapter<AdapterBarangRecyclerView.ViewHolder>  {

    private ArrayList<Tambah> daftarTambah;
    private Context context;
    FirebaseDataListener listener;
    CardView kontener;

    private DatabaseReference database;

    public AdapterBarangRecyclerView(ArrayList<Tambah> tambahs, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarTambah = tambahs;
        context = ctx;
        listener = (FirebaseDBReadActivity)ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Inisiasi View
         * Di tutorial ini kita hanya menggunakan data String untuk tiap item
         * dan juga view nya hanyalah satu TextView
         */
        TextView tvTitle,tvalamat;

        ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_namabarang);
            tvalamat = (TextView) v.findViewById(R.id.tv_alamat);
            kontener = (CardView) v.findViewById(R.id.kontener);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         */
        final String name = daftarTambah.get(position).getNama();
        final String alamat = daftarTambah.get(position).getAlamat();
        final String hslstatus = daftarTambah.get(position).getStatus();
        //System.out.println("BARANG DATA one by one "+position+daftarTambah.size());
//        Log.e("Data snapshot","barang1"+position+daftarTambah.size());
//        Log.e("Data snapshot","barang1"+name);
        kontener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *  Kodingan untuk tutorial Read detail data
                 */
                //Log.e("Data snapshot","Fetched Name"+daftarTambah);
                context.startActivity(FirebaseDBReadSingleActivity.getActIntent((Activity) context).putExtra("data", daftarTambah.get(position)));
            }
        });
        kontener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /**
                 *  Kodingan untuk tutorial delete dan update data
                 */
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(R.id.bt_edit_data);
                Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                //apabila tombol edit diklik
                editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                context.startActivity(TambahActivity.getActIntent((Activity) context).putExtra("data", daftarTambah.get(position)));
                            }
                        }
                );

                //apabila tombol delete diklik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                listener.onDeleteData(daftarTambah.get(position), position);
                            }
                        }
                );
                return true;
            }
        });


        holder.tvTitle.setText(name);
        holder.tvalamat.setText(alamat);

    }

    @Override
    public int getItemCount() {
        /**
         * Mengembalikan jumlah item pada barang
         */
        return daftarTambah.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(Tambah tambah, int position);
    }
}