package com.kwave.android.musicplayer__study;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kwave.android.musicplayer__study.ListFragment.OnListFragmentInteractionListener;
import com.kwave.android.musicplayer__study.domain.Music;
import com.kwave.android.musicplayer__study.dummy.DummyContent.DummyItem;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    private Context context = null;
    // 데이터 저장소
    private List<Music.Item> datas;

    public ListAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }


    public void setDatas(List<Music.Item> datas){
        // Set에서 데이터 꺼내서 사용을 하는데 index를 필요로 하는 경우 array에 담는다.
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null)
            context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // onBindViewHolder에 인자로 들어오는 것은 화면에 보이는 리스트에 해당한다.
    // 각 아이템리스트마다 뷰홀더를 갖고 있으며 여기서는 화면에 보이는 리스트의 뷰홀더와 그에 해당하는 포지션을 갖늗다.
    // 예를 들면 내가 1번 홀더로 포지션 1번 값을 재생 시키고 스크롤을 올린다고 할때
    // 내 앞에 보이는 것이 1번 포지션에 21번 포지션이라면 여기서는
    // View홀더는 1번이고 position은 21번이다.
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // datas 저장소에 들어가 있는 Music.Item 한개를 꺼낸다.
//        Music.Item item = datas.get(position);

        holder.position = position;
        holder.musicUri = datas.get(position).musicUri;
        holder.mIdView.setText(datas.get(position).id);
        holder.mContentView.setText(datas.get(position).title);
        //holder.imgAlbum.setImageURI(datas.get(position).albumArt);
        Glide
                .with(context)
                .load(datas.get(position).albumArt) // 로드할 대상
                .placeholder(R.mipmap.icon)         // 로드가 안됬을 경우
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.imgAlbum);             // 이미지를 출력할 대상

//        // 아이템이 클릭 상태인지를 체크해서 puase를 보일지 결정
//        if(datas.get(position).ITEM_FLAG == false){
//            holder.btnPause.setVisibility(View.GONE);
//        }
//        else if(datas.get(position).ITEM_FLAG == true){
//            holder.btnPause.setVisibility(View.VISIBLE);
//        }

        // pause 버튼의 보임 유무를 결정한다.
        if(datas.get(position).ITEM_FLAG){
            holder.btnPause.setVisibility(View.VISIBLE);
        }
        else{
            holder.btnPause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    public void goDetail(int position){
        mListener.goDetailInteraction(position);
    }


    public void setItemClicked(int position){
        for(Music.Item item : datas){
            item.ITEM_FLAG = false;
        }
        datas.get(position).ITEM_FLAG = true;

        // 리스트뷰 전체를 갱신해준다.
        notifyDataSetChanged(); // adapter 갱신하기
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public Uri musicUri;
        public final View mView;
        public final TextView mIdView;      // id
        public final TextView mContentView; // title
        public final ImageView imgAlbum;
        public final ImageButton btnPause;



//        public Music.Item item;             //

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            imgAlbum = (ImageView) view.findViewById(R.id.imgAlbum);
            btnPause = (ImageButton) view.findViewById(R.id.btnPause);


            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    if(preViewHolder != null){
////                        preViewHolder.btnPause.setImageResource(android.R.drawable.ic_media_play);
//                        preViewHolder.btnPause.setVisibility(View.GONE);
//                        datas.get(preViewHolder.position).ITEM_FLAG = 0;
//                        Log.d("btnPause","Preposition = "+preViewHolder.position + "  position = "+position);
//                    }
                    setItemClicked(position);
                    Player.init(musicUri, mView.getContext(),null);
                    Player.play();
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
//                    btnPause.setVisibility(View.VISIBLE);
//                    preViewHolder = DetailView.this;

//                    datas.get(position).ITEM_FLAG = 1;

                }
            });

            // 상세보기로 이동 -> 뷰페이저이동
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    goDetail(position);
                    return true;        // 롱클릭 후 온클릭이 실행되지 않도록 한다.
                }
            });

            // pause 버튼 클릭
            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (Player.status){
                        case Const.player.PLAY :
                            Player.pause();
                            // pause가 클릭 되면 이미지 모양이 play로 바뀐더.
                            btnPause.setImageResource(android.R.drawable.ic_media_play);
                            break;
                        case Const.player.PAUSE :
                            Player.Replay();
                            break;
                        case Const.player.STOP :
                            btnPause.setVisibility(View.GONE);
                            break;
                    }
                }
            });





        }


//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
