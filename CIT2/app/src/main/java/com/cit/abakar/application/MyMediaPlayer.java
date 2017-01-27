package com.cit.abakar.application;

import android.content.Context;
import android.media.MediaPlayer;


public class MyMediaPlayer {

    MediaPlayer mediaPlayer;

    public MyMediaPlayer(Context context, String v) {
        if(v.equals("Button")){
            mediaPlayer = MediaPlayer.create(context, R.raw.button_sound);
        }
        if(v.equals("Switch")){
            mediaPlayer = MediaPlayer.create(context, R.raw.switch_sound);
        }
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
