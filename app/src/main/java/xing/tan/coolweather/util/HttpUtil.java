package xing.tan.coolweather.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Tx.Loooper on 2018/11/5.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        Log.v("TAG", "url:"+address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
