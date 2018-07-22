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
import android.widget.TextView;

import com.example.nanodg.tambalban.CariTambalActivity;
import com.example.nanodg.tambalban.DtltambalActivity;
import com.example.nanodg.tambalban.FirebaseDBReadActivity;
import com.example.nanodg.tambalban.FirebaseDBReadSingleActivity;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.R;
import com.example.nanodg.tambalban.TambahActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class AdapterCariTambalRecyclerView extends RecyclerView.Adapter<AdapterCariTambalRecyclerView.ViewHolder>  {

    private ArrayList<Tambah> daftarTambah;
    private Context context;
    FirebaseDataListener listener;
    CardView kontener;
    public static final String DATA = "com.example.nanodg.tambalban";
    private DatabaseReference database;

    public AdapterCariTambalRecyclerView(ArrayList<Tambah> tambahs, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarTambah = tambahs;
        context = ctx;
        listener = (CariTambalActivity)ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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

                //Log.e("Data snapshot","Fetched Name"+daftarTambah);
                context.startActivity(DtltambalActivity.getActIntent((Activity) context).putExtra(DATA, holder.tvTitle.getText().toString()));
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