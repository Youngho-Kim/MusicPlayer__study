package com.kwave.android.musicplayer__study;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kwave.android.musicplayer__study.domain.Music;
import com.kwave.android.musicplayer__study.util.TimeUtil;

import java.util.List;

import static com.kwave.android.musicplayer__study.DetailFragment.CHANGE_SEEKBAR;
import static com.kwave.android.musicplayer__study.DetailFragment.STOP_THREAD;
import static com.kwave.android.musicplayer__study.Player.player;
import static com.kwave.android.musicplayer__study.util.TimeUtil.miliToMinSec;

/**
 * Created by kwave on 2017-06-20.
 */

// ViewPaser의 view
public class DetailView implements View.OnClickListener{
        Context context;
        View view;
        ViewPager viewPager;
        RelativeLayout layoutController;
        ImageButton btnPlay, btnNext, btnPre;
        SeekBar seekBar;
        TextView current, duration;

        // 플레이어를 컨트롤하는 메인에 있는 함수 사용
        DetailFragment.PlayerInterface playerInterface;

        // 음악플레이에 따라 seekbar를 변경해주는 thread
        SeekBarThread seekBarThread = null;
        // 프레젠터 역할 - 인터페이스 설계 필요
        DetailFragment presenter;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_SEEKBAR :
                    setSeekBarPosition(msg.arg1);
                    break;
                case STOP_THREAD :
                    seekBarThread.setRunFlag(false);
                    break;

            }
        }
    };

        public View getView(){
            return view;
        }

        public DetailView(View view, DetailFragment presenter, DetailFragment.PlayerInterface playerInterface){
            this.playerInterface = playerInterface;
            this.context = view.getContext();
            this.view = view;
            this.presenter = presenter;
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            layoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
            btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
            btnNext = (ImageButton) view.findViewById(R.id.btnNext);
            btnPre = (ImageButton) view.findViewById(R.id.btnPre);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            current = (TextView) view.findViewById(R.id.current);
            duration = (TextView) view.findViewById(R.id.duration);

        }
        public void init(int position){
            setOnClickedListener();
            setViewPager(position);
            setSeekBarBar();
            setSeekBarThread();
        }

        private void setOnClickedListener() {
            btnPlay.setOnClickListener(this);
            btnNext.setOnClickListener(this);
            btnPre.setOnClickListener(this);
        }

        public List<Music.Item> getDatas(){
            Music music = Music.getInstance();
            music.loader(context);
            return music.getItems();
        }

        private void setViewPager(int position) {
            DetailAdapter adapter = new DetailAdapter(getDatas());
            // 어댑터를 생성
            viewPager.setAdapter(adapter);
            // 리스너를 달았다.
            viewPager.addOnPageChangeListener(viewPagerListener);
            // 페이지 이동하고
            viewPager.setCurrentItem(position);
            //처음  한번 Presenter에 해당되는 Fragment의 musicInit을 호출해서 음악을 초기화해준다.
//            musicInit(position);

        }

        private void setSeekBarBar(){
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // 사용자가 seekbar를 터치했을때만 동작하도록 설정
                    if(fromUser)
                        Player.setCurrent(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        private void setSeekBarThread(){
            //SeekBar를 변경해주는 thread
            seekBarThread = new SeekBarThread(handler);
            seekBarThread.start();
        }

        public void setDuration(int time){
            String formmatted = miliToMinSec(time);
            duration.setText(formmatted);
        }



        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnPlay:
//                    Uri musicUri = getDatas().get(position).musicUri;
//                    Player.play(musicUri, v.getContext());
                    play();
//                    // seekbar의 최대길이를 지정
//                    Log.d("DetailFragment","duration = " + Player.getDuration());
//                    seekBar.setMax(player.getDuration());

                    // seekbar를 변경해주는 thread
//                    new SeekBarThread(handler).start();
//                    seekBarThread.start();

                    break;
                case R.id.btnPre:
                    prev();
                    break;
                case R.id.btnNext:
                    next();
                    break;
            }
        }


        public void play(){
            switch (Player.status){
                case Const.player.PLAY :
//                    Player.pause();
                    playerInterface.pausePlayer();
                    // pause 가 클릭되면 이미지 모양이 play 로 바뀐다.
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    break;
                case Const.player.PAUSE :
//                    Player.Replay();
                    playerInterface.playPlayer();
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    break;
                case Const.player.STOP :
//                    Player.pause();
                    playerInterface.playPlayer();

                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    break;
            }


        }
        public void next(){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }
        public void prev(){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);

        }

    public void setSeekBarPosition(int time) {
        seekBar.setProgress(time);
        current.setText(TimeUtil.miliToMinSec(time));
    }



        // 최초에 호출될 경우는 페이지의 이동이 없으므로 호출되지 않는다
        ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 페이지의 변경사항을 체크해서 현재 페이지 값을 알려준다.
            @Override
            public void onPageSelected(int position) {
                // 현재 페이지가 변경된 후 호출된다.
                // 플레이어에 음악을 세팅해준다.
//            Uri musicUri = getDatas().get(position);
//            player.init(musicUri);
                musicInit(position);
//            Log.d("position", "position ================"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        // 음악을 초기화 해준다.
        public void musicInit(int position){
            Uri musicUri = getDatas().get(position).musicUri;
//        Log.d("position", "position ================"+position);
            Player.init(musicUri, context, handler);

//        Log.d("DetailFragment", "musicInit detailView ================"+detailView);
            int musicDuration = player.getDuration();
            setDuration(musicDuration);
            seekBar.setMax(player.getDuration());

            // 뷰페이저 이동시 음원을 초기화하는데 이전 음원이 실행상태였으면 자동으로 실행시켜준다.
            if(Player.status == Const.player.PLAY){
                Player.play();
            }
        }


        public void setDestroy(){
            seekBarThread.setRunFlag(false);
            seekBarThread.interrupt();
        }



}
