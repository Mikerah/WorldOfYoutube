package com.mikerah.android.worldofyoutube;

import android.widget.ArrayAdapter;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoCategory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    /*
    public static void addItemsToSpinner(ArrayAdapter<CharSequence> arrayAdapter, YouTube.VideoCategories.List categoriesList){
        List<VideoCategory> categories = null;
        try {
            categories = categoriesList.execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(VideoCategory vd: categories) {
            arrayAdapter.add(vd.getSnippet().getTitle());
        }
        arrayAdapter.notifyDataSetChanged();
    }
    */

    public static Map<CharSequence,CharSequence> createCategoriesMap(YouTube.VideoCategories.List categoriesList) {
        List<VideoCategory> categories = null;
        try {
            categories = categoriesList.execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<CharSequence,CharSequence> categoriesMap = new HashMap<>();

        for(VideoCategory vd: categories) {
            categoriesMap.put(vd.getSnippet().getTitle(),vd.getId());
        }

        return categoriesMap;
    }

}
