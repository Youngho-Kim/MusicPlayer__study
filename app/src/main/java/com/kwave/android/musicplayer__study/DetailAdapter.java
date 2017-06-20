package com.kwave.android.musicplayer__study;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kwave.android.musicplayer__study.domain.Music;

import java.util.List;

/**
 * Created by kwave on 2017-06-16.
 */

public class DetailAdapter extends PagerAdapter {
    List<Music.Item> datas = null;


    public DetailAdapter(List<Music.Item> datas) {
//        this.datas = new ArrayList<>(datas);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }


// RecyclerView의 onBindViewholder의 역할
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_pager_item,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);

        Glide.with(container.getContext())
                .load(datas.get(position).albumArt)
                .into(imageView);
//Log.d("albumArt", "datas.get(position).albumArt : " +datas.get(position).albumArt);
        textView.setText(datas.get(position).title);
//Log.d("title", "datas.get(position).title : " +datas.get(position).title);
        // 생성한 뷰를 뷰페이저 container에 담아준다.
        container.addView(view);

        return view;
    }


    // 화면에 사라진 뷰를 메모리에서 제거하는 역할
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    // instantiateItem 에서 리턴한 Object 가 View 가 맞는지를 확인한다.
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
