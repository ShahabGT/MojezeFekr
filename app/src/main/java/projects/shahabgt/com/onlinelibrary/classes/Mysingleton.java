package projects.shahabgt.com.onlinelibrary.classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jonathan on 7/12/2017.
 */

public class Mysingleton {

    private static  Mysingleton mInstance;
    private RequestQueue requestQueue;
    private  Context mCtx;

    private Mysingleton(Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }
    private RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized  Mysingleton getmInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance = new Mysingleton(context);
        }
        return mInstance;
    }
    public<T> void addToRequestque (Request<T> request)
    {
        requestQueue.add(request);
    }



}
