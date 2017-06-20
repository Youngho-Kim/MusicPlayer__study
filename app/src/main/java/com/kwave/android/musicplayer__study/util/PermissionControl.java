package com.kwave.android.musicplayer__study.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by kwave on 2017-06-15.
 */

public class PermissionControl {
    public static final int REQ_FLAG = 10000;      // 갤러리가 호출했음을 알려주는 키값
    // 사용할 권한을 미리 배열로 넣어둠
   private static String permission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};


    public static void checkVersion(Activity activity){
        // api level이 23이상일 경우만 실행
        // 마시멜로 이상인 경우만 런타임 체크
        // Build.VERSION.SDK_INT = 설치 안드로이드폰의 api level 가져오기
        // VERSION_CODES 아래에 상수로 각 버전별 api level이 작성되어 있다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission(activity);
        }
        else{
            // 아니면 init
            callInit(activity);
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkPermission(Activity activity){             // 권한 체크
        // 1. 권한체크 - 특정 권한이 있는지 시스템에 물어본다.              WRITE_EXTERNAL_STORAGE : 외부저장소에 쓰기
         boolean denied = false;
        for(String perm : permission){
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                denied = true;
                break;
            }
        }



        if(denied){
            // 2. 권한이 없으면 사용자에 권한을 달라고 요청
            activity.requestPermissions(permission, REQ_FLAG); // -> 권한을 요구하는 팝업이 사용자 화면에 노출된다.
        }
        else {
            callInit(activity);
        }
    }

    public static void onResult(Activity activity, int requestCode, @NonNull int grantResults[]){
        if(requestCode == REQ_FLAG){      // 갤러리에서 승인 요청 했음
            boolean granted = true;
            for(int grant : grantResults){
                if(grant != PackageManager.PERMISSION_GRANTED){
                    granted = false;
                    break;
                }
            }

            // 3.1 사용자가 승인을 했음
            if(granted){
                callInit(activity);
            }
            // 3.2 사용자가 거절했음
            else{
//                activity.finish();
                Toast.makeText(activity,"권한요청을 승인하셔야 앱을 사용할 수 있습니다.",Toast.LENGTH_SHORT).show();
            }
        }
//        else if(requestCode == REQ1_PERMISSION){      // 카메라에서 승인 요청 했음
//            // 3.1 사용자가 승인을 했음
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                init();
//            }
//            // 3.2 사용자가 거절했음
//            else{
//                Toast.makeText(this,"권한요청을 승인하셔야 앱을 사용할 수 있습니다.",Toast.LENGTH_SHORT).show();
//            }
//        }
    }



    private static void callInit(Activity activity){
        if(activity instanceof CallBack){
            ((CallBack)activity).init();
        }
        else {
            throw new RuntimeException("must implement this CallBack");
        }
    }






    public interface CallBack{
        public void init();
    }


}
