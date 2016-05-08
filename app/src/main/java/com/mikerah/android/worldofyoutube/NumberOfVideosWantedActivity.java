package com.mikerah.android.worldofyoutube;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Mikerah on 2016-05-07.
 */
public class NumberOfVideosWantedActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, String country, String category) {
        Intent i = new Intent(context, NumberOfVideosWantedActivity.class);
        i.putExtra("Country",country);
        i.putExtra("Category",category);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        String country = getIntent().getStringExtra("Country");
        String category = getIntent().getStringExtra("Category");
        return NumberOfVideosWantedFragment.newInstance(country,category);
    }
}
