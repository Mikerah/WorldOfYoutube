package com.mikerah.android.worldofyoutube;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class WorldOfYoutubeActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, WorldOfYoutubeActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return WorldOfYoutubeFragment.newInstance();
    }




}
