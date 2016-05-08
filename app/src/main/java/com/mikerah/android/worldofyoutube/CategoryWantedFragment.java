package com.mikerah.android.worldofyoutube;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

/**
 * Created by Mikerah on 2016-05-07.
 */
public class CategoryWantedFragment extends Fragment {
    private List<CharSequence> mPossibleCategories;
    private String mCountry;
    private Spinner mCategorySpinner;
    private ArrayAdapter<CharSequence> mCategoryAdapter;

    public static CategoryWantedFragment newInstance(String country) {
        Bundle args = new Bundle();
        args.putString(Constants.COUNTRY_CODE,country);

        CategoryWantedFragment fragment = new CategoryWantedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mCountry = (String) Constants.COUNTRIES.get(getArguments().getString(Constants.COUNTRY_CODE));

        mCategorySpinner = new Spinner(getActivity().getBaseContext());

        mCategoryAdapter = new ArrayAdapter<CharSequence>(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item);

        new GetCategoriesTask(mCountry).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_wanted, container, false);

        mCategorySpinner = (Spinner) view.findViewById(R.id.categories_spinner);
        mCategorySpinner.setAdapter(mCategoryAdapter);

        Button nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String category = mCategorySpinner.getSelectedItem().toString();
                Intent i = NumberOfVideosWantedActivity.newIntent(getActivity(), mCountry,category);
                startActivity(i);

            }
        });


        return view;
    }

    private class GetCategoriesTask extends AsyncTask<Void, Void, List<CharSequence>> {
        private String mCountry;

        public GetCategoriesTask(String country) {
            mCountry = country;
        }

        @Override
        protected List<CharSequence> doInBackground(Void... params) {
            List<CharSequence> videoCategories = null;
            videoCategories = YoutubeHelper.getPossibleCategories(mCountry);
            return videoCategories;
        }

        @Override
        protected void onPostExecute(List<CharSequence> categories) {
            mPossibleCategories = categories;
            MiscUtils.addItemsToSpinner(mCategoryAdapter,mPossibleCategories);
        }

    }
}
