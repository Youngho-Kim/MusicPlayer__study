package com.kwave.android.musicplayer__study;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static com.kwave.android.musicplayer__study.Player.getDuration;

/**
 * Created by kwave on 2017-06-20.
 */

public class SeekBarThread extends Thread {

    Handler handler;
    boolean runFlag = true;


    public SeekBarThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        while (runFlag) {
            // 매초마다 음원의 실행영역을 가져와서 seekBar의 위치를 변경해준다.
            int current = Player.getCurrent();

            Message msg = new Message();
            msg.what = DetailFragment.CHANGE_SEEKBAR;
            msg.arg1 = current;
            handler.sendMessage(msg);


            // 플레이 시간이 끝나면 thread를 종료한다.
            if (current >= Player.getDuration()) {
                runFlag = false;
            }
            Log.d("SeekBarThread", "current = " + current + " duration = " + getDuration());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    public void setRunFlag(boolean flag) {
        runFlag = flag;
    }


}