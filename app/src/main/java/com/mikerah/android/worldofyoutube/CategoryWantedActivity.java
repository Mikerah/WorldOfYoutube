package com.mikerah.android.worldofyoutube;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Mikerah on 2016-05-07.
 */
public class CategoryWantedActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, String country) {
        Intent i = new Intent(context, CategoryWantedActivity.class);
        i.putExtra("Country", country);
        return i;

    }

    @Override
    protected Fragment createFragment() {
        String country = getIntent().getStringExtra("Country");
        return CategoryWantedFragment.newInstance(country);
    }
}
