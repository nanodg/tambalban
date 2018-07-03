package com.example.nanodg.tambalban;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import com.example.nanodg.tambalban.ir.apend.slider.ui.Slider;
import com.example.nanodg.tambalban.ir.apend.slider.model.Slide;



public class DtlImgActivity extends AppCompatActivity {
    public static String IMAGE_LIST = "intent_image_item";
    public static String CURRENT_ITEM = "current_item";
    private List<Slide> mUriList;
    private Slider slider;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_img);

        slider = (Slider) findViewById(R.id.slider);
        mUriList = (List<Slide>)
                getIntent().getSerializableExtra(IMAGE_LIST);
        slider.addSlides(mUriList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(" ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();

            }
        });
    }
}
