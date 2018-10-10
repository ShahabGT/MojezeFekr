package projects.shahabgt.com.onlinelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.changer.audiowife.AudioWife;
import projects.shahabgt.com.onlinelibrary.adapters.SubjectsAdapter;
import projects.shahabgt.com.onlinelibrary.adapters.SubsetAdapter;
import projects.shahabgt.com.onlinelibrary.classes.Mysingleton;
import projects.shahabgt.com.onlinelibrary.models.SubjectsModel;
import projects.shahabgt.com.onlinelibrary.models.SubsetModel;

import static projects.shahabgt.com.onlinelibrary.classes.Network.checknet;

public class SubsetActivity extends AppCompatActivity {
    ImageView back;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubsetAdapter adapter;
    LinearLayout loadinglayout;
    SimpleDraweeView loadingimg;
    SharedPreferences sp;
    Bundle bundle;
    String s_id="",subject="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(SubsetActivity.this);
        setContentView(R.layout.activity_subset);
        back = findViewById(R.id.subset_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadinglayout = findViewById(R.id.subset_loadinglayout);
        loadingimg = findViewById(R.id.subset_loading);
        Uri loadinguri= Uri.parse("asset:///loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(loadinguri)
                .setAutoPlayAnimations(true)
                .build();
        loadingimg.setController(controller);
        loadinglayout.setVisibility(View.VISIBLE);
        bundle = getIntent().getExtras();
        s_id=bundle.getString("s_id");
        subject=bundle.getString("subject");
        getsubset();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        AudioWife.getInstance().pause();
        AudioWife.getInstance().release();
        super.onBackPressed();

    }

    private void getsubset(){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet(SubsetActivity.this)) {
            final String url=getResources().getString(R.string.url)+"getsubsets.php";
            StringRequest subsetrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("nothing")){
                        Toast.makeText(SubsetActivity.this,"چیزی برای نمایش وجود ندارد", Toast.LENGTH_LONG).show();
                        onBackPressed();

                    }else
                    {
                        recyclerView = findViewById(R.id.subset_recylcer);
                        ArrayList<SubsetModel> subsetModels = new ArrayList<SubsetModel>();
                        subsetModels.clear();
                        try{
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                SubsetModel subsetModel = new SubsetModel();
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                subsetModel.set_id(jsonobject.getString("id"));
                                subsetModel.set_title(jsonobject.getString("title"));
                                subsetModel.set_subject(subject);
                                subsetModel.set_s_id(s_id);

                                subsetModels.add(subsetModel);

                            }


                        }catch (Exception e){
                            Toast.makeText(SubsetActivity.this,"لطفا دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        layoutManager = new LinearLayoutManager(SubsetActivity.this);
                        adapter = new SubsetAdapter(SubsetActivity.this,getApplicationContext(), subsetModels);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        loadinglayout.setVisibility(View.GONE);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SubsetActivity.this,"لطفا دوباره امتحان کنید.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("s_id",s_id);
                    return params;
                }

            };
            Mysingleton.getmInstance(SubsetActivity.this).addToRequestque(subsetrequest);

        }else{
            Toast.makeText(SubsetActivity.this,"اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    public static void play(Activity activity,Uri uri) {
        try {
            AudioWife.getInstance().pause();
            AudioWife.getInstance().release();
            SeekBar mMediaSeekBar;
            ImageView mPlayMedia, mPauseMedia;
            TextView mRunTime, mTotalTime;
            mPlayMedia = activity.findViewById(R.id.nohe_player_play);
            mPauseMedia = activity.findViewById(R.id.nohe_player_pause);
            mMediaSeekBar = activity.findViewById(R.id.nohe_player_seekbar);
            mRunTime = activity.findViewById(R.id.nohe_player_runtime);
            mTotalTime = activity.findViewById(R.id.nohe_player_totaltime);
            AudioWife.getInstance()
                    .init(activity.getApplicationContext(), uri)
                    .setPlayView(mPlayMedia)
                    .setPauseView(mPauseMedia)
                    .setSeekBar(mMediaSeekBar)
                    .setRuntimeView(mRunTime)
                    .setTotalTimeView(mTotalTime)
                    .play();
        }catch (Exception e){
            Toast.makeText(activity,"لطفا دوباره امتحان کنید",Toast.LENGTH_LONG).show();
        }
    }
}
