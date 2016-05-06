package com.mikerah.android.worldofyoutube;

import android.content.Intent;
import android.net.Uri;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;

import java.io.IOException;
import java.util.List;

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

    public static List<Video> getPopularVideosList(YouTube youTube) throws IOException {
        YouTube.Videos.List videoList = youTube.videos().list("snippet,contentDetails");
        videoList.setChart("mostPopular");
        videoList.setMaxResults(Constants.NUMBER_OF_VIDEOS_RETURNED);
        videoList.setKey(Constants.API_KEY);

        VideoListResponse videoListResponse = videoList.execute();

        List<Video> videos = videoListResponse.getItems();
        return videos;
    }

    public static String getVideoThumbnailUrl(Video video) {
        VideoSnippet videoSnippet = video.getSnippet();
        ThumbnailDetails thumbnailDetails = videoSnippet.getThumbnails();
        Thumbnail thumbnail = thumbnailDetails.getStandard();
        String thumbnailUrl = thumbnail.getUrl();

        return thumbnailUrl;
    }
}
