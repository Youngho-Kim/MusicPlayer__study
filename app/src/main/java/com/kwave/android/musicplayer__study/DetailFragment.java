package com.kwave.android.musicplayer__study;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    public static final int CHANGE_SEEKBAR = 99;
    public static final int STOP_THREAD = 98;
    //    static final String ARG1 = "position";
    private int position = -1;
    public PlayerInterface playerInterface;

    private DetailView viewHolder = null;

    public DetailFragment() {
        // Required empty public constructor

    }

    public void setPosition(int position){
        this.position = position;
    }

    public static DetailFragment newInstance(){
        DetailFragment fragment = new DetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG1,position);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PlayerInterface){
            playerInterface = (PlayerInterface) context;
        }
        else{
            // PlayerInterface를 Activity가 구현하지 않았으면 강제 종료 시켜 버린다.
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
//        Log.d("DetailFragment","detailView====================================="+detailView);
        if(viewHolder == null){

            view = inflater.inflate(R.layout.fragment_pager, container, false);
            viewHolder = new DetailView(view,this, playerInterface);
        }
        else{
            view = viewHolder.getView();
        }
//        Bundle bundle = getArguments();
//        position = bundle.getInt(ARG1);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewHolder.init(position);
    }

    public void setDestroy(){
        viewHolder.setDestroy();
    }
    public interface PlayerInterface {
        void playPlayer();
        void pausePlayer();
        void stopPlayer();
        void initPlayer();
    }
}