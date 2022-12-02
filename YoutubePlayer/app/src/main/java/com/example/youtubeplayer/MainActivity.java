package com.example.youtubeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlayVideo = (Button) findViewById(R.id.btnSingleVideos);
        Button btnSubMenu = (Button) findViewById(R.id.btnStandalone);
        btnPlayVideo.setOnClickListener(this);
        btnSubMenu.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btnSingleVideos:
                intent = new Intent(this, YoutubeActivity.class);
                break;
            case R.id.btnStandalone:
                intent = new Intent(this, StandaloneActivity.class);
                break;

            default:
        }
        if(intent != null){
            startActivity(intent);
        }
    }
}
