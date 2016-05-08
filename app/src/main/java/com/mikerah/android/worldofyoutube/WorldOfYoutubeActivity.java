package com.mikerah.android.worldofyoutube;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class WorldOfYoutubeActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Long numberOfVideos, String region, String category) {
        Intent i = new Intent(context, WorldOfYoutubeActivity.class);
        i.putExtra("Number of videos", numberOfVideos);
        i.putExtra("Region", region);
        i.putExtra("Category", category);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        Long numberOfVideos = getIntent().getLongExtra("Number of videos",5);
        String region = getIntent().getStringExtra("Region");
        String category = getIntent().getStringExtra("Category");
        return WorldOfYoutubeFragment.newInstance(numberOfVideos,region,category);
    }




}
