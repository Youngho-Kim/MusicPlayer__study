package com.kwave.android.musicplayer__study;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.kwave.android.musicplayer__study.util.PermissionControl;

public class MainActivity extends AppCompatActivity implements
        ListFragment.OnListFragmentInteractionListener,
        DetailFragment.PlayerInterface,
        PermissionControl.CallBack{

    FrameLayout layout;
    ListFragment list;
    DetailFragment detail;

    Intent service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 볼륨조절버튼으로 미디어 음량만 조절하기 위한 설정
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        service = new Intent(this, PlayerService.class);

        list = ListFragment.newInstance(1);
        detail = DetailFragment.newInstance();

        PermissionControl.checkVersion(this);
        // ListFragment.newInstance(1) : 리스트뷰 형태
        // ListFragment.newInstance(2), ListFragment.newInstance(3) : 그리그뷰 형태
    }




    // 3. 사용자가 권한체크 팝업에서 권한을 승인 또는 거절하면 아래 메서드가 호출된다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this,requestCode,grantResults);

    }

    @Override
    public void init() {
        setViews();
        setFragment(ListFragment.newInstance(1));    // 목록프래그넌트
    }


    private void setViews(){
        layout = (FrameLayout) findViewById(R.id.layout);
    }

    private void setFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.commit();
    }

    private void addFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Fragment 를 통해 Adapter까지 interface를 전달하고
    // Adapter에서 interface를 직접 호출해서 사용한다.
    @Override
    public void goDetailInteraction(int position) {
        detail.setPosition(position);
//        addFragment(DetailFragment.newInstance(position));
        addFragment(detail);
    }

    @Override
    protected void onDestroy() {
        detail.setDestroy();
        super.onDestroy();
    }

    @Override
    public void initPlayer(){

    }

    @Override
    public void playPlayer(){
        // 1. service를 생성하고
        service.setAction(Const.Action.PLAY);
        startService(service);

        // 2. service에 명령어를 담아서 넘긴다.


        // 3.
    }

    @Override
    public void stopPlayer(){
        service.setAction(Const.Action.STOP);
        startService(service);
    }

    @Override
    public void pausePlayer(){
        service.setAction(Const.Action.PAUSE);
        startService(service);
    }


}
