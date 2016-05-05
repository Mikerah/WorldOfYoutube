package com.mikerah.android.worldofyoutube;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikerah on 2016-05-04.
 */
public class WorldOfYoutubeFragment extends Fragment {

    private static final String TAG = "WorldOfYoutubeFragment";

    private static YouTube mYoutube;
    private static final Long NUMBER_OF_VIDEOS_RETURNED = 25L;
    private static final String API_KEY = " AIzaSyCA7P4r5I2HffPVR4-A-iHY3a738WQHUDY";

    private RecyclerView mVideoRecyclerView;
    private List<com.google.api.services.youtube.model.Video> mPopularVideos = new ArrayList<>();

    public static WorldOfYoutubeFragment newInstance() {
        return new WorldOfYoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        createYoutubeObj();
        new GetVideosTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View v = inflater.inflate(R.layout.fragment_video_recycler_view, container, false);

        mVideoRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_video_recycler_view);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if(isAdded()) {
            mVideoRecyclerView.setAdapter(new VideoAdapter(mPopularVideos));
        }
    }

    private void createYoutubeObj() {
        try {
            mYoutube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("World of Youtube").build();
        } catch (Exception e) {
            System.err.println("Couldn't create youtube object.");
        }
    }

    private class VideoHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;
        private Video mVideo;

        public VideoHolder(View videoView) {
            super(videoView);

            mTitleTextView = (TextView)videoView;
            mTitleTextView.setClickable(true);
        }

        public void bindVideoItem(Video video) {

            mVideo = video;
            mTitleTextView.setText(mVideo.getSnippet().getTitle());
            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri link = Uri.parse("http://www.youtube.com/watch?v=" + mVideo.getId());
                    Intent i = new Intent(Intent.ACTION_VIEW, link);
                    startActivity(i);
                }
            });
        }

    }


    private class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {

        private List<Video> mVideos;

        public VideoAdapter(List<Video> videos) {
            mVideos = videos;
        }

        @Override
        public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            TextView textView = new TextView(getActivity());
            return new VideoHolder(textView);
        }

        @Override
        public void onBindViewHolder(VideoHolder videoHolder, int position) {
            Video video = mVideos.get(position);
            videoHolder.bindVideoItem(video);
        }

        @Override
        public int getItemCount() {
            return mVideos.size();
        }
    }

    private class GetVideosTask extends AsyncTask<Void,Void,List<Video>> {

        @Override
        protected List<Video> doInBackground(Void... params) {
            List<Video> videos = null;
            try {
                videos = getVideos();
            } catch (IOException io) {
                System.err.println("Couldn't get videos");
            }
            return videos;
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            mPopularVideos = videos;
            setupAdapter();
        }
    }

    private List<Video> getVideos() throws IOException {
        YouTube.Videos.List videoList = mYoutube.videos().list("snippet");
        videoList.setChart("mostPopular");
        videoList.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
        videoList.setKey(API_KEY);

        VideoListResponse videoListResponse = videoList.execute();
        Log.i(TAG, "execute method: " +videoList.execute());

        List<Video> videos = videoListResponse.getItems();
        return videos;
    }



}
