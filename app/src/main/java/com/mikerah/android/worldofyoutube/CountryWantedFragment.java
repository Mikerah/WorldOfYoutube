package com.mikerah.android.worldofyoutube;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mikerah on 2016-05-07.
 */
public class CountryWantedFragment extends Fragment {

    public static CountryWantedFragment newInstance() {
        return new CountryWantedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIntanceState) {
        View view = inflater.inflate(R.layout.fragment_country_wanted,container,false);

        final Spinner countriesSpinner = (Spinner) view.findViewById(R.id.countries_spinner);
        String[] countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<CharSequence> countriesAdapter = new ArrayAdapter<CharSequence>(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList(Arrays.asList(countries)));
        MiscUtils.addItemsToSpinner(countriesAdapter, Constants.COUNTRIES);
        countriesSpinner.setAdapter(countriesAdapter);



        Button nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String country = countriesSpinner.getSelectedItem().toString();
                Intent i = CategoryWantedActivity.newIntent(getActivity(), country);
                startActivity(i);
            }
        });


        return view;
    }
}
