package com.example.youtubeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class StandaloneActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standalone);

        Button btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        Button btnPlayList = (Button) findViewById(R.id.btnPlaylist);

        // Bidejki na linija 10 go implementiravme View.OnCLickListener i kreiravme instanca od istiot vo onClick(View v) mozeme vo kreiranite kopcinja vo parametrite da
        // ispratime metod this koj pokazuva tokmu na ovaa instanca.
        btnPlayVideo.setOnClickListener(this);
        btnPlayList.setOnClickListener(this);

        // Vo prodolzenie e primer kako moze da se kreira OnClickListener koj isto taka moze da bide upotreben za poveke kopcinja
//        View.OnClickListener myListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        };
//
//        btnPlayVideo.setOnClickListener(myListener);
//        btnPlayList.setOnClickListener(myListener);

        // Vo prodolzenie e primer kako moze da se kreira OnClickListener, no vo ovoj slucaj istiot moze da bide upotreben samo za edno kopce
//        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {

            // Pri kreiranjeto na intent vo ovoj slucaj se koristat tri parametri: prviot (this) pokazuva od koja aktivnost se povikuva intent, vtoriot pretstavuva API_KEY i tretiot (ID od videoto) pretstavuva referenca ili link do videoto koe ke se emituva
            case R.id.btnPlayVideo:
//                intent = YouTubeStandalonePlayer.createVideoIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_VIDEO_ID);
//                break;

            intent = YouTubeStandalonePlayer.createVideoIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_VIDEO_ID, 0, true, false);
            break;

            case R.id.btnPlaylist:
    //          intent = YouTubeStandalonePlayer.createPlaylistIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_PLAYLIST);
             //   break;
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_PLAYLIST, 0, 0, true, false);
                break;
            default:

        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
