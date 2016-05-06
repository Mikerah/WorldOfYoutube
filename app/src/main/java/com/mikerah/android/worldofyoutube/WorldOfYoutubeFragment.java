package com.mikerah.android.worldofyoutube;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;

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

    private ThumbnailDownloader<VideoHolder> mThumbnailDownloader;

    public static WorldOfYoutubeFragment newInstance() {
        return new WorldOfYoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mYoutube = YoutubeHelper.createYoutubeObj();
        new GetVideosTask().execute();

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<VideoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(VideoHolder photoHolder, Bitmap bitmap) {
                        if (!isAdded()) return;
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View v = inflater.inflate(R.layout.fragment_video_recycler_view, container, false);

        mVideoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_video_recycler_view);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        setupAdapter();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
    }

    private void setupAdapter() {
        if (isAdded()) {
            mVideoRecyclerView.setAdapter(new VideoAdapter(mPopularVideos));
        }
    }

    private class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Video mVideo;

        private ImageView mVideoThumbnail;
        private TextView mVideoInfoTextView;


        public VideoHolder(View videoView) {
            super(videoView);
            videoView.setOnClickListener(this);

            mVideoThumbnail = (ImageView) videoView.findViewById(R.id.list_video_thumbnail_image_view);
            mVideoInfoTextView = (TextView)videoView.findViewById(R.id.video_info_text_view);
        }


        public void bindVideoItem(Video video) {

            mVideo = video;

            VideoSnippet videoSnippet = mVideo.getSnippet();
            String title = videoSnippet.getTitle();
            String channel = videoSnippet.getChannelTitle();
            String duration = mVideo.getContentDetails().getDuration();
            String uploadDate = videoSnippet.getPublishedAt().toString();
            mVideoInfoTextView.setText(getString(R.string.video_info,title,channel,duration,uploadDate));

        }

        public void bindDrawable(Drawable drawable) {
            mVideoThumbnail.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View v) {
            startActivity(YoutubeHelper.watchVideoIntent(mVideo));
        }
    }


    private class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {

        private List<Video> mVideos;

        public VideoAdapter(List<Video> videos) {
            mVideos = videos;
        }

        @Override
        public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_video, viewGroup, false);
            return new VideoHolder(view);
        }

        @Override
        public void onBindViewHolder(VideoHolder videoHolder, int position) {
            Video video = mVideos.get(position);
            videoHolder.bindVideoItem(video);
            mThumbnailDownloader.queueThumbnail(videoHolder, YoutubeHelper.getVideoThumbnailUrl(video));
        }

        @Override
        public int getItemCount() {
            return mVideos.size();
        }
    }

    private class GetVideosTask extends AsyncTask<Void, Void, List<Video>> {

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
