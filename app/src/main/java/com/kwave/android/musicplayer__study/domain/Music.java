package com.kwave.android.musicplayer__study.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kwave on 2017-06-14.
 */

public class Music {
    private static Music instance = null;
    // 중복을 방지하기 위해 데이터 저장소의 형태를 Set으로 설정
   private Set<Item> items = null;

    private Music() {
        items  = new HashSet<>();
    }

    public static Music getInstance(){
        if(instance == null)
            instance = new Music();
        return instance;
    }

    public List<Item> getItems(){
        return new ArrayList<>(items);
    }

    //음악 데이터를 폰에서 꺼낸 다음 List 저장소에 담아둔다.
    public void loader(Context context){
        // 데이터가 계속 쌓이는 것을 방지한다.
        items.clear();
        ContentResolver resolver = context.getContentResolver();

    // 1. 테이블 명 정의
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    // 2. 가져올 컬럼명 정의
    String proj[] = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
    };
    // 3. 쿼리
    Cursor cursor = resolver.query(uri, proj,null,null,null);
      if(cursor != null) {
          while (cursor.moveToNext()) {
              Item item = new Item();
              item.id = getValue(cursor,proj[0]);
//              item.id = proj[0];
//              Log.d("item.id", "id = "+item.id);
              item.albumId = getValue(cursor, proj[1]);
              item.title = getValue(cursor, proj[2]);
              item.artist = getValue(cursor, proj[3]);

              item.musicUri = makeMusicUri(item.id);
//              item.artistUri = makeMusicUri(item.artist);
              item.albumArt = makeAlbumUri(item.albumId);

              // 저장소에 담는다.
              items.add(item);
          }
      }
      // 커서 꼭 닫을 것
        cursor.close();
    }

    private String getValue(Cursor cursor , String name){
        int index = cursor.getColumnIndex(name);
//        Log.d("name", "name = "+name);
//        Log.d("index", "index = "+index);
//        Log.d("cursor.getString(index)", "cursor.getString(index) = "+cursor.getString(index));

        return cursor.getString(index);
    }



    // Set이 정상적으로 중복값을 허용하지 않도록 어떤 함수를 오버라이드해서 구현하세요.
    public  class Item {
        public String id;
        public String albumId;
        public String title;
        public String artist;

        public Uri musicUri;
        public Uri artistUri;
        public Uri albumArt;

        public boolean ITEM_FLAG = false;
//
//        @Override
//        public boolean equals(Object obj) {
//            if(!(obj instanceof Item)){
//                return false;
//            }
//            return id.equals(obj.toString());
//        }


        @Override
        public boolean equals(Object item) {
            // object가 같은지 비교
//            if (this == o) return true;
            // null 체크
            if(item == null) return false;
            // 객체 타입 쳌,
            if (this.getClass() != item.getClass()) return false;

//            Item item = (Item) o;

//            return id.equals(item.id);
            // 키값의 hashcode 비교
//            return id.hashCode() == item.hashCode();
            // 키값의 직접 비교
            return artist.equals(item.toString());


        }
        @Override
        public String toString() {
            return artist;
        }


        @Override
        public int hashCode() {
            return artist.hashCode();
        }

        /**
         * String string = "문자열" + "문자열1" + "문자열2"
         *                  -------------------
         *                      메모리 1        + "문자열2"
         *                      --------------------------
         *                              메모리 2
         */
    }

    Item item1 = new Item();
    Item item2 = new Item();

    // Set.add(item1);
    // Set.add(item2);      < equals 함수를 호출 : item1.equals(item2)



    private Uri makeMusicUri(String musicId){
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Log.d("Music", "contentUri = "+contentUri);
        return Uri.withAppendedPath(contentUri, musicId);
    }
    private Uri makeAlbumUri(String albumId){
        String albumuri = "content://media/external/audio/albumart/";
        return Uri.parse(albumuri + albumId);
    }
}


