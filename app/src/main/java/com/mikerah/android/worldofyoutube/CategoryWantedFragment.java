package com.mikerah.android.worldofyoutube;

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

        ArrayAdapter<CharSequence> categoriesAdapter = new ArrayAdapter<CharSequence>(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item);

        mCategorySpinner.setAdapter(categoriesAdapter);
        new GetCategoriesTask(mCountry).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_wanted, container, false);

        Spinner categoriesSpinner = (Spinner) view.findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> categoriesAdapter = new ArrayAdapter<CharSequence>(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mPossibleCategories
        );

        mCategorySpinner.setAdapter(mCategoryAdapter);


        Button nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String category = mCategorySpinner.getSelectedItem().toString();
            }
        });


        return view;
    }

    private class GetCategoriesTask extends AsyncTask<Void, Void, Void> {
        private String mCountry;

        public GetCategoriesTask(String country) {
            mCountry = country;
        }

        @Override
        protected Void doInBackground(Void... params) {
            MiscUtils.addItemsToSpinner(mCategoryAdapter,YoutubeHelper.getPossibleCategories(mCountry));
            return null;
        }
        /*
        @Override
        protected void onPostExecute(List<CharSequence> categories) {
            mPossibleCategories = categories;
        }
        */
    }
}
