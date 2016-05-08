package com.mikerah.android.worldofyoutube;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Mikerah on 2016-05-05.
 */
public class YoutubeHelper {

    public static YouTube createYoutubeObj() {
        YouTube youTube = null;
        try {
            youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("World of Youtube").build();
        } catch (Exception e) {
            System.err.println("Couldn't create youtube object.");
        }
        return youTube;
    }

    public static Intent watchVideoIntent(Video video) {
        Uri link = Uri.parse("http://www.youtube.com/watch?v=" + video.getId());
        Intent i = new Intent(Intent.ACTION_VIEW, link);
        return i;
    }

    public static List<Video> getPopularVideosList(YouTube youTube, String regionCode,String category, Long numVideos) throws IOException {
        Map<CharSequence,CharSequence> categories = MiscUtils.createCategoriesMap(youTube.videoCategories().list("snippet,id"),Constants.API_KEY,regionCode);

        YouTube.Videos.List videoList = youTube.videos().list("snippet,contentDetails");
        videoList.setChart("mostPopular");
        videoList.setMaxResults(numVideos);
        videoList.setRegionCode(regionCode);
        Log.d("YoutubeHelper", "Category, CategoryId: "+ category + ", "+categories.get(category));
        videoList.setVideoCategoryId((String) categories.get(category));
        videoList.setKey(Constants.API_KEY);

        VideoListResponse videoListResponse = videoList.execute();

        List<Video> videos = videoListResponse.getItems();
        return videos;
    }

    public static List<CharSequence> getPossibleCategories(String country) {
        YouTube.VideoCategories.List list = null;
        try {
            list = createYoutubeObj().videoCategories().list("snippet");
            list.setRegionCode(country);
            list.setKey(Constants.API_KEY);
        } catch (IOException e) {
            System.err.println("Couldn't create list");
            e.printStackTrace();
        }

        List<VideoCategory> videoCatList = null;
        try {
            videoCatList = list.execute().getItems();
        } catch (IOException e) {
            System.out.println("Didnt'get categories");
            e.printStackTrace();
        }

        List<CharSequence> videoCategories = new ArrayList<>();
        for(VideoCategory vd: videoCatList) {
            if(vd.getSnippet().getAssignable() == true) {
                videoCategories.add(vd.getSnippet().getTitle());
            }
        }
        videoCategories.add("All");

        return videoCategories;
    }

    public static String getVideoThumbnailUrl(Video video) {
        VideoSnippet videoSnippet = video.getSnippet();
        ThumbnailDetails thumbnailDetails = videoSnippet.getThumbnails();
        Thumbnail thumbnail = thumbnailDetails.getDefault();
        String thumbnailUrl = thumbnail.getUrl();

        return thumbnailUrl;
    }

    public static String getVideoUploadDate(Video video) {
        String ud = video.getSnippet().getPublishedAt().toString();
        String uploadDate = ud.substring(0,ud.indexOf('T'));
        return uploadDate;
    }


    public static String getVideoDuration(Video video) {
        String duration_in_ISO = video.getContentDetails().getDuration().substring(2);
        String pattern = "(\\d+)M(\\d+)S";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(duration_in_ISO);


        String duration = null;
        if (m.find()) {
            if (m.group(2).length() > 1) {
                duration = m.group(1) + ":" + m.group(2);
            } else {
                duration = m.group(1) + ":0" + m.group(2);
            }
        } else {

            Pattern patternSec = Pattern.compile("(\\d+)S");
            Matcher matcherSec = patternSec.matcher(duration_in_ISO);
            matcherSec.find();
            duration = "0:"+matcherSec.group(1);

        }
        return duration;

    }

}
