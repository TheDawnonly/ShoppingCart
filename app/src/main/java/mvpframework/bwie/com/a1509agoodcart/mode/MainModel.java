package mvpframework.bwie.com.a1509agoodcart.mode;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;

import mvpframework.bwie.com.a1509agoodcart.bean.GoosBean;
import mvpframework.bwie.com.a1509agoodcart.net.Api;
import mvpframework.bwie.com.a1509agoodcart.net.HttpUtils;
import mvpframework.bwie.com.a1509agoodcart.net.OnNetListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by peng on 2017/11/16.
 */

public class MainModel implements IMainModel {
    private Handler handler = new Handler(Looper.getMainLooper());


    public void getGoods(final OnNetListener<GoosBean> onNetListener) {
        HttpUtils.getHttpUtils().doGet(Api.url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                final GoosBean goosBean = new Gson().fromJson(string, GoosBean.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onNetListener.onSuccess(goosBean);
                    }
                });
            }
        });
    }
}
