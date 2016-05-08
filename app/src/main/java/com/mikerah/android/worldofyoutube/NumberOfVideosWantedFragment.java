package com.mikerah.android.worldofyoutube;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Mikerah on 2016-05-07.
 */
public class NumberOfVideosWantedFragment extends Fragment {

    private String mCountry;
    private String mCategory;

    public static NumberOfVideosWantedFragment newInstance(String country, String category) {
        Bundle args = new Bundle();
        args.putString(Constants.COUNTRY_CODE,country);
        args.putString(Constants.CATEGORY_CODE,category);

        NumberOfVideosWantedFragment fragment = new NumberOfVideosWantedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setRetainInstance(true);

        mCountry = getArguments().getString(Constants.COUNTRY_CODE);
        mCategory =(String) getArguments().getString(Constants.CATEGORY_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.fragment_number_of_videos_wanted, container, false);

        final EditText numberOfVideosEditText = (EditText) view.findViewById(R.id.number_of_videos_edit_text);

        Button nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long numberOfVideos = Long.valueOf(numberOfVideosEditText.getText().toString());
                Intent intent = WorldOfYoutubeActivity.newIntent(getActivity(), numberOfVideos, mCountry, mCategory);
                startActivity(intent);
            }
        });

        return view;

    }
}
