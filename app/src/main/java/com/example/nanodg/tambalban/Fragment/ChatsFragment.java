package com.example.nanodg.tambalban.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nanodg.tambalban.ActivityChatDetails;
import com.example.nanodg.tambalban.PesanPemilikActivity;
import com.example.nanodg.tambalban.R;
import com.example.nanodg.tambalban.Adapter.ChatsListAdapter;
import com.example.nanodg.tambalban.data.ParseFirebaseData;
import com.example.nanodg.tambalban.data.SettingsAPI;
import com.example.nanodg.tambalban.Model.ChatMessage;
import com.example.nanodg.tambalban.widget.DividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatsFragment extends Fragment {

    public RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;
    public ChatsListAdapter mAdapter;
    private ProgressBar progressBar;

    public static final String MESSAGE_CHILD = "messages";

    View view;

    ParseFirebaseData pfbd;
    SettingsAPI set;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        pfbd = new ParseFirebaseData(getContext());
        set = new SettingsAPI(getContext());

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(MESSAGE_CHILD);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalData = "";
                if (dataSnapshot.getValue() != null)
                    totalData = dataSnapshot.getValue().toString();
                // TODO: 25-05-2017 if number of items is 0 then show something else
                mAdapter = new ChatsListAdapter(getContext(), pfbd.getLastMessageList(totalData));
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ChatsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, ChatMessage obj, int position) {
                        if (obj.getReceiver().getKey().equals(set.readSetting("myid")))
                            ActivityChatDetails.navigate((PesanPemilikActivity) getActivity(), v.findViewById(R.id.lyt_parent), obj.getSender());
                        else if (obj.getSender().getKey().equals(set.readSetting("myid")))
                            ActivityChatDetails.navigate((PesanPemilikActivity) getActivity(), v.findViewById(R.id.lyt_parent), obj.getReceiver());
                    }
                });

                bindView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (getView() != null)
                    Snackbar.make(getView(), "Could not connect", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void bindView() {
        try {
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
        }

    }
}
