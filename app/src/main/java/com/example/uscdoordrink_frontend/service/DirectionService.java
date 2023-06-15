package com.example.uscdoordrink_frontend.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.utils.DirectionHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.rpc.context.AttributeContext;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/28 3:06
 */
public class DirectionService extends Service {

    public class DirectionServiceBinder extends Binder{
        public DirectionService getService(){
            return DirectionService.this;
        }
    }

    private final IBinder mBinder = new DirectionServiceBinder();

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    public void getDirections(final String URL, final OnSuccessCallBack<Response> successCallBack, final OnFailureCallBack<Exception> failureCallBack){
        Thread otherThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(URL)
                            .method("GET", null)
                            .build();
                    Response response = client.newCall(request).execute();
                    successCallBack.onSuccess(response);
                }catch(IOException e){
                    failureCallBack.onFailure(e);
                }
            }
        });
        otherThread.start();
    }
}
