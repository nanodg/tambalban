package com.example.nanodg.tambalban;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import com.example.nanodg.tambalban.ir.apend.slider.ui.Slider;
import com.example.nanodg.tambalban.ir.apend.slider.model.Slide;



public class DtlImgActivity extends AppCompatActivity {
    public static String IMAGE_LIST = "intent_image_item";
    public static String CURRENT_ITEM = "current_item";
    private List<Slide> mUriList;
    private Slider slider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_img);

        slider = (Slider) findViewById(R.id.slider);
        mUriList = (List<Slide>)
                getIntent().getSerializableExtra(IMAGE_LIST);
        slider.addSlides(mUriList);
    }
}
