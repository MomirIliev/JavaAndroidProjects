package com.example.youtubeplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "YoutubeActivity";
    static final String GOOGLE_API_KEY = "AIzaSyDlWGrcv0rsgYtXOC4gPvrmfR_iz9eyjms";
    static final String YOUTUBE_VIDEO_ID = "ElpitAfkRS4";
//    static final String YOUTUBE_PLAYLIST = "PLXtTjtWmQhg1SsviTmKkWO5n0a_-T0bnD&index=1";
    static final String YOUTUBE_PLAYLIST = "PLXtTjtWmQhg1YRG0L0dIT5B22Utujaq20";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_youtube);
//        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_youtube);
        ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_youtube, null);
        setContentView(layout);

//        Button button1 = new Button(this);
//        button1.setLayoutParams(new ConstraintLayout.LayoutParams(500, 150));
//        button1.setText("Button added");
//        layout.addView(button1);

        YouTubePlayerView playerView = new YouTubePlayerView(this);
        playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(playerView);
        playerView.initialize(GOOGLE_API_KEY, this);



    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess: provider is: " + provider.getClass().toString());
        Toast.makeText(this, "Initialized Youtube Player successfully", Toast.LENGTH_LONG).show();

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        if(!wasRestored){
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        // Dokolku ne uspee programot da go inicijalizira YouTube player-ot, najprvo vo if se proveruva dali korisnikot moze da go resi problemot (na primer da go apdejtira Youtube,
        // ili vo else se prikazuva greskata koja nastanala pri obidot za inicijaliziranje na Youtube player-ot. 
        final int REQUEST_CODE = 1;

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
        }
        else {
            String errorMessage = String.format("There was an error initializing YoutubePlayer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } }

        private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                Toast.makeText(YoutubeActivity.this, "Good, video is playing ok! ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPaused() {
                Toast.makeText(YoutubeActivity.this, "Video has paused!  ", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onStopped() {
                Toast.makeText(YoutubeActivity.this, "Video has Stopped!  ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        };

        private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {
                Toast.makeText(YoutubeActivity.this, "Click on the AD and make video creator rich! ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVideoStarted() {
                Toast.makeText(YoutubeActivity.this, "Video has started!  ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVideoEnded() {
                Toast.makeText(YoutubeActivity.this, "Congratulations, you have completed another video. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        };
}
