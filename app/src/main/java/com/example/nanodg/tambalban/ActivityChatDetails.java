package com.example.nanodg.tambalban;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.nanodg.tambalban.Adapter.ChatDetailsListAdapter;
import com.example.nanodg.tambalban.data.ParseFirebaseData;
import com.example.nanodg.tambalban.data.SettingsAPI;
import com.example.nanodg.tambalban.data.Tools;
import com.example.nanodg.tambalban.Model.ChatMessage;
import com.example.nanodg.tambalban.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityChatDetails extends AppCompatActivity {
    public static String KEY_FRIEND = "FRIEND";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, User obj) {
        Intent intent = new Intent(activity, ActivityChatDetails.class);
        intent.putExtra(KEY_FRIEND, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_FRIEND);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Button btn_send;
    private EditText et_content;
    public static ChatDetailsListAdapter mAdapter;
    private ListView listview;
    private ActionBar actionBar;
    private User user;
    private List<ChatMessage> items = new ArrayList<>();
    private View parent_view;
    ParseFirebaseData pfbd;
    SettingsAPI set;

    String chatNode, chatNode_1, chatNode_2;

    public static final String MESSAGE_CHILD = "messages";
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        parent_view = findViewById(android.R.id.content);
        pfbd = new ParseFirebaseData(this);
        set = new SettingsAPI(this);

        // animation transition
        ViewCompat.setTransitionName(parent_view, KEY_FRIEND);

        // initialize conversation data
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable(KEY_FRIEND);

        initToolbar();
        iniComponen();
        chatNode_1 = set.readSetting("myid") + "-" + user.getKey();
        chatNode_2 = user.getKey() + "-" + set.readSetting("myid");

        ref = FirebaseDatabase.getInstance().getReference(MESSAGE_CHILD);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(chatNode_1)) {
                    chatNode = chatNode_1;
                } else if (dataSnapshot.hasChild(chatNode_2)) {
                    chatNode = chatNode_2;
                } else {
                    chatNode = chatNode_1;
                }
                String totalData = dataSnapshot.child(chatNode).toString();
                items.clear();
                items.addAll(pfbd.getMessageListForUser(totalData));
                mAdapter = new ChatDetailsListAdapter(ActivityChatDetails.this, items);
                listview.setAdapter(mAdapter);
                listview.setSelectionFromTop(mAdapter.getCount(), 0);
                listview.requestFocus();
                registerForContextMenu(listview);
                bindView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getWindow().getDecorView(), "Could not connect", Snackbar.LENGTH_LONG).show();
            }
        });

        // for system bar in lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Tools.systemBarLolipop(this);
        }
    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(user.getUsername());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();

            }
        });
    }

    public void bindView() {
        try {
            mAdapter.notifyDataSetChanged();
            listview.setSelectionFromTop(mAdapter.getCount(), 0);
        } catch (Exception e) {

        }
    }

    public void iniComponen() {
        listview = (ListView) findViewById(R.id.listview);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                ChatMessage im=new ChatMessage(et_content.getText().toString(), String.valueOf(System.currentTimeMillis()),friend.getId(),friend.getName(),friend.getPhoto());

                HashMap hm = new HashMap();
                hm.put("text", et_content.getText().toString());
                hm.put("timestamp", String.valueOf(System.currentTimeMillis()));
                hm.put("receiverkey", user.getKey());
                hm.put("receivernama", user.getUsername());
                hm.put("senderkey", set.readSetting("myid"));
                hm.put("sendernama", set.readSetting("myname"));

                ref.child(chatNode).push().setValue(hm);
                et_content.setText("");
                hideKeyboard();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        if (et_content.length() == 0) {
            btn_send.setEnabled(false);
        }
        hideKeyboard();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setEnabled(false);
            } else {
                btn_send.setEnabled(true);
            }
            //draft.setContent(etd.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
