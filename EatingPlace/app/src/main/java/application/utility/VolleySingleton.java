package application.utility;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.uuser.eatingplace.ApplicationController;

import java.util.ArrayList;

/**
 * Created by Uuser on 2015/4/27.
 */
public class VolleySingleton {

    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    static int maxMemory = (int) Runtime.getRuntime().maxMemory();
    static int mCacheSize = maxMemory / 8;
    private static final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(mCacheSize);

    private VolleySingleton(){

        mRequestQueue = Volley.newRequestQueue(ApplicationController.getAppContext());
        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static VolleySingleton getInstance(){
        if(mInstance == null){
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public static void removeAllCache(){
        mCache.evictAll();

    }

    public RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return this.mImageLoader;
    }
}
