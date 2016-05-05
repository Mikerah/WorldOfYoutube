package com.mikerah.android.worldofyoutube;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikerah on 2016-05-04.
 */
public class WorldOfYoutubeFragment extends Fragment {



    private static YouTube mYoutube;

    private RecyclerView mVideoRecyclerView;
    private List<com.google.api.services.youtube.model.Video> mPopularVideos = new ArrayList<>();

    public static WorldOfYoutubeFragment newInstance() {
        return new WorldOfYoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mYoutube = YoutubeHelper.createYoutubeObj();
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
                    startActivity(YoutubeHelper.watchVideoIntent(mVideo));
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
                videos = YoutubeHelper.getPopularVideosList(mYoutube);
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


}
