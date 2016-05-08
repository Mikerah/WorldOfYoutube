package com.mikerah.android.worldofyoutube;

import android.widget.ArrayAdapter;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoCategoryListResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikerah on 2016-05-05.
 */
public class MiscUtils {

    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }


    public static void addItemsToSpinner(ArrayAdapter<CharSequence> arrayAdapter, Map<CharSequence, CharSequence> map) {
        for(CharSequence key: map.keySet()) {
            arrayAdapter.add(key);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public static void addItemsToSpinner(ArrayAdapter<CharSequence> arrayAdapter, List<CharSequence> list) {
        for(CharSequence key: list) {
            arrayAdapter.add(key);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public static Map<CharSequence,CharSequence> createCategoriesMap(YouTube.VideoCategories.List categoriesList, String apiKey, String regionCode) {
        List<VideoCategory> categories = new ArrayList<>();
        VideoCategoryListResponse videoCategoryListResponse = null;

        categoriesList.setKey(apiKey);
        categoriesList.setRegionCode(regionCode);

        try {
            videoCategoryListResponse = categoriesList.execute();
        } catch (IOException e) {
            System.err.println("Didn't get video categories");
            e.printStackTrace();
        }

        Map<CharSequence,CharSequence> categoriesMap = new HashMap<>();

        categories = videoCategoryListResponse.getItems();
        for(VideoCategory vd: categories) {
            categoriesMap.put(vd.getSnippet().getTitle(),vd.getId());
        }
        categoriesMap.put("All","0");

        return categoriesMap;
    }

}
