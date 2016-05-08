package com.mikerah.android.worldofyoutube;

import android.support.v4.app.Fragment;

/**
 * Created by Mikerah on 2016-05-07.
 */
public class CountryWantedActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return CountryWantedFragment.newInstance();
    }
}
