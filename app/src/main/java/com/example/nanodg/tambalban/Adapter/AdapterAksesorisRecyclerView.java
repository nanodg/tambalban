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

import com.example.nanodg.tambalban.FirebaseDBReadAksesorisActivity;
import com.example.nanodg.tambalban.FirebaseDBReadBengkelActivity;
import com.example.nanodg.tambalban.FirebaseDBReadSingleAksesorisActivity;
import com.example.nanodg.tambalban.FirebaseDBReadSingleBengkelActivity;
import com.example.nanodg.tambalban.Model.Aksesoris;
import com.example.nanodg.tambalban.Model.Bengkel;
import com.example.nanodg.tambalban.R;
import com.example.nanodg.tambalban.TambahAksesorisActivity;
import com.example.nanodg.tambalban.TambahBengkelActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class AdapterAksesorisRecyclerView extends RecyclerView.Adapter<AdapterAksesorisRecyclerView.ViewHolder>  {

    private ArrayList<Aksesoris> daftarAksesoris;
    private Context context;
    FirebaseDataListener listener;
    CardView kontener;
    public static final String DATA = "com.example.nanodg.tambalban";
    private DatabaseReference database;

    public AdapterAksesorisRecyclerView(ArrayList<Aksesoris> aksesoriss, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarAksesoris = aksesoriss;
        context = ctx;
        listener = (FirebaseDBReadAksesorisActivity)ctx;
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
        final String name = daftarAksesoris.get(position).getNama();
        final String alamat = daftarAksesoris.get(position).getAlamat();
        final String hslstatus = daftarAksesoris.get(position).getStatus();
        //System.out.println("BARANG DATA one by one "+position+daftarTambah.size());
//        Log.e("Data snapshot","barang1"+position+daftarTambah.size());
//        Log.e("Data snapshot","barang1"+name);
        kontener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.e("Data snapshot","Fetched Name"+daftarTambah);
                context.startActivity(FirebaseDBReadSingleAksesorisActivity.getActIntent((Activity) context).putExtra(DATA, holder.tvTitle.getText().toString()));
            }
        });
        kontener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

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
                                context.startActivity(TambahAksesorisActivity.getActIntent((Activity) context).putExtra("data", daftarAksesoris.get(position)));
                            }
                        }
                );

                //apabila tombol delete diklik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                listener.onDeleteData(daftarAksesoris.get(position), position);
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
        return daftarAksesoris.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(Aksesoris aksesoris, int position);
    }
}