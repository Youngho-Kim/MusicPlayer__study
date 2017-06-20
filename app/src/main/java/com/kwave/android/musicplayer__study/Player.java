package com.kwave.android.musicplayer__study;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by kwave on 2017-06-16.
 */


public class Player {
    public static MediaPlayer player = null;
    public static int status  = Const.player.STOP;

    /**
     * 음원을 세팅하는 함수
     * @param musicUri
     * @param context
     * @param handler seekbar 를 조작하는 핸들러
     */
    public static void init(Uri musicUri, Context context, final Handler handler){
        // 1. 미디어플레이어 사용하기
//        musicUri = datas.get(position).musicUri;
        if(player != null){
            player.release();
        }
        player = MediaPlayer.create(context,musicUri);

        // 2. 설정
        player.setLooping(false);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 음악이 종료되면 호출된다.
                // 이 때 seekBar Thread를 멈춰야한다.
                if(handler != null){
                    handler.sendEmptyMessage(DetailFragment.STOP_THREAD);
                }
            }
        });
        Log.d("Player","init======="+player);

    }
    public static void play() {
        // 3. 시작
        player.start();
        status  = Const.player.PLAY;
    }

    public static void pause(){
        player.pause();
        status  = Const.player.PAUSE;
    }

    public static void Replay(){
        player.start();
        status  = Const.player.PLAY;
    }

    public static int getDuration(){
        if(player != null){
            return player.getDuration();
        }
        else{
            return 0;
        }
    }

    // 현재 실행 구간
    public static int getCurrent(){
        Log.d("Player","getCurrent======="+player);
        if(player != null) {
            try {
                return player.getCurrentPosition();
            } catch (Exception e) {
                Log.e("Player", e.toString());
            }
        }
            return 0;
    }


    // current로 구간 이동 시키기
    public static void setCurrent(int current){
        if(player != null)
            player.seekTo(current);
    }
}
