package com.cit.abakar.application;

import android.content.Context;
import android.media.MediaPlayer;


public class MyMediaPlayer {

    MediaPlayer mediaPlayer;

    public MyMediaPlayer(Context context, int r) {
        mediaPlayer = MediaPlayer.create(context, r);
        mediaPlayer.setLooping(false);
    }

    public void start(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void reset(){
        mediaPlayer.reset();
    }


    public void setFree(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
}
