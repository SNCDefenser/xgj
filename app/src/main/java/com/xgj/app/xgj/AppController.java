package com.xgj.app.xgj;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application{

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController controller;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
        requestQueue = getRequestQueue();
        imageLoader = getImageLoader();
    }

    public static synchronized AppController getInstance() {
        return controller;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {
        if(imageLoader == null){
            imageLoader = new ImageLoader(getRequestQueue(),
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
        }
        return imageLoader;
    }
}
