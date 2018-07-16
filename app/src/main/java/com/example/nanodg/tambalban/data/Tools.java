package com.example.nanodg.tambalban.data;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.nanodg.tambalban.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Tools {
    private static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "Error API" + e.getMessage());
        }

        return f.floatValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void systemBarLolipop(Activity act){
        if (getAPIVerison() >= 5.0) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static String formatTime(long time){
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if(date.get(Calendar.YEAR)==curDate.get(Calendar.YEAR)){
            if(date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR) ){
                dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            }
            else{
                dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
            }
        }
        else{
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        }
        return dateFormat.format(time);
    }
}
