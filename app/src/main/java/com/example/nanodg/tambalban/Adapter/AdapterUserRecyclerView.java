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

import com.example.nanodg.tambalban.EditTambalActivity;
import com.example.nanodg.tambalban.FirebaseDBReadSingleActivity;
import com.example.nanodg.tambalban.ListTambalActivity;
import com.example.nanodg.tambalban.ListUserActivity;
import com.example.nanodg.tambalban.Model.Tambah;
import com.example.nanodg.tambalban.Model.User;
import com.example.nanodg.tambalban.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;




public class AdapterUserRecyclerView extends RecyclerView.Adapter<AdapterUserRecyclerView.ViewHolder>  {

    private ArrayList<User> daftarUser;
    private Context context;
    FirebaseDataListener listener;
    CardView kontener;

    private DatabaseReference database;

    public AdapterUserRecyclerView(ArrayList<User> users, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarUser = users;
        context = ctx;
        listener = (ListUserActivity)ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvemail,tvusername,tvstatus;

        ViewHolder(View v) {
            super(v);
            tvemail = (TextView) v.findViewById(R.id.tv_email);
            tvusername = (TextView) v.findViewById(R.id.tv_username);
            tvstatus = (TextView) v.findViewById(R.id.tv_status);
            kontener = (CardView) v.findViewById(R.id.kontener);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengguna_admin, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         */
        final String email = daftarUser.get(position).getEmail();
        final String nama = daftarUser.get(position).getUsername();
        final String status = daftarUser.get(position).getPemilik();

        kontener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

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
                                listener.onDeleteData(daftarUser.get(position), position);
                            }
                        }
                );
                return true;
            }
        });


        holder.tvemail.setText("Email : " +email);
        holder.tvusername.setText("Nama : " +nama);
        if(status.equals("0")){
            holder.tvstatus.setText("Status : pengguna");
        } if(status.equals("1")){
            holder.tvstatus.setText("Status : Pemilik");
        } if(status.equals("2")){
            holder.tvstatus.setText("Status : Admin");
        }


    }

    @Override
    public int getItemCount() {
        /**
         * Mengembalikan jumlah item pada barang
         */
        return daftarUser.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(User user, int position);
    }
}